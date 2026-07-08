package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {

    private Titular titular;
    private ContaCorrente contaCorrente;
    private ContaPoupanca contaPoupanca;

    @BeforeEach
    void setUp() {
        titular = new Titular("Maria Teste", "000.000.000-00");
        contaCorrente = new ContaCorrente(1, 100, titular, 500.0, 200.0);
        contaPoupanca = new ContaPoupanca(1, 200, titular, 300.0);
    }

    @Test
    void deveDepositarValorPositivo() {
        contaCorrente.depositar(100.0);

        assertEquals(600.0, contaCorrente.getSaldo(), 0.001);
    }

    @Test
    void naoDeveDepositarValorNegativoOuZero() {
        assertThrows(IllegalArgumentException.class, () -> contaCorrente.depositar(-10.0));
        assertThrows(IllegalArgumentException.class, () -> contaCorrente.depositar(0.0));
    }

    @Test
    void deveSacarDentroDoSaldoNaContaPoupanca() {
        contaPoupanca.sacar(100.0);

        assertEquals(200.0, contaPoupanca.getSaldo(), 0.001);
    }

    @Test
    void naoDeveSacarMaisQueOSaldoNaContaPoupanca() {
        assertThrows(IllegalStateException.class, () -> contaPoupanca.sacar(9999.0));
    }

    @Test
    void deveSacarUsandoLimiteNaContaCorrente() {
        contaCorrente.sacar(600.0);

        assertEquals(0.0, contaCorrente.getSaldo(), 0.001,
                "Saldo deveria zerar antes de descontar a taxa");
        assertTrue(contaCorrente.getLimiteDisponivel() < 200.0,
                "O limite disponível deveria diminuir");
    }

    @Test
    void naoDeveSacarMaisQueSaldoMaisLimite() {
        assertThrows(IllegalStateException.class, () -> contaCorrente.sacar(1000.0));
    }

    @Test
    void deveTransferirEntreContas() {
        contaCorrente.transferir(contaPoupanca, 100.0);

        assertEquals(400.0, contaCorrente.getSaldo(), 0.001);
        assertEquals(400.0, contaPoupanca.getSaldo(), 0.001);
    }

    @Test
    void naoDeveTransferirParaAPropriaConta() {
        assertThrows(IllegalArgumentException.class,
                () -> contaCorrente.transferir(contaCorrente, 10.0));
    }

    @Test
    void contaPoupancaDeveRenderMensalmente() {
        double saldoAntes = contaPoupanca.getSaldo();

        contaPoupanca.aplicarAtualizacaoMensal();

        assertTrue(contaPoupanca.getSaldo() > saldoAntes,
                "O saldo deveria aumentar após o rendimento");
    }

    @Test
    void contaCorrenteDeveCobrarTarifaMensal() {
        double saldoAntes = contaCorrente.getSaldo();

        contaCorrente.aplicarAtualizacaoMensal();

        assertTrue(contaCorrente.getSaldo() < saldoAntes,
                "O saldo deveria diminuir após a tarifa");
    }

    @Test
    void contaCorrenteDeveCalcularTaxaComoTributavel() {
        Tributavel tributavel = contaCorrente;

        assertEquals(12.00, tributavel.calcularTaxa(), 0.001);
    }

    @Test
    void naoDeveCriarContaComTitularNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaPoupanca(1, 300, null, 100.0));
    }

    @Test
    void naoDeveCriarContaComSaldoInicialNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContaPoupanca(1, 300, titular, -50.0));
    }
}
