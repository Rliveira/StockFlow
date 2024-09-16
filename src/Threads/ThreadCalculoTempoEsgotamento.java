package Threads;

import Beans.Estoque;
import Beans.Produto;
import Repositorios.RepositorioOperacao;

import java.util.ArrayList;
import java.util.List;

public class ThreadCalculoTempoEsgotamento extends Thread
{
    private String idThread;
    private Estoque estoque;
    private RepositorioOperacao repositorioOperacao;

    public ThreadCalculoTempoEsgotamento(String nomeThread, Estoque estoque, RepositorioOperacao repositorioOperacao) {
        super(nomeThread);
        this.estoque = estoque;
        this.repositorioOperacao = repositorioOperacao;
    }

    @Override
    public void run() {
        List<Produto> produtos = new ArrayList<>(estoque.getProdutos());

        for (Produto produto : produtos) {
            try {
                double demandaPrevista = repositorioOperacao.calcularDemandaPrevista(produto, "Retirada");

                if (demandaPrevista > 0) {
                    double quantidadeEmEstoque = produto.getQuantidade();
                    double tempoEsgotamento = quantidadeEmEstoque / demandaPrevista;

                    System.out.printf("Produto: %s - Estoque será esgotado em aproximadamente %.2f dias com base na demanda prevista.\n",
                            produto.getNome(), tempoEsgotamento);

                } else {
                    System.out.println("Demanda insuficiente para prever esgotamento do produto: " + produto.getNome());
                }
            }
            catch (Exception e)
            {
                System.out.println("Erro ao calcular esgotamento do produto: " + produto.getNome());
            }

        }
    }
}
