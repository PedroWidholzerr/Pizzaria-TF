package com.pasfinal.Aplicacao;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Pedido;

/**
 * Caso de uso: Cliente solicita o cancelamento de um pedido aprovado, mas não pago.
 * Regra: Só pode cancelar se status atual == APROVADO. (Assumindo que 'PAGO' já não pode).
 * Retorna true se cancelou, false se não foi possível (pedido inexistente ou status inválido).
 */
@Component
public class CancelarPedidoUC {
    private final PedidoRepository pedidoRepository;

    public CancelarPedidoUC(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public boolean run(long idPedido){
        Pedido pedido = pedidoRepository.recuperaPorId(idPedido);
        if(pedido == null) return false;
        if(pedido.getStatus() == Pedido.Status.APROVADO){
            pedido.setStatus(Pedido.Status.CANCELADO);
            pedidoRepository.salva(pedido);
            return true;
        }
        return false;
    }
}
