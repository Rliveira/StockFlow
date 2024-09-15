package Beans;

import Repositorios.RepositorioOperacao;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Estoque implements Serializable{
    private static final long serialVersionUID = 3L;
    private static Estoque instanciaUnica;
    private List<Produto> produtos;
    private RepositorioOperacao repositorioOperacao;

    // CONSTRUTOR:
    private Estoque() {
        this.produtos = new ArrayList<>();
        this.repositorioOperacao = RepositorioOperacao.getInstancia();
        lerArquivo();
    }

    //SINGLETON:
    public static Estoque getInstancia()
    {
        if (instanciaUnica == null) {
            instanciaUnica = new Estoque();
        }
        return instanciaUnica;
    }

    public void adicionarProduto(Produto produto) {
        boolean contemNomesIguais = verificarProdutosComNomesIguais(produto.getNome());

        if(!contemNomesIguais){
            produtos.add(produto);
            salvarParaArquivo();
        }
        else{
            System.out.println("Produto já cadastrado no sistema.");
        }
    }

    public void removerProduto(Produto produto){
        Produto produto1 = buscarProduto(produto.getId());

        if(produto1 != null){
            produtos.remove(produto1);
            salvarParaArquivo();
        }
        else{
            System.out.println("Produto não cadastrado no sistema.");
        }
    }

    public void editarProduto(Produto produto, String novoNome){
        Produto produto1 = buscarProduto(produto.getId());

        if(produto1 != null){
            produto1.setNome(novoNome);
            produto1.setPrecoCompra(novoPrecoCompra);
            produto1.setPrecoVenda(novoPrecoVenda);
            salvarParaArquivo();
            System.out.println("Produto editado com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void repor(Produto produto, int quantidadeReposta, double precoUnitario) {

        if (produto != null) {
            int novaQuantidade = produto.getQuantidade() + quantidadeReposta;
            produto.setQuantidade(novaQuantidade);

            Operacao operacao = new Operacao("Reposição", produto.getId(), quantidadeReposta,
                                             LocalDateTime.now(), precoUnitario);
            repositorioOperacao.adicionarOperacao(operacao);
            salvarParaArquivo();

        } else {
            System.out.println("Produto não encontrado no estoque.");
        }
    }

    public void retirar(Produto produto, int quantidadeRetirada, double precoUnitario) {

        if (produto != null) {
            if (produto.getQuantidade() >= quantidadeRetirada) {
                int novaQuantidade = produto.getQuantidade() - quantidadeRetirada;
                produto.setQuantidade(novaQuantidade);

                Operacao operacao = new Operacao("Retirada", produto.getId(), quantidadeRetirada,
                        LocalDateTime.now(), precoUnitario);
                repositorioOperacao.adicionarOperacao(operacao);
                salvarParaArquivo();
            } else {
                System.out.println("Estoque insuficiente para " + produto.getNome());
            }
        } else {
            System.out.println("Produto não encontrado no estoque.");
        }
    }

    public Produto buscarProduto(UUID id) {
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

    public boolean verificarProdutosComNomesIguais(String nome){
        boolean contemNomeIgual = false;

        for(int i = 0; i < produtos.size() && !contemNomeIgual; i++){
            if(produtos.get(i).getNome().equals(nome)){
                contemNomeIgual = true;
            }
        }

        return contemNomeIgual;
    }

    public void salvarParaArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("estoque.txt"))) {
            for (Produto produto : produtos) {
                writer.write(produto.getId() + "," + produto.getNome() + "," + produto.getQuantidade());
                writer.newLine();
            }
            System.out.println("Estoque salvo com sucesso em estoque.txt");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o estoque: " + e.getMessage());
        }
    }

    public void lerArquivo() {
        produtos.clear(); // Limpa o estoque atual antes de carregar do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader("estoque.txt"))) {
            String linha;

            //Loop lê linha por linha de estoque.txt, pegando os atributos salvos,
            //convertendo de string para os tipos correspondentes e cria o objeto produto.
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                UUID id = UUID.fromString(dados[0]);
                String nome = dados[1];
                int quantidade = Integer.parseInt(dados[2]);

                Produto produto = new Produto(nome);
                produto.setQuantidade(quantidade);
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
