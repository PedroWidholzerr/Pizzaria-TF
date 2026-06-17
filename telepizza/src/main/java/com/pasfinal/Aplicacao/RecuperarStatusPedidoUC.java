package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Pedido;

@Component
public class RecuperarStatusPedidoUC {
    private PedidoRepository pedidoRepository;

    public RecuperarStatusPedidoUC(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido.Status run(long idPedido){
        Pedido pedido = pedidoRepository.recuperaPorId(idPedido);
        if(pedido==null) return null;
        return pedido.getStatus();
    }
}
