package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Entidades.Pedido;
import com.pasfinal.Dominio.Servicos.PedidoService;

@Component
public class RecuperarStatusPedidoUC {

    private final PedidoService pedidoService;

    public RecuperarStatusPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public Pedido.Status run(long idPedido) {
        Pedido pedido = pedidoService.consultarPedido(idPedido);
        if (pedido == null) return null;
        return pedido.getStatus();
    }
}
