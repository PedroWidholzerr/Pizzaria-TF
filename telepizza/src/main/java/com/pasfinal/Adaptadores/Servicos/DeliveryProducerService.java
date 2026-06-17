package com.pasfinal.Adaptadores.Servicos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(DeliveryProducerService.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void enviarPedidoParaEntrega(Long pedidoId, String clienteNome, String enderecoEntrega, 
                                        List<String> itens, Double valorTotal) {
        DeliveryRequestDTO request = new DeliveryRequestDTO(
            pedidoId, 
            clienteNome, 
            enderecoEntrega, 
            itens, 
            valorTotal
        );
        
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQProducerConfig.DELIVERY_EXCHANGE,
                RabbitMQProducerConfig.DELIVERY_ROUTING_KEY,
                request
            );
            
            logger.info("╔════════════════════════════════════════╗");
            logger.info("║  PEDIDO ENVIADO PARA FILA DE ENTREGA  ║");
            logger.info("╚════════════════════════════════════════╝");
            logger.info("Pedido ID: {} enviado para a fila RabbitMQ", pedidoId);
            logger.info("Cliente: {}", clienteNome);
            logger.info("Endereço: {}", enderecoEntrega);
            logger.info("Exchange: {}", RabbitMQProducerConfig.DELIVERY_EXCHANGE);
            logger.info("Routing Key: {}", RabbitMQProducerConfig.DELIVERY_ROUTING_KEY);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar pedido {} para a fila: {}", pedidoId, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar pedido para fila de entrega", e);
        }
    }
}
