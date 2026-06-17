package com.pasfinal.Dominio.Servicos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pasfinal.Adaptadores.Servicos.DeliveryProducerService;
import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.ItemPedido;
import com.pasfinal.Dominio.Entidades.Pedido;

@Service
public class EntregaService {
    private PedidoRepository pedidoRepository;
    private DeliveryProducerService deliveryProducer;

    public EntregaService(PedidoRepository pedidoRepository, DeliveryProducerService deliveryProducer) {
        this.pedidoRepository = pedidoRepository;
        this.deliveryProducer = deliveryProducer;
    }

    public synchronized void receberPedidoPronto(Pedido p) {
        // Atualiza status localmente para PRONTO (já deve vir como PRONTO da cozinha, mas garante)
        // Na verdade a cozinha já setou PRONTO.
        // O próximo status TRANSPORTE será atualizado via callback do microsserviço.
        
        List<String> nomesItens = new ArrayList<>();
        for (ItemPedido item : p.getItens()) {
            nomesItens.add(item.getItem().getDescricao() + " (x" + item.getQuantidade() + ")");
        }
        
        deliveryProducer.enviarPedidoParaEntrega(
            p.getId(), 
            p.getCliente().getNome(), 
            p.getEnderecoEntrega(), 
            nomesItens, 
            p.getValorCobrado()
        );
    }
}
