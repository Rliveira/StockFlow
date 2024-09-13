package Threads;

import Beans.Estoque;
import Beans.Operacao;
import Beans.Produto;
import Repositorios.RepositorioOperacao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ThreadCalculoLucro extends Thread {
    private Estoque estoque;
    private RepositorioOperacao repositorioOperacoes;

    // Construtor
    public ThreadCalculoLucro() {
        this.estoque = Estoque.getInstancia();
        this.repositorioOperacoes = RepositorioOperacao.getInstancia();
    }

    @Override
    public void run() {
        System.out.println("=== Cálculo de Lucro Iniciado ===");

        double lucroTotal = calcularLucro();
        System.out.println("Lucro total gerado pelas vendas: R$ " + lucroTotal);

        System.out.println("=== Cálculo de Lucro Concluído ===");
    }

    public double calcularLucro() {
        double lucroTotal = 0;

        for (Operacao operacao : repositorioOperacoes.listarOperacoes())
        {
            if (operacao.getTipoOperacao().equalsIgnoreCase("Venda"))
            {
                double precoVenda = operacao.getValor();

                double precoCompra = operacao.getQuantidade() * getPrecoCompraPorProduto(operacao.getIdProduto());
                lucroTotal += (precoVenda - precoCompra);
            }
        }
        return lucroTotal;
    }

    private double getPrecoCompraPorProduto(UUID produtoId) {

        Produto produto = Estoque.getInstancia().encontrarProduto(produtoId);
        return produto != null ? produto.getPrecoCompra() : 0;
    }
}
