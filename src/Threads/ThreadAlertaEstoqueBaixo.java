package Threads;

import Beans.Estoque;
import Beans.Operacao;
import Beans.Produto;
import Repositorios.RepositorioOperacao;

import java.util.List;
import java.util.Scanner;

public class ThreadAlertaEstoqueBaixo  extends Thread implements ThreadProcessamento
{
    private String idThread;
    private Estoque estoque;
    private RepositorioOperacao repositorioOperacao;
    private Scanner scanner;

    public ThreadAlertaEstoqueBaixo(String idThread, RepositorioOperacao repositorioOperacao, Estoque estoque)
    {
        this.idThread = idThread;
        this.estoque = estoque;
        this.repositorioOperacao = repositorioOperacao;
    }



    @Override
    public void run() {
        List<Produto> produtos = estoque.getProdutos();

        for (Produto produto : produtos)
        {
            double limiteEstoqueBaixo = calcularLimiteEstoqueBaixo(produto);

            if (produto.getQuantidade() < limiteEstoqueBaixo) {
                System.out.println("\nAlerta! O estoque do produto '" + produto.getNome() + "' está baixo: " + produto.getQuantidade() + " unidades.");
                System.out.printf("O limite considerado baixo para este produto é: %.2f unidades.\n", limiteEstoqueBaixo);
                System.out.print("Deseja repor o estoque deste produto? (S/N): ");
                String resposta = scanner.nextLine();

                if (resposta.equalsIgnoreCase("S")) {
                    System.out.print("Digite a quantidade para repor: ");
                    int quantidadeRepor = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Digite o valor unitário: ");
                    double valorUnitario = scanner.nextDouble();
                    scanner.nextLine();

                    //Reposição do estoque
                    estoque.repor(produto, quantidadeRepor, valorUnitario);
                    System.out.println("Estoque do produto '" + produto.getNome() + "' foi reposto com sucesso.");
                }
            }
        }
    }

    private double calcularLimiteEstoqueBaixo(Produto produto)
    {
        List<Operacao> operacoesRetiradas = repositorioOperacao.listarOperacoesPorTipo("Retirada");

        if(operacoesRetiradas.isEmpty())
        {
            return 0;
        }

        double somaRetiradas = 0;

        for(Operacao operacao : operacoesRetiradas)
        {
            somaRetiradas += operacao.getQuantidade();;
        }

        double mediaRetiradas = somaRetiradas / operacoesRetiradas.size();

        return mediaRetiradas * 0.15;
    }

    @Override
    public String getIdThread() {
        return null;
    }

    @Override
    public void setProduto(Produto produto) {

    }

    @Override
    public boolean isTemProduto() {
        return false;
    }
}