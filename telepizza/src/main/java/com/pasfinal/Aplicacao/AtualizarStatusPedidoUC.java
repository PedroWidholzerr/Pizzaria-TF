package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Entidades.Pedido;
import com.pasfinal.Dominio.Servicos.PedidoService;

@Component
public class AtualizarStatusPedidoUC {

    private final PedidoService pedidoService;

    public AtualizarStatusPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public boolean run(long idPedido, Pedido.Status novoStatus) {
        try {
            pedidoService.atualizarStatus(idPedido, novoStatus);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
