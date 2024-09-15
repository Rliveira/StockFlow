package Threads;

import Beans.Produto;
import Exceptions.OperacoesInsuficientesException;
import Repositorios.RepositorioOperacao;

public class ThreadCalculoLucro extends Thread {
    private String idThread;
    private RepositorioOperacao repositorioOperacoes;
    private Produto produto;

    // CONSTRUTOR:
    public ThreadCalculoLucro(String idThread, RepositorioOperacao repositorioOperacao) {
        this.idThread = idThread;
        this.repositorioOperacoes = repositorioOperacao;
        this.produto = null;
    }

    //MÉTODOS:
    @Override
    public void run() {
        if (produto != null) {
            try {
                double lucro = repositorioOperacoes.calcularLucroPorProduto(produto);
                System.out.println("Lucro calculado para o produto " + produto.getNome() + ": " + lucro);
            } catch (OperacoesInsuficientesException e) {
                System.out.println(e.getMessage()); //imprime a mensagem de err caso a excessão seja levantada
            }
        } else {
            System.out.println("Produto não definido para a thread " + idThread);
        }
    }

    //GETS AND SETS:
    public String getIdThread() {
        return idThread;
    }

    public RepositorioOperacao getRepositorioOperacoes() {
        return repositorioOperacoes;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
