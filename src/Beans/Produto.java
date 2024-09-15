package Beans;

import java.io.Serializable;
import java.util.UUID;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String nome;
    private int quantidade;

    //CONSTRUTOR:
    public Produto(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.quantidade = 0;
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

    @Override
    public String toString() {
        return "Produto{" +
                "id =" + id +
                ", nome ='" + nome + '\'' +
                ", quantidade =" + quantidade +
                '}';
    }
}
