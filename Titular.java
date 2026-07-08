package model;

import java.util.Objects;

public class Titular {

    private String nome;
    private String cpf;

    public Titular(String nome, String cpf) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do titular não pode ser vazio.");
        }

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF do titular não pode ser vazio.");
        }

        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Titular)) return false;

        Titular titular = (Titular) o;
        return cpf.equals(titular.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
