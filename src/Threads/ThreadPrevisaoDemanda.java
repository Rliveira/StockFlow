package Threads;

import Beans.Estoque;
import Beans.Produto;

import java.util.List;
import java.util.Map;

public class ThreadPrevisaoDemanda extends Thread
{
    private final Estoque estoque;

    public ThreadPrevisaoDemanda(Estoque estoque)
    {
        this.estoque = estoque;
    }

    @Override
    public void run()
    {
        System.out.println("=== Previsão de Demanda Iniciada ===");

        for(Produto produto : estoque.getProdutos())
        {
            double demandaPrevista = calcularDemandaPrevista(produto);
            System.out.println("Demanda prevista para o produto: " + produto.getNome() + ": "+ demandaPrevista + " unidades.");
        }

        System.out.println("=== Previsão de Demanda Concluída");
    }

    private double calcularDemandaPrevista(Produto produto) {
        Map<String, List<Integer>> historicoVendas = estoque.listarVendas();
        List<Integer> vendasProduto = historicoVendas.get(produto.getNome());

        if (vendasProduto == null || vendasProduto.size() < 2) {
            System.out.println("Histórico insuficiente para o produto: " + produto.getNome());
            return 0;
        }

        double somaVendas = vendasProduto.stream().mapToInt(Integer::intValue).sum();
        double mediaVendas = somaVendas / vendasProduto.size();

        double somaTaxaCrescimento = 0;
        for (int i = 1; i < vendasProduto.size(); i++) {
            int vendasDiaAtual = vendasProduto.get(i);
            int vendasDiaAnterior = vendasProduto.get(i - 1);

            if (vendasDiaAnterior != 0) {
                double taxaCrescimento = (double) (vendasDiaAtual - vendasDiaAnterior) / vendasDiaAnterior;
                somaTaxaCrescimento += taxaCrescimento;
            }
        }

        double taxaCrescimentoMedia = somaTaxaCrescimento / (vendasProduto.size() - 1);
        return mediaVendas * (1 + taxaCrescimentoMedia);
    }
}
