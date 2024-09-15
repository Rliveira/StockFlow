package Beans;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Override
    public String toString() {
        return "Operacao{" +
                "idOperacao=" + idOperacao +
                ", tipoOperacao=" + tipoOperacao +
                ", idProduto=" + idProduto +
                ", quantidade=" + quantidade +
                ", dataOperacao=" + dataOperacao +
                ", valorUnitario = " + valorUnitario +
                '}';
    }
}
