package com.pasfinal.deliveryservice.service;

import com.pasfinal.deliveryservice.client.AtualizarStatusRequest;
import com.pasfinal.deliveryservice.client.TelePizzaClient;
import com.pasfinal.deliveryservice.dto.DeliveryRequest;
import com.pasfinal.deliveryservice.model.Delivery;
import com.pasfinal.deliveryservice.model.Delivery.DeliveryStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class DeliveryService {
    
    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    private final Random random = new Random();
    private final TelePizzaClient telePizzaClient;
    
    @Value("${spring.application.name}")
    private String instanceName;
    
    private static final List<String> ENTREGADORES = Arrays.asList(
        "João Silva", "Maria Santos", "Pedro Oliveira", 
        "Ana Costa", "Carlos Souza", "Julia Lima"
    );

    public DeliveryService(TelePizzaClient telePizzaClient) {
        this.telePizzaClient = telePizzaClient;
    }
    
    public void processarEntrega(DeliveryRequest request) {
        logger.info("===========================================");
        logger.info("NOVA ENTREGA RECEBIDA - Instância: {}", instanceName);
        logger.info("Pedido ID: {}", request.getPedidoId());
        logger.info("Cliente: {}", request.getClienteNome());
        logger.info("Endereço: {}", request.getEnderecoEntrega());
        logger.info("Itens: {}", request.getItens());
        logger.info("Valor Total: R$ {}", request.getValorTotal());
        logger.info("===========================================");
        
        Delivery delivery = new Delivery(
            request.getPedidoId(), 
            request.getClienteNome(), 
            request.getEnderecoEntrega()
        );
        
        CompletableFuture.runAsync(() -> {
            try {
                simularEntrega(delivery);
            } catch (InterruptedException e) {
                logger.error("Erro durante simulação de entrega: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        });
    }
    
    private void simularEntrega(Delivery delivery) throws InterruptedException {
        String entregador = ENTREGADORES.get(random.nextInt(ENTREGADORES.size()));
        delivery.setEntregadorNome(entregador);
        
        delivery.setStatus(DeliveryStatus.EM_PREPARACAO);
        int tempoPreparacao = 2000 + random.nextInt(3000);
        logger.info("[Pedido {}] Status: EM_PREPARACAO - Entregador: {} - Aguardando {} ms", 
                   delivery.getPedidoId(), entregador, tempoPreparacao);
        Thread.sleep(tempoPreparacao);
        
        delivery.setStatus(DeliveryStatus.SAIU_PARA_ENTREGA);
        atualizarStatusRemoto(delivery.getPedidoId(), "TRANSPORTE");
        
        int tempoRota = 5000 + random.nextInt(5000);
        logger.info("[Pedido {}] Status: SAIU_PARA_ENTREGA - Tempo estimado: {} ms", 
                   delivery.getPedidoId(), tempoRota);
        Thread.sleep(tempoRota);
        
        if (random.nextDouble() < 0.95) {
            delivery.setStatus(DeliveryStatus.ENTREGUE);
            delivery.setFimEntrega(LocalDateTime.now());
            atualizarStatusRemoto(delivery.getPedidoId(), "ENTREGUE");
            logger.info("✓ [Pedido {}] Status: ENTREGUE com sucesso!", delivery.getPedidoId());
            logger.info("  Entregador: {} | Endereço: {}", entregador, delivery.getEnderecoEntrega());
        } else {
            delivery.setStatus(DeliveryStatus.FALHOU);
            delivery.setFimEntrega(LocalDateTime.now());
            // Talvez notificar falha? Telepizza não tem status FALHOU, talvez manter TRANSPORTE ou criar status.
            // Por enquanto não atualizo.
            logger.warn("✗ [Pedido {}] Status: FALHOU - Cliente não encontrado no endereço", 
                       delivery.getPedidoId());
        }
        
        logger.info("===========================================");
    }

    private void atualizarStatusRemoto(Long pedidoId, String status) {
        try {
            telePizzaClient.atualizarStatus(pedidoId, new AtualizarStatusRequest(status));
        } catch (Exception e) {
            logger.error("Erro ao atualizar status remoto do pedido {}: {}", pedidoId, e.getMessage());
        }
    }
}
