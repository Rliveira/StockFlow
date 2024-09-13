package Beans;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Estoque {

    private static Estoque instanciaUnica;
    private final List<Produto> produtos;
    private final List<Operacao> operacoes;

    //TODO: fazer o CRUD dos produtos no estoque.

    // CONSTRUTOR:
    public Estoque() {
        this.produtos = new ArrayList<>();
        this.operacoes = new ArrayList<>();
    }

    public static synchronized Estoque getInstancia()
    {
        if (instanciaUnica == null) {
            instanciaUnica = new Estoque();
        }
        return instanciaUnica;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        salvarParaArquivo();
    }

    public void removerProduto(Produto produto){
        Produto produto1 = encontrarProduto(produto.getId());

        if(produto1 != null){
            produtos.remove(produto1);
            salvarParaArquivo();
        }
    }

    public void editarProduto(Produto produto, String novoNome, double novoPrecoCompra, Double novoPrecoVenda){
        Produto produto1 = encontrarProduto(produto.getId());

        if(produto1 != null){
            produto1.setNome(novoNome);
            produto1.setPrecoCompra(novoPrecoCompra);
            produto1.setPrecoVenda(novoPrecoVenda);
            salvarParaArquivo();
        }
    }

    public void repor(UUID id, int quantidadeReposta, String idThread) {
        Produto produto = encontrarProduto(id);

        if (produto != null) {
            int novaQuantidade = produto.getQuantidade() + quantidadeReposta;
            produto.setQuantidade(novaQuantidade);
            System.out.println("Reposto: " + quantidadeReposta + " unidades de " + produto.getNome()
                    + ".\nEstoque atual: " + novaQuantidade + "\nExecutado pela thread: " + idThread + '\n');

            registrarOperacao("Reposição", produto, quantidadeReposta, idThread, produto.getPrecoCompra());

            salvarParaArquivo();

        } else {
            System.out.println("Produto não encontrado no estoque.");
        }
    }

    public void retirar(UUID id, int quantidadeVendida, String idThread) {
        Produto produto = encontrarProduto(id);

        if (produto != null) {
            if (produto.getQuantidade() >= quantidadeVendida) {
                int novaQuantidade = produto.getQuantidade() - quantidadeVendida;
                produto.setQuantidade(novaQuantidade);
                System.out.println("Vendido: " + quantidadeVendida + " unidades de " + produto.getNome()
                        + ".\nEstoque atual: " + novaQuantidade + "\nExecutado pela thread: " + idThread + '\n');

                registrarOperacao("Venda", produto, quantidadeVendida, idThread, produto.getPrecoVenda());
                salvarParaArquivo();
            } else {
                System.out.println("Estoque insuficiente para " + produto.getNome());
            }
        } else {
            System.out.println("Produto não encontrado no estoque.");
        }
    }

    public Produto encontrarProduto(UUID id) {
        boolean achou = false;
        Produto produtoEncontrado = null;

        for (int i = 0; i < produtos.size() && !achou; i++) {
            if (produtos.get(i).getId().equals(id)) {
                produtoEncontrado = produtos.get(i);
                achou = true;
            }
        }
        return produtoEncontrado;
    }

    public Map<String, List<Integer>> listarVendas() {
        return operacoes.stream()
                .filter(operacao -> operacao.getTipoOperacao().equalsIgnoreCase("Venda"))
                .collect(Collectors.groupingBy(
                        operacao -> encontrarProduto(operacao.getIdProduto()).getNome(),   // chave: nome do produto
                        Collectors.mapping(Operacao::getQuantidade, Collectors.toList())   // valor: lista de quantidades vendidas
                ));
    }

    public Map<String, List<Integer>> listarReposicoes() {
        return operacoes.stream()
                .filter(operacao -> operacao.getTipoOperacao().equalsIgnoreCase("Reposição"))
                .collect(Collectors.groupingBy(
                        operacao -> encontrarProduto(operacao.getIdProduto()).getNome(),   // chave: nome do produto
                        Collectors.mapping(Operacao::getQuantidade, Collectors.toList())   // valor: lista de quantidades repostas
                ));
    }

    private void registrarOperacao(String tipo, Produto produto, int quantidade, String idThread, double precoUnitario) {
        Operacao operacao = new Operacao(

                UUID.randomUUID(),
                tipo,
                produto.getId(),
                quantidade,
                LocalDateTime.now(),
                produto.getPrecoVenda() * quantidade
        );
        operacoes.add(operacao);
        salvarOperacoesParaArquivo();
    }

    public void salvarOperacoesParaArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("operacoes.txt"))) {
            for (Operacao operacao : operacoes) {
                writer.write(operacao.toString());
                writer.newLine();
            }
            System.out.println("Operações salvas com sucesso em operacoes.txt");
        } catch (IOException e) {
            System.out.println("Erro ao salvar as operações: " + e.getMessage());
        }
    }

    public void salvarParaArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("estoque.txt"))) {
            for (Produto produto : produtos) {
                writer.write(produto.getId() + "," + produto.getNome() + "," + produto.getPrecoCompra() + ","
                        + produto.getPrecoVenda() + "," + produto.getQuantidade());
                writer.newLine();
            }
            System.out.println("Estoque salvo com sucesso em estoque.txt");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o estoque: " + e.getMessage());
        }
    }

    // Método para ler o estoque de um arquivo
    public void lerDeArquivo() {
        produtos.clear(); // Limpa o estoque atual antes de carregar do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader("estoque.txt"))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                UUID id = UUID.fromString(dados[0]);
                String nome = dados[1];
                double precoCompra = Double.parseDouble(dados[2]);
                double precoVenda = Double.parseDouble(dados[3]);
                int quantidade = Integer.parseInt(dados[4]);

                Produto produto = new Produto(nome, quantidade, precoCompra, precoVenda);
                produto.setId(id);
                produtos.add(produto);
            }
            System.out.println("Estoque carregado com sucesso de estoque.txt");
        } catch (IOException e) {
            System.out.println("Erro ao ler o estoque: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro nos dados do arquivo: " + e.getMessage());
        }
    }

    //GETS AND SETS
    public List<Produto> getProdutos() {
        return produtos;
    }

}
