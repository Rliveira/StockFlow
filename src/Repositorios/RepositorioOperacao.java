package Repositorios;

import Beans.Operacao;
import Beans.Produto;
import Exceptions.OperacoesInsuficientesException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RepositorioOperacao implements Serializable
{
    private static final long serialVersionUID = 4L;
    private static RepositorioOperacao instanciaUnica;
    private final List<Operacao> operacoes;

    //CONSTRUTOR
    private RepositorioOperacao() {
        this.operacoes = new ArrayList<>();
        lerArquivo();
    }

    //SINGLETON
    public static RepositorioOperacao getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new RepositorioOperacao();
        }
        return instanciaUnica;
    }

    public void adicionarOperacao(Operacao operacao)
    {
        operacoes.add(operacao);
        System.out.println("Operação adicionada com sucesso.");
        salvarOperacoesParaArquivo();
    }

    public void  atualizarOperacao(UUID idOperacao, int novaQuantidade, String novoTipoOperacqo) {
        Operacao operacao = buscarPorId(idOperacao);

        if (operacao != null) {
            operacao.setQuantidade(novaQuantidade);
            operacao.setTipoOperacao(novoTipoOperacqo);
            System.out.println("Operação atualizada com sucesso!");
            salvarOperacoesParaArquivo();
        }
        else {
            System.out.println("Operacão não encontrada.");
        }
    }

    private Operacao buscarPorId(UUID idOperacao) {
        boolean achou = false;
        Operacao operacao = null;

        for(int i = 0; i < operacoes.size() && !achou; i++) {
            if(operacoes.get(i).getIdOperacao().equals(idOperacao)) {
                achou = true;
                operacao = operacoes.get(i);
            }
        }
        if(!achou){
            System.out.println("Operação não encontrada.");
        }

        return operacao;
    }

    public List<Operacao> listarOperacoesPorTipo(String tipoOperacao) {
        return operacoes.stream()
                .filter(operacao -> operacao.getTipoOperacao().equalsIgnoreCase(tipoOperacao))
                .collect(Collectors.toList());
    }

    public List<Operacao> listarOperacoesPorProduto(UUID idProduto) {
        return operacoes.stream()
                .filter(operacao -> operacao.getIdProduto().equals(idProduto))
                .collect(Collectors.toList());
    }

    public List<Operacao> listarOperacoes() {
        return new ArrayList<>(operacoes);
    }

    public synchronized double calcularDemandaPrevista(Produto produto, String tipoOperacao) {
        double demandaPrevista = 0;

        // Filtra as operações do produto com base no tipo de operação
        List<Operacao> operacoesDoProduto = listarOperacoesPorProduto(produto.getId());
        List<Operacao> operacoesFiltradas = operacoesDoProduto.stream()
                .filter(operacao -> operacao.getTipoOperacao().equalsIgnoreCase(tipoOperacao))
                .toList();

        // Verifica se há histórico suficiente
        if (operacoesFiltradas.size() < 2) {
            System.out.println("Histórico insuficiente para o produto: " + produto.getNome());
        }
        else{
            // Calcula a soma das quantidades
            double somaQuantidades = operacoesFiltradas.stream()
                    .mapToInt(Operacao::getQuantidade)
                    .sum();
            double mediaQuantidades = somaQuantidades / operacoesFiltradas.size();

            // Calcula a taxa de crescimento
            double somaTaxaCrescimento = 0;
            for (int i = 1; i < operacoesFiltradas.size(); i++) {
                int quantidadeAtual = operacoesFiltradas.get(i).getQuantidade();
                int quantidadeAnterior = operacoesFiltradas.get(i - 1).getQuantidade();

                if (quantidadeAnterior != 0) {
                    double taxaCrescimento = (double) (quantidadeAtual - quantidadeAnterior) / quantidadeAnterior;
                    somaTaxaCrescimento += taxaCrescimento;
                }
            }

            double taxaCrescimentoMedia = somaTaxaCrescimento / (operacoesFiltradas.size() - 1);

            // Retorna a demanda prevista
            demandaPrevista = mediaQuantidades * (1 + taxaCrescimentoMedia);
        }
        return demandaPrevista;
    }

    public synchronized double calcularLucroPorProduto(Produto produto) throws OperacoesInsuficientesException {
        double lucroTotal;
        double gastoTotalCompras = 0;
        double gastoTotalVendas = 0;

        List<Operacao> operacoesProduto = listarOperacoesPorProduto(produto.getId());

        // Variáveis para verificar se as operações necessárias
        // estão presentes para o cálculo de lucro
        boolean temCompra = false;
        boolean temVenda = false;

        for (Operacao operacao : operacoesProduto) {
            if (operacao.getTipoOperacao().equals("Reposição")) {
                temCompra = true;
                gastoTotalCompras += operacao.getValorUnitario() * operacao.getQuantidade();
            }

            else if (operacao.getTipoOperacao().equals("Retirada")) {
                temVenda = true;
                gastoTotalVendas += operacao.getValorUnitario() * operacao.getQuantidade();
            }
        }

        // Verifica se há pelo menos uma operação de reposição e uma operação de retirada.
        if (!temCompra || !temVenda) {
            throw new OperacoesInsuficientesException("Não é possível calcular o lucro de " + produto.getNome() +
                    " pois para o cálculo do lucro o produto deve ter ao menos uma operação de reposição e uma de retirada.");
        }
        else{
            lucroTotal = gastoTotalVendas - gastoTotalCompras;
        }

        return lucroTotal;
    }

    public void salvarOperacoesParaArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("operacoes.txt"))) {
            for (Operacao operacao : operacoes) {
                writer.write(operacao.getIdOperacao() + "," + operacao.getTipoOperacao() + "," +
                        operacao.getIdProduto() + "," + operacao.getQuantidade() + "," + operacao.getDataOperacao() +
                        "," + operacao.getValorUnitario());
                writer.newLine();
            }
            System.out.println("Operações salvas com sucesso em operacoes.txt");
        } catch (IOException e) {
            System.out.println("Erro ao salvar as operações: " + e.getMessage());
        }
    }

    public void lerArquivo() {
        operacoes.clear(); // Limpa o estoque atual antes de carregar do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader("operacoes.txt"))) {
            String linha;

            //Loop lê linha por linha de operacoes.txt, pegando os atributos salvos,
            //convertendo de string para os tipos correspondentes e cria o objeto produto.
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                UUID id = UUID.fromString(dados[0]);
                String tipoOperacao = dados[1];
                UUID idProduto = UUID.fromString(dados[2]);
                int quantidade = Integer.parseInt(dados[3]);
                LocalDateTime dataOperacao = LocalDateTime.parse(dados[4]);
                double valor = Double.parseDouble(dados[5]);

                Operacao operacao = new Operacao(tipoOperacao, idProduto, quantidade, dataOperacao, valor);
                operacao.setIdOperacao(id);

                operacoes.add(operacao);
            }
            System.out.println("Estoque carregado com sucesso de operacoes.txt");
        } catch (IOException e) {
            System.out.println("Erro ao ler o estoque: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro nos dados do arquivo: " + e.getMessage());
        }
    }

}
