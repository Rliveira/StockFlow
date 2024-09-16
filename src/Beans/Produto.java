package Beans;

import java.io.Serializable;
import java.util.List;
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

    public void imprimirProdutos(List<Produto> produtos) {
        System.out.println("+--------------------------------------+----------------------+------------+");
        System.out.println("| ID do Produto                       | Nome do Produto      | Quantidade |");
        System.out.println("+--------------------------------------+----------------------+------------+");

        for (Produto produto : produtos) {
            System.out.println(produto.toString());
        }

        System.out.println("+--------------------------------------+----------------------+------------+");
    }
    @Override
    public String toString() {
        return String.format("| %-36s | %-20s | %-10d |",
                id.toString(),
                nome,
                quantidade);
    }
}
