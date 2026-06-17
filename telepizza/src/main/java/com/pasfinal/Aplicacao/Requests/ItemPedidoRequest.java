package com.pasfinal.Aplicacao.Requests;

public class ItemPedidoRequest {
    private long produtoId;
    private int quantidade;

    public ItemPedidoRequest() {
    }

    public ItemPedidoRequest(long produtoId, int quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public long getProdutoId() {
        return produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setProdutoId(long produtoId) {
        this.produtoId = produtoId;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}