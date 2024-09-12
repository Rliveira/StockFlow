package Beans;

import java.util.Date;
import java.util.UUID;

public class Operacao
{
    private UUID idOperacao;
    private String tipoOperacao;
    private UUID idProduto;
    private int quantidade;
    private Date dataOperacao;

    private double valor;

    public Operacao(UUID idOperacao, String tipoOperacao, UUID idProduto, int quantidade, Date dataOperacao, double valor) {
        this.idOperacao = idOperacao;
        this.tipoOperacao = tipoOperacao;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.dataOperacao = dataOperacao;
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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

    public Date getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(Date dataOperacao) {
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
                '}';
    }
}
