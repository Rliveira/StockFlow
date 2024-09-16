package Threads;

import Beans.Produto;

public interface ThreadProcessamento {
    String getIdThread();
    void setProduto(Produto produto);
    boolean isTemProduto();
}