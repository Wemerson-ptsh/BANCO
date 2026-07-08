package model;

public class ContaPoupanca extends Conta {

    private static final double TAXA_RENDIMENTO_MENSAL = 0.005;

    public ContaPoupanca(int agencia, int numero, Titular titular, double saldo) {
        super(agencia, numero, titular, saldo);
    }

    @Override
    public void aplicarAtualizacaoMensal() {
        this.saldo += this.saldo * TAXA_RENDIMENTO_MENSAL;
    }

    @Override
    public String toString() {
        return "[Conta Poupança] " + super.toString();
    }
}
