package Repositorios;

import Beans.Operacao;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RepositorioOperacao
{
    private final List<Operacao> operacoes;

    public RepositorioOperacao(List<Operacao> operacoes) {
        this.operacoes = operacoes;
    }

    public void adicionarOperacao(Operacao operacao)
    {
        operacoes.add(operacao);
        System.out.println("Operacao adicionada: " + operacao);
    }

    public Operacao buscarPorId(UUID idOperacao)
    {
        for(Operacao operacao : operacoes)
        {
            if(operacao.getIdOperacao().equals(idOperacao))
            {
                return operacao;
            }
        }
        System.out.println("Operacção não encontrada com o ID: " + idOperacao);
        return null;
    }
    public boolean atualizarOperacao(UUID idOperacao, int novaQuantidade) {
        Operacao operacao = buscarPorId(idOperacao);
        if (operacao != null) {
            operacao.setQuantidade(novaQuantidade);
            System.out.println("Operação atualizada: " + operacao);
            return true;
        }
        System.out.println("Não foi possível atualizar. Operação não encontrada com o ID: " + idOperacao);
        return false;
    }

    public List<Operacao> listarPorTipo(String tipoOperacao) {
        return operacoes.stream()
                .filter(operacao -> operacao.getTipoOperacao().equalsIgnoreCase(tipoOperacao))
                .collect(Collectors.toList());
    }
    public List<Operacao> listarPorProduto(UUID idProduto) {
        return operacoes.stream()
                .filter(operacao -> operacao.getIdProduto().equals(idProduto))
                .collect(Collectors.toList());
    }
    public boolean removerOperacao(UUID idOperacao)
    {
        Operacao operacao = buscarPorId(idOperacao);
        if (operacao != null)
        {
            operacoes.remove(operacao);
            System.out.println("Operação removida: " + operacao);
            return true;
        }
        System.out.println("Não foi possível remover. Operação não encontrada com o ID: " + idOperacao);
        return false;
    }
    public int contarOperacoes() {
        return operacoes.size();
    }

}
