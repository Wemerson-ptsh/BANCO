package model;

public abstract class Conta {

    private final int agencia;
    private final int numero;
    private Titular titular;
    protected double saldo;

    protected Conta(int agencia, int numero, Titular titular, double saldo) {
        if (titular == null) {
            throw new IllegalArgumentException("A conta precisa de um titular.");
        }
        if (saldo < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }

        this.agencia = agencia;
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Não é possível depositar um valor negativo ou zero.");
        }

        this.saldo += valor;
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Não é possível sacar um valor negativo ou zero.");
        }

        if (valor > saldo) {
            throw new IllegalStateException("Saldo insuficiente para realizar o saque.");
        }

        this.saldo -= valor;
    }

    public void transferir(Conta destino, double valor) {
        if (destino == null) {
            throw new IllegalArgumentException("A conta de destino não pode ser nula.");
        }

        if (destino == this) {
            throw new IllegalArgumentException("Não é possível transferir para a própria conta.");
        }

        this.sacar(valor);
        destino.depositar(valor);
    }

    public abstract void aplicarAtualizacaoMensal();

    public int getAgencia() {
        return agencia;
    }

    public int getNumero() {
        return numero;
    }

    public Titular getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return String.format(
                "Agência: %04d | Conta: %06d | Titular: %s | Saldo: R$ %.2f",
                agencia, numero, titular, saldo
        );
    }
}
