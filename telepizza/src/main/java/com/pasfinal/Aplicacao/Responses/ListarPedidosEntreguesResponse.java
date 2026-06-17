package com.pasfinal.Aplicacao.Responses;

import java.util.List;

public class ListarPedidosEntreguesResponse {
    private List<PedidoResumoResponse> pedidos;
    private int total;

    public ListarPedidosEntreguesResponse() {
    }

    public ListarPedidosEntreguesResponse(List<PedidoResumoResponse> pedidos) {
        this.pedidos = pedidos;
        this.total = pedidos != null ? pedidos.size() : 0;
    }

    public List<PedidoResumoResponse> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoResumoResponse> pedidos) {
        this.pedidos = pedidos;
        this.total = pedidos != null ? pedidos.size() : 0;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
