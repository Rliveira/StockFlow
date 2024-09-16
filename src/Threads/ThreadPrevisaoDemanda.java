package Threads;

import Beans.Produto;
import Exceptions.HistoricoInsuficienteException;
import Repositorios.RepositorioOperacao;

public class ThreadPrevisaoDemanda extends Thread implements ThreadProcessamento{
    private String idThread;
    private Produto produto;
    private String tipoOperacao; //Reposição ou retirada
    private RepositorioOperacao repositorioOperacao;
    private boolean temProduto;  // Flag para saber se há um produto a ser processado

    // CONSTRUTOR:
    public ThreadPrevisaoDemanda(String id, RepositorioOperacao repositorioOperacao) {
        this.idThread = id;
        this.produto = null;
        this.tipoOperacao = "";
        this.repositorioOperacao = repositorioOperacao;
        this.temProduto = false;
    }

    // MÉTODOS:
    @Override
    public void run() {
        while (true) { // Loop contínuo
            synchronized (this) {
                while (!temProduto) { // Aguarda até que um produto seja atribuído
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Thread " + idThread + " foi interrompida.");
                        return; // Sai do loop se a thread for interrompida
                    }
                }

                // Processa a previsão de demanda
                if (produto != null) {
                    try {
                        int demandaPrevista = repositorioOperacao.calcularDemandaPrevista(produto, tipoOperacao);
                        System.out.println("Demanda prevista para o produto " + produto.getNome() + ": " + demandaPrevista + " unidades.");
                    } catch (HistoricoInsuficienteException e){
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Produto não definido para a thread " + idThread);
                }

                temProduto = false; // Reseta a flag após processar o produto
                notifyAll(); // Notifica outras threads para continuar
            }
        }
    }

    // Atribui um produto e sinaliza a thread para começar a trabalhar.
    @Override
    public synchronized void setProduto(Produto produto) {
        this.produto = produto;
        this.temProduto = true;
        notify(); // Notifica a thread para sair do estado de espera e realizar o cálculo de previsão por demanda.
    }

    public synchronized void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    // GETs and SETs:
    @Override
    public String getIdThread() {
        return idThread;
    }

    public Produto getProduto() {
        return produto;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public RepositorioOperacao getRepositorioOperacao() {
        return repositorioOperacao;
    }

    public void setRepositorioOperacao(RepositorioOperacao repositorioOperacao) {
        this.repositorioOperacao = repositorioOperacao;
    }

    @Override
    public boolean isTemProduto() {
        return temProduto;
    }

    public void setTemProduto(boolean temProduto) {
        this.temProduto = temProduto;
    }
}
