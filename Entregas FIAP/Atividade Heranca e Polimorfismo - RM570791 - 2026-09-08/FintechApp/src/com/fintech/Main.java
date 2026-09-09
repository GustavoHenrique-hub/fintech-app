package com.fintech;

public class Main {

    public static void main(String[] args) {
        Usuario usuario1 = new Usuario("Ana Souza", "123.456.789-00", "ana.souza@email.com", "senha123");
        Usuario usuario2 = new Usuario("Bruno Lima", "987.654.321-00", "bruno.lima@email.com", "outraSenha456");

        System.out.println("=== Usuarios cadastrados ===");
        System.out.println(usuario1);
        System.out.println(usuario2);
        System.out.println("Autenticacao usuario1 com senha correta: " + usuario1.autenticar("senha123"));
        System.out.println("Autenticacao usuario1 com senha errada: " + usuario1.autenticar("errada"));

        ContaCorrente contaCorrente = new ContaCorrente("CC-0001", usuario1, 500.0, 1000.0);
        ContaPoupanca contaPoupanca = new ContaPoupanca("PP-0002", usuario1, 300.0, 0.006);
        ContaInvestimento contaInvestimento = new ContaInvestimento("CI-0003", usuario2, 2000.0, 0.012, 0.15);

        System.out.println();
        System.out.println("=== Movimentacoes ===");
        contaCorrente.depositar(200.0);
        contaCorrente.sacar(1300.0);
        contaPoupanca.depositar(150.0);
        contaInvestimento.sacar(500.0);

        try {
            contaPoupanca.sacar(10000.0);
        } catch (SaldoInsuficienteException e) {
            System.out.println("Falha esperada ao sacar da poupanca: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Polimorfismo: mesma chamada, comportamento diferente por subclasse ===");
        ContaBancaria[] contas = { contaCorrente, contaPoupanca, contaInvestimento };
        for (ContaBancaria conta : contas) {
            System.out.println(conta);
            System.out.printf("Rendimento calculado: R$ %.2f%n", conta.calcularRendimento());
        }

        contaPoupanca.aplicarRendimento();
        contaInvestimento.aplicarRendimento();

        System.out.println();
        System.out.println("=== Extratos apos aplicar rendimentos ===");
        System.out.print(contaCorrente.exibirExtrato());
        System.out.print(contaPoupanca.exibirExtrato());
        System.out.print(contaInvestimento.exibirExtrato());

        System.out.println();
        System.out.println("=== Categorias ===");
        Categoria salario = new Categoria("Salario", TipoCategoria.RECEITA);
        Categoria mercado = new Categoria("Mercado", TipoCategoria.GASTO);
        System.out.println(salario);
        System.out.println(mercado);
    }
}
