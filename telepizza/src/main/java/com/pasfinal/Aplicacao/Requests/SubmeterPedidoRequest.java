package com.pasfinal.Aplicacao.Requests;

import java.util.List;

public class SubmeterPedidoRequest {
    private long id;
    private List<ItemPedidoRequest> itens;

    public SubmeterPedidoRequest() {
    }

    public SubmeterPedidoRequest(long id, List<ItemPedidoRequest> itens) {
        this.id = id;
        this.itens = itens;
    }

    public long getId() {
        return id;
    }

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
}
