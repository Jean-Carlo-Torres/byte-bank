package br.com.alura.bytebank;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import br.com.alura.bytebank.domain.conta.ContaService;
import br.com.alura.bytebank.domain.conta.DadosAberturaConta;

import java.util.Scanner;

public class BytebankApplication {

    private static ContaService service = new ContaService();
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) {
        var opcao = exibirMenu();
        while (opcao != 0) {
            try {
                switch (opcao) {
                    case 1 -> listarContas();
                    case 2 -> abrirConta();
                    case 3 -> encerrarConta();
                    case 4 -> consultarSaldo();
                    case 5 -> realizarSaque();
                    case 6 -> realizarDeposito();
                    case 7 -> buscarPorNumero();
                    case 8 -> realizarTransferencia();
                }
            } catch (RegraDeNegocioException e) {
                System.out.println("Erro: " + e.getMessage());
                System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
                sc.next();
            }
            opcao = exibirMenu();
        }

        System.out.println("Finalizando a aplicação.");
    }

    private static int exibirMenu() {
        System.out.println("""
                BYTEBANK - ESCOLHA UMA OPÇÃO:
                1 - Listar contas abertas
                2 - Abertura de conta
                3 - Encerramento de conta
                4 - Consultar saldo de uma conta
                5 - Realizar saque em uma conta
                6 - Realizar depósito em uma conta
                7 - Buscar conta por número
                8 - Realizar transferência
                0 - Sair
                """);
        return sc.nextInt();
    }

    private static void listarContas() {
        System.out.println("Contas cadastradas:");
        var contas = service.listarContasAbertas();
        contas.stream().forEach(System.out::println);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void abrirConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = sc.nextInt();

        System.out.println("Digite o nome do cliente:");
        var nome = sc.next();

        System.out.println("Digite o cpf do cliente:");
        var cpf = sc.next();

        System.out.println("Digite o email do cliente:");
        var email = sc.next();

        service.abrir(new DadosAberturaConta(numeroDaConta, new DadosCadastroCliente(nome, cpf, email)));

        System.out.println("Conta aberta com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void encerrarConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = sc.nextInt();

        service.desativarConta(numeroDaConta);

        System.out.println("Conta encerrada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void consultarSaldo() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = sc.nextInt();
        var saldo = service.consultarSaldo(numeroDaConta);
        System.out.println("Saldo da conta: " + saldo);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void realizarSaque() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = sc.nextInt();

        System.out.println("Digite o valor do saque:");
        var valor = sc.nextBigDecimal();

        service.realizarSaque(numeroDaConta, valor);
        System.out.println("Saque realizado com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void realizarDeposito() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = sc.nextInt();

        System.out.println("Digite o valor do depósito:");
        var valor = sc.nextBigDecimal();

        service.realizarDeposito(numeroDaConta, valor);

        System.out.println("Depósito realizado com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void buscarPorNumero() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = sc.nextInt();

        try {
            System.out.println(service.buscarContaPorNumero(numeroDaConta));
        } catch (RegraDeNegocioException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }

    private static void realizarTransferencia() {
        System.out.println("Digite o número da conta de origem:");
        var numeroDaContaOrigem = sc.nextInt();

        System.out.println("Digite o número da conta de destino:");
        var numeroDaContaDestino = sc.nextInt();

        System.out.println("Digite o valor da transferência:");
        var valor = sc.nextBigDecimal();

        service.realizarTransferencia(numeroDaContaOrigem, numeroDaContaDestino, valor);

        System.out.println("Transferência realizada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        sc.next();
    }
}
