package com.pasfinal.Dominio.Servicos;

import java.util.List;

public class EstoqueInsuficienteException extends RuntimeException {
    private final List<Long> idsIndisponiveis;

    public EstoqueInsuficienteException(List<Long> idsIndisponiveis) {
        super("Estoque insuficiente para alguns itens do pedido.");
        this.idsIndisponiveis = idsIndisponiveis;
    }

    public List<Long> getIdsIndisponiveis() {
        return idsIndisponiveis;
    }
}
