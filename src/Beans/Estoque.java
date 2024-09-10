package Beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Estoque {
    private final List<Produto> produtos;

    // CONSTRUTOR:
    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    // Método para adicionar um produto ao estoque
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    // Método para repor o estoque de um produto
    public synchronized void repor(UUID id, int quantidadeReposta, String idThread) {
        boolean achou = false;

        for (int i = 0; i < produtos.size() && !achou; i++) {
            if (produtos.get(i).getId().equals(id)) {
                Produto produto = produtos.get(i);
                achou = true;

                int novaQuantidade = produto.getQuantidade() + quantidadeReposta;
                produto.setQuantidade(novaQuantidade);
                System.out.println("Reposto: " + quantidadeReposta + " unidades de " + produto.getNome()
                        + ".\n Beans.Estoque atual: " + novaQuantidade + "\n Executado pela thread: " + idThread + '\n');
            }
        }
        if(!achou){
            System.out.println("Beans.Produto não encontrado no estoque.");
        }
    }

    // Método para vender um produto
    public synchronized void vender(UUID id, int quantidadeVendida, String idThread) {
        boolean achou = false;

        for (int i = 0; i < produtos.size() && !achou; i++) {
            if (produtos.get(i).getId().equals(id)) {
                Produto produto = produtos.get(i);
                achou = true;

                if (produto.getQuantidade() >= quantidadeVendida) {
                    int novaQuantidade = produto.getQuantidade() - quantidadeVendida;
                    produto.setQuantidade(novaQuantidade);
                    System.out.println("Vendido: " + quantidadeVendida + " unidades de " + produto.getNome()
                            + ".\n Beans.Estoque atual: " + novaQuantidade + "\n Executado pela thread: " + idThread + '\n');
                } else {
                    System.out.println("Beans.Estoque insuficiente para " + produto.getNome());
                }
            }
        }
        if(!achou){
            System.out.println("Beans.Produto não encontrado no estoque.");
        }    }

    //GETS AND SETS
    public List<Produto> getProdutos() {
        return produtos;
    }
}
