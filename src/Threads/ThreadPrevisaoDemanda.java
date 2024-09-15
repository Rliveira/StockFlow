package Threads;

import Beans.Estoque;
import Beans.Operacao;
import Beans.Produto;
import Repositorios.RepositorioOperacao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ThreadPrevisaoDemanda extends Thread
{
    private String idThread;
    private Produto produto;
    private String tipoOperacao;
    private RepositorioOperacao repositorioOperacao;

    //CONSTRUTOR:
    public ThreadPrevisaoDemanda(String id, RepositorioOperacao repositorioOperacao)
    {
        //o produto e o tipo de operação são constantemente atualizados no código.
        this.idThread = id;
        this.produto = null;
        this.tipoOperacao = "";
        this.repositorioOperacao = repositorioOperacao;
    }

    //MÉTODOS:
    @Override
    public void run()
    {
        if(produto != null){
            double demandaPrevista = repositorioOperacao.calcularDemandaPrevista(produto, tipoOperacao);
            System.out.println("Demanda prevista para o produto: " + produto.getNome() + ": "+ demandaPrevista + " unidades.");
        } else {
            System.out.println("Produto não definido para a thread " + idThread);
        }

    }

    //GETs and SETs:

    public String getIdThread() {
        return idThread;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public RepositorioOperacao getRepositorioOperacao() {
        return repositorioOperacao;
    }

    public void setRepositorioOperacao(RepositorioOperacao repositorioOperacao) {
        this.repositorioOperacao = repositorioOperacao;
    }
}
