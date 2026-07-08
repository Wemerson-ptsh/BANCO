package model;

public class ContaCorrente extends Conta implements Tributavel {

    private static final double TAXA_OPERACAO_SOBRE_LIMITE = 0.02;
    private static final double TARIFA_MENSAL = 12.00;

    private double limite;

    public ContaCorrente(int agencia, int numero, Titular titular, double saldo, double limite) {
        super(agencia, numero, titular, saldo);

        if (limite < 0) {
            throw new IllegalArgumentException("O limite não pode ser negativo.");
        }

        this.limite = limite;
    }

    @Override
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Não é possível sacar um valor negativo ou zero.");
        }

        if (valor <= saldo) {
            saldo -= valor;
            return;
        }

        double valorUsandoLimite = valor - saldo;
        double taxa = valorUsandoLimite * TAXA_OPERACAO_SOBRE_LIMITE;
        double totalDescontadoDoLimite = valorUsandoLimite + taxa;

        if (totalDescontadoDoLimite > limite) {
            throw new IllegalStateException("Saldo e limite insuficientes para realizar o saque.");
        }

        saldo = 0;
        limite -= totalDescontadoDoLimite;
    }

    @Override
    public void aplicarAtualizacaoMensal() {
        this.saldo -= TARIFA_MENSAL;
    }

    @Override
    public double calcularTaxa() {
        return TARIFA_MENSAL;
    }

    public double getLimite() {
        return limite;
    }

    public double getLimiteDisponivel() {
        return limite;
    }

    @Override
    public String toString() {
        return "[Conta Corrente] " + super.toString() + String.format(" | Limite: R$ %.2f", limite);
    }
}
