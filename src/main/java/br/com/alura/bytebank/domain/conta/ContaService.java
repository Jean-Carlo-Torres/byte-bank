package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

    private ConnectionFactory connection;
    private Connection conn;

    public ContaService() {
        this.connection = new ConnectionFactory();
    }

    private Set<Conta> contas = new HashSet<>();

    public Set<Conta> listarContasAbertas() {
        conn = connection.conectar();
        return new ContaDAO(conn).listar();
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        conn = connection.conectar();
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {
        conn = connection.conectar();
        new ContaDAO(conn).salvar(dadosDaConta);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        if (!conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("Conta encerrada!");
        }

        BigDecimal novoValor = conta.getSaldo().subtract(valor);
        alterar(conta, novoValor);

    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        if (!conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("Conta encerrada!");
        }

        BigDecimal novoValor = conta.getSaldo().add(valor);
        alterar(conta, novoValor);
    }

    private void alterar(Conta conta, BigDecimal valor) {
        conn = connection.conectar();
        new ContaDAO(conn).alterar(conta.getNumero(), valor);
    }

    public void realizarTransferencia(Integer numeroDaContaOrigem, Integer numeroDaContaDestino, BigDecimal valor) {
        realizarSaque(numeroDaContaOrigem, valor);
        realizarDeposito(numeroDaContaDestino, valor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        conn = connection.conectar();
        new ContaDAO(conn).deletar(conta.getNumero());
    }

    public void desativarConta(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (!conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("Conta já está desativada!");
        }

        conn = connection.conectar();
        new ContaDAO(conn).desativar(conta.getNumero());
    }

    public Conta buscarContaPorNumero(Integer numero) {
        conn = connection.conectar();
        Conta conta = new ContaDAO(conn).listarPorNumero(numero);
        if(conta != null) {
            return conta;
        } else {
            throw new RegraDeNegocioException("Não existe conta cadastrada com esse número!");
        }
    }
}
