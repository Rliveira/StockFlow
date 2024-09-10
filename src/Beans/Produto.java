package Beans;

import java.util.UUID;

public class Produto {
    private UUID id;
    private String nome;
    private int quantidade;

    //CONSTRUTOR:
    public Produto(String nome, int quantidadeInicial) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.quantidade = quantidadeInicial;
    }

    //GETs AND SETs:
    public UUID getId() {
        return id;
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
}
