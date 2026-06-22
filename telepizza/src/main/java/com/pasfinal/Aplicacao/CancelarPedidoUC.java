package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Servicos.PedidoService;

@Component
public class CancelarPedidoUC {

    private final PedidoService pedidoService;

    public CancelarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public boolean run(long idPedido) {
        try {
            pedidoService.cancelarPedido(idPedido);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
