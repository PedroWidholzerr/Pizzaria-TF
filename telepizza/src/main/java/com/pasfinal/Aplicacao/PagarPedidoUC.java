package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {

    private final PedidoService pedidoService;

    public PagarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public boolean run(long idPedido) {
        try {
            pedidoService.pagarPedido(idPedido);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
