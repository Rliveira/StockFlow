package Threads;

import Beans.Estoque;
import Beans.Produto;
import Exceptions.HistoricoInsuficienteException;
import Repositorios.RepositorioOperacao;
import StockFlow.Testes;

import java.util.ArrayList;
import java.util.List;

public class ThreadCalculoTempoEsgotamento extends Thread implements ThreadProcessamento{
    private String idThread;
    private Estoque estoque;
    private Produto produto;
    private RepositorioOperacao repositorioOperacao;
    private boolean temProduto;

    public ThreadCalculoTempoEsgotamento(String idThread, Estoque estoque, RepositorioOperacao repositorioOperacao) {
        super(idThread);
        this.estoque = estoque;
        this.repositorioOperacao = repositorioOperacao;
    }

    @Override
    public void run() {
        List<Produto> produtos = new ArrayList<>(estoque.getProdutos());

        while (true) {
            synchronized (this) {
                while (!temProduto) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Thread " + idThread + " foi interrompida.");
                        return; // Sai do loop se a thread for interrompida
                    }
                }

                // Processa calculo de esgotamento
                if (produto != null) {
                    try {
                        double demandaPrevista = repositorioOperacao.calcularDemandaPrevista(produto, "Retirada");

                        if (demandaPrevista > 0) {
                            double quantidadeEmEstoque = produto.getQuantidade();
                            double tempoEsgotamento = quantidadeEmEstoque / demandaPrevista;

                            System.out.printf("Produto: %s - Estoque será esgotado em aproximadamente %.2f dias com base na demanda prevista.\n",
                                    produto.getNome(), tempoEsgotamento);
                        }
                    } catch (HistoricoInsuficienteException e) {
                        System.out.println(e.getMessage());                    }
                } else {
                    System.out.println("Produto não definido para a thread " + idThread);
                }

                //Descomentar as 2 linhas abaixo somente quando for realizar testes
                Testes teste = Testes.getInstancia();
                teste.getListaProdutosTeste().add(produto);

                temProduto = false;
                notifyAll();
            }
        }
    }

    @Override
    public String getIdThread() {
        return idThread;
    }

    @Override
    public void setProduto(Produto produto) {
        this.produto = produto;
        this.temProduto = true;
        notify(); // Notifica a thread para sair do estado de espera e realizar o cálculo de previsão por demanda.
    }

    @Override
    public boolean isTemProduto() {
        return temProduto;
    }
}
