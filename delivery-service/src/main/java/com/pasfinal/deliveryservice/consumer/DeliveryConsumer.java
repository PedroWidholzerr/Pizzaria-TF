package com.pasfinal.deliveryservice.consumer;

import com.pasfinal.deliveryservice.dto.DeliveryRequest;
import com.pasfinal.deliveryservice.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeliveryConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(DeliveryConsumer.class);
    
    @Autowired
    private DeliveryService deliveryService;
    
    @RabbitListener(queues = "#{@getQueueName}")
    public void receiveDeliveryRequest(DeliveryRequest request) {
        logger.info("╔════════════════════════════════════════╗");
        logger.info("║  MENSAGEM RECEBIDA DA FILA RABBITMQ   ║");
        logger.info("╚════════════════════════════════════════╝");
        logger.info("DeliveryRequest recebido: {}", request);
        
        try {
            deliveryService.processarEntrega(request);
            logger.info("Entrega processada com sucesso para o pedido: {}", request.getPedidoId());
        } catch (Exception e) {
            logger.error("Erro ao processar entrega para o pedido {}: {}", 
                        request.getPedidoId(), e.getMessage(), e);
        }
    }
}
