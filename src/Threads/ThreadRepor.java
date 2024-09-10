package Threads;

import Beans.Estoque;
import Beans.Produto;

public class ThreadRepor extends Thread {
    private String idThread;
    private final Estoque estoque;
    private final Produto produto;
    private final int quantidadeReposta;

    //CONSTRUTOR
    public ThreadRepor(String idThread, Estoque estoque, Produto produto, int quantidadeReposta) {
        this.idThread = idThread;
        this.estoque = estoque;
        this.produto = produto;
        this.quantidadeReposta = quantidadeReposta;
    }

    @Override
    public void run() {
        estoque.repor(produto.getId(), quantidadeReposta, getIdThread());
    }

    //GETs AND SETs
    public String getIdThread() {
        return idThread;
    }
}