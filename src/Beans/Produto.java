package Beans;

import java.util.UUID;

public class Produto {
    private UUID id;
    private String nome;
    private int quantidade;
    private double precoCompra;
    private double precoVenda;

    //CONSTRUTOR:
    public Produto(String nome, int quantidadeInicial, double precoCompra, double precoVenda) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.quantidade = quantidadeInicial;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
    }

    //GETs AND SETs:
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }
}
