package com.pasfinal.Aplicacao.Responses;

/**
 * DTO para retorno do status de um pedido em formato JSON.
 */
public class StatusPedidoResponse {
    private long id;
    private String status;
    private String descricao;

    public StatusPedidoResponse(long id, String status, String descricao) {
        this.id = id;
        this.status = status;
        this.descricao = descricao;
    }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusPedidoResponse naoEncontrado(long id){
        return new StatusPedidoResponse(id, "NAO_ENCONTRADO", "Pedido não encontrado");
    }
}
