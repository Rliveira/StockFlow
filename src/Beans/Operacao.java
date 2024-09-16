package Beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Operacao implements Serializable
{
    private static final long serialVersionUID = 2L;
    private UUID idOperacao;
    private String tipoOperacao; //reposição ou retirada
    private UUID idProduto;
    private int quantidade;
    private LocalDateTime dataOperacao;
    private double valorUnitario;

    //CONSTRUTOR:
    public Operacao(String tipoOperacao, UUID idProduto, int quantidade, LocalDateTime dataOperacao,
                    double valor) {
        this.idOperacao = UUID.randomUUID();
        this.tipoOperacao = tipoOperacao;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.dataOperacao = dataOperacao;
        this.valorUnitario = valor;
    }

    //GETS AND SETS:
    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public UUID getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(UUID idOperacao) {
        this.idOperacao = idOperacao;
    }

   public String getTipoOperacao()
   {
       return tipoOperacao;
   }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public UUID getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(UUID idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(LocalDateTime dataOperacao) {
        this.dataOperacao = dataOperacao;
    }


    public void imprimirOperacoes(List<Operacao> operacoes) {
        System.out.println("+--------------------------------------+------------+------------+");
        System.out.println("| ID da Operação                       | Tipo       | Valor Total|");
        System.out.println("+--------------------------------------+------------+------------+");

        for (Operacao operacao : operacoes) {
            System.out.println(operacao.toString());
        }

        System.out.println("+--------------------------------------+------------+------------+");
    }
    @Override
    public String toString() {
        double valorTotal = quantidade * valorUnitario;
        return String.format("| %-36s | %-10s | %-10.2f |",
                idOperacao.toString(),
                tipoOperacao,
                valorTotal);
    }
}
