package Threads;

import Beans.Estoque;
import Beans.Produto;

public class ThreadRetirar extends Thread {
    private String idThread;
    private final Estoque estoque;
    private final Produto produto;
    private final int quantidadeVendida;

    public ThreadRetirar(String idThread, Estoque estoque, Produto produto, int quantidadeVendida) {
        this.idThread = idThread;
        this.estoque = estoque;
        this.produto = produto;
        this.quantidadeVendida = quantidadeVendida;
    }

    @Override
    public void run() {
        estoque.retirar(produto.getId(), quantidadeVendida, getIdThread());
    }

    //GETs AND SETs
    public String getIdThread() {
        return idThread;
    }
}