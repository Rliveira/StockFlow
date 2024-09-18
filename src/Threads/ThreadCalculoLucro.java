package Threads;

import Beans.Produto;
import Exceptions.OperacoesInsuficientesException;
import Repositorios.RepositorioOperacao;

public class ThreadCalculoLucro extends Thread implements ThreadProcessamento {
    private String idThread;
    private RepositorioOperacao repositorioOperacoes;
    private Produto produto;
    boolean temProduto;

    // CONSTRUTOR:
    public ThreadCalculoLucro(String idThread, RepositorioOperacao repositorioOperacao) {
        this.idThread = idThread;
        this.repositorioOperacoes = repositorioOperacao;
        this.produto = null;
        this.temProduto = false; // Flag para saber se há um produto a ser processado
    }

    //MÉTODOS:
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

                // Processa o cálculo do lucro.
                if (produto != null){
                    try {
                        double lucro = repositorioOperacoes.calcularLucroPorProduto(produto);
                        if(lucro > 0){
                            System.out.println("Lucro calculado para o produto " + produto.getNome() + ": " + lucro);
                        }
                        else{
                            System.out.println("Lucro calculado para o produto " + produto.getNome() + ": " + lucro);

                        }
                    } catch (OperacoesInsuficientesException e) {
                        System.out.println(e.getMessage()); //Imprime a mensagem de erro caso a excessão seja levantada
                    }

                } else{
                    System.out.println("Produto não definido para a thread " + idThread);
                }

                this.temProduto = false; // Reseta a flag após processar o produto
                notifyAll(); // Notifica outras threads para continuar
            }
        }
    }
    // Atribui um produto e sinaliza a thread para começar a trabalhar.
    @Override
    public synchronized void setProduto(Produto produto) {
        this.produto = produto;
        this.temProduto = true;
        notify(); // Notifica a thread para sair do estado de espera e realizar o cálculo lucro.
    }

    //GETS AND SETS:
    @Override
    public String getIdThread() {
        return idThread;
    }

    public RepositorioOperacao getRepositorioOperacoes() {
        return repositorioOperacoes;
    }

    public Produto getProduto() {
        return produto;
    }

    @Override
    public boolean isTemProduto() {
        return temProduto;
    }
}