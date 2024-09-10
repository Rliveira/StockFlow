import Beans.Estoque;
import Beans.Produto;
import Threads.ThreadRepor;
import Threads.ThreadRetirar;

import java.util.List;

public class Main {

    // Constantes para configuração
    private static final int NUMERO_THREADS = 2;
    private static final int QUANTIDADE_INICIAL = 100;
    private static final int QUANTIDADE_REPOSICAO = 10;
    private static final int QUANTIDADE_VENDA = 20;

    public static void main(String[] args) {
        Estoque estoque = new Estoque();

        // Instancia os produtos e adiciona ao estoque
        Produto produto1 = new Produto("Caneta", QUANTIDADE_INICIAL);
        Produto produto2 = new Produto("Lápis", QUANTIDADE_INICIAL);
        Produto produto3 = new Produto("borracha", QUANTIDADE_INICIAL);

        estoque.adicionarProduto(produto1);
        estoque.adicionarProduto(produto2);
        estoque.adicionarProduto(produto3);

        // Criação de threads de compra e venda
        List<Produto> produtos = estoque.getProdutos();

        //TODO: CORRIGIR E MELHORAR ISSO

        for(int i = 0; i < produtos.size(); i++){
            for (int j = 0; j < NUMERO_THREADS; j++) {
                new ThreadRepor(i + 1 + "TC", estoque, produtos.get(i), QUANTIDADE_REPOSICAO).start();
                new ThreadRetirar(i + 1 + "TV", estoque, produtos.get(i), QUANTIDADE_VENDA).start();
            }
        }
    }
}