package app;

import model.Conta;
import model.ContaCorrente;
import model.ContaPoupanca;
import model.Titular;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class App {

    private static final List<Conta> contas = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Locale.setDefault(Locale.of("pt", "BR"));

        System.out.println("=======================================");
        System.out.println("   SISTEMA BANCÁRIO - DEMONSTRAÇÃO");
        System.out.println("=======================================\n");
        demonstracaoAutomatica();

        System.out.println("\n=======================================");
        System.out.println("   SISTEMA BANCÁRIO - MENU INTERATIVO");
        System.out.println("=======================================");
        menuInterativo();

        System.out.println("\nEncerrando o sistema. Até logo!");
        scanner.close();
    }

    private static void demonstracaoAutomatica() {
        Titular titularChico = new Titular("Chico", "111.111.111-11");
        Titular titularToin = new Titular("Toin", "222.222.222-22");

        ContaCorrente chico = new ContaCorrente(1, 1001, titularChico, 500.0, 300.0);
        ContaPoupanca toin = new ContaPoupanca(1, 1002, titularToin, 200.0);

        contas.add(chico);
        contas.add(toin);

        System.out.println("--- Contas criadas ---");
        System.out.println(chico);
        System.out.println(toin);

        System.out.println("\n--- Depósito de R$ 100,00 na conta do Chico ---");
        chico.depositar(100.0);
        System.out.println(chico);

        System.out.println("\n--- Saque de R$ 700,00 na conta do Chico (usa o limite) ---");
        chico.sacar(700.0);
        System.out.println(chico);

        System.out.println("\n--- Transferência de R$ 50,00 do Chico para o Toin ---");
        chico.transferir(toin, 50.0);
        System.out.println(chico);
        System.out.println(toin);

        System.out.println("\n--- Aplicando atualização mensal (tarifa / rendimento) ---");
        chico.aplicarAtualizacaoMensal();
        toin.aplicarAtualizacaoMensal();
        System.out.println(chico);
        System.out.println(toin);

        System.out.println("\n--- Taxa cobrada pela conta corrente (Tributavel) ---");
        System.out.printf("Taxa mensal da conta do Chico: R$ %.2f%n", chico.calcularTaxa());

        System.out.println("\n--- Tentando sacar um valor negativo (deve gerar erro tratado) ---");
        try {
            chico.sacar(-10.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        System.out.println("\n--- Tentando sacar mais do que saldo + limite (deve gerar erro tratado) ---");
        try {
            chico.sacar(999999.0);
        } catch (IllegalStateException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
    }
  private static void menuInterativo() {
    int opcao;

    do {
        exibirMenu();
        opcao = lerInteiro("Escolha uma opção: ");

        switch (opcao) {
            case 1 -> criarContaCorrente();
            case 2 -> criarContaPoupanca();
            case 3 -> depositar();
            case 4 -> sacar();
            case 5 -> transferir();
            case 6 -> listarContas();
            case 7 -> aplicarAtualizacaoMensalMenu();
            case 0 -> System.out.println("Saindo do menu...");
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
    } while (opcao != 0);
}

private static void exibirMenu() {
    System.out.println("\n--------- MENU ---------");
    System.out.println("1 - Criar conta corrente");
    System.out.println("2 - Criar conta poupança");
    System.out.println("3 - Depositar");
    System.out.println("4 - Sacar");
    System.out.println("5 - Transferir");
    System.out.println("6 - Listar contas");
    System.out.println("7 - Aplicar atualização mensal em uma conta");
    System.out.println("0 - Sair");
    System.out.println("-------------------------");
}

private static void criarContaCorrente() {
    System.out.println("\n-- Criar Conta Corrente --");
    int agencia = lerInteiro("Agência: ");
    int numero = lerInteiro("Número da conta: ");
    String nome = lerTexto("Nome do titular: ");
    String cpf = lerTexto("CPF do titular: ");
    double saldo = lerDouble("Saldo inicial: ");
    double limite = lerDouble("Limite: ");

    try {
        Titular titular = new Titular(nome, cpf);
        ContaCorrente conta = new ContaCorrente(agencia, numero, titular, saldo, limite);
        contas.add(conta);
        System.out.println("Conta criada com sucesso: " + conta);
    } catch (IllegalArgumentException e) {
        System.out.println("Erro ao criar conta: " + e.getMessage());
    }
}

private static void criarContaPoupanca() {
    System.out.println("\n-- Criar Conta Poupança --");
    int agencia = lerInteiro("Agência: ");
    int numero = lerInteiro("Número da conta: ");
    String nome = lerTexto("Nome do titular: ");
    String cpf = lerTexto("CPF do titular: ");
    double saldo = lerDouble("Saldo inicial: ");

    try {
        Titular titular = new Titular(nome, cpf);
        ContaPoupanca conta = new ContaPoupanca(agencia, numero, titular, saldo);
        contas.add(conta);
        System.out.println("Conta criada com sucesso: " + conta);
    } catch (IllegalArgumentException e) {
        System.out.println("Erro ao criar conta: " + e.getMessage());
    }
}

private static void depositar() {
    Conta conta = selecionarConta("Escolha a conta para depositar: ");
    if (conta == null) return;

    double valor = lerDouble("Valor a depositar: ");

    try {
        conta.depositar(valor);
        System.out.println("Depósito realizado. " + conta);
    } catch (RuntimeException e) {
        System.out.println("Erro no depósito: " + e.getMessage());
    }
}

private static void sacar() {
    Conta conta = selecionarConta("Escolha a conta para sacar: ");
    if (conta == null) return;

    double valor = lerDouble("Valor a sacar: ");

    try {
        conta.sacar(valor);
        System.out.println("Saque realizado. " + conta);
    } catch (RuntimeException e) {
        System.out.println("Erro no saque: " + e.getMessage());
    }
}

private static void transferir() {
    System.out.println("Conta de origem:");
    Conta origem = selecionarConta("Escolha a conta de origem: ");
    if (origem == null) return;

    System.out.println("Conta de destino:");
    Conta destino = selecionarConta("Escolha a conta de destino: ");
    if (destino == null) return;

    double valor = lerDouble("Valor a transferir: ");

    try {
        origem.transferir(destino, valor);
        System.out.println("Transferência realizada com sucesso.");
        System.out.println(origem);
        System.out.println(destino);
    } catch (RuntimeException e) {
        System.out.println("Erro na transferência: " + e.getMessage());
    }
}

private static void aplicarAtualizacaoMensalMenu() {
    Conta conta = selecionarConta("Escolha a conta para aplicar a atualização mensal: ");
    if (conta == null) return;

    conta.aplicarAtualizacaoMensal();
    System.out.println("Atualização aplicada. " + conta);
}

private static void listarContas() {
    System.out.println("\n-- Contas cadastradas --");

    if (contas.isEmpty()) {
        System.out.println("Nenhuma conta cadastrada ainda.");
        return;
    }

    for (int i = 0; i < contas.size(); i++) {
        System.out.println(i + " - " + contas.get(i));
    }
}

private static Conta selecionarConta(String mensagem) {
    if (contas.isEmpty()) {
        System.out.println("Não há contas cadastradas ainda.");
        return null;
    }

    listarContas();
    int indice = lerInteiro(mensagem);

    if (indice < 0 || indice >= contas.size()) {
        System.out.println("Índice de conta inválido.");
        return null;
    }

    return contas.get(indice);
}

private static int lerInteiro(String mensagem) {
    while (true) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine().trim();

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite um número inteiro.");
        }
    }
}

private static double lerDouble(String mensagem) {
    while (true) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine().trim().replace(",", ".");

        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite um número (use ponto ou vírgula para decimais).");
        }
    }
}

private static String lerTexto(String mensagem) {
    System.out.print(mensagem);
    return scanner.nextLine().trim();
}
}
