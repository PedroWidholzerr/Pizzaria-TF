package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Pedido;

@Component
public class AtualizarStatusPedidoUC {
    private PedidoRepository pedidoRepository;

    public AtualizarStatusPedidoUC(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public boolean run(long idPedido, Pedido.Status novoStatus){
        Pedido pedido = pedidoRepository.recuperaPorId(idPedido);
        if(pedido == null) {
            return false;
        }
        pedido.setStatus(novoStatus);
        pedidoRepository.salva(pedido);
        return true;
    }
}
