import Beans.Estoque;
import Beans.Produto;

import java.util.List;
import java.util.Random;

public class SimuladorOperacoesEstoque {

    public static void main(String[] args) {
        Estoque estoque = Estoque.getInstancia();
        List<Produto> produtos = estoque.getProdutos();

        Random random = new Random();

        for (Produto produto : produtos) {
            int totalOperacoes = 100;  // Gera de 10 a 30 operações

            int numeroReposicoes = random.nextInt(totalOperacoes + 1);  // Aleatório de 0 até totalOperacoes
            int numeroRetiradas = totalOperacoes - numeroReposicoes;    // Garantir que a soma seja totalOperacoes

            System.out.println("Produto: " + produto.getNome());
            System.out.println("Total de Operações: " + totalOperacoes + " (Reposições: " + numeroReposicoes + ", Retiradas: " + numeroRetiradas + ")");

            for (int i = 0; i < numeroReposicoes; i++) {
                int quantidade = random.nextInt(50) + 1;  // Quantidade aleatória entre 1 e 50
                double precoUnitario = random.nextDouble() * 100;  // Preço aleatório entre 0 e 100
                System.out.println("Reposição - Quantidade: " + quantidade);
                estoque.repor(produto, quantidade, precoUnitario);
            }

            for (int i = 0; i < numeroRetiradas; i++) {
                int quantidade = random.nextInt(50) + 1;  // Quantidade aleatória entre 1 e 50
                double precoUnitario = random.nextDouble() * 100;  // Preço aleatório entre 0 e 100
                System.out.println("Retirada - Quantidade: " + quantidade);
                estoque.retirar(produto, quantidade, precoUnitario);
            }
        }
    }
}
