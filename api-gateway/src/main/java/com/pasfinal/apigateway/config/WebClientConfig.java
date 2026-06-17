package com.pasfinal.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuração do WebClient para comunicação com microserviços
 * 
 * O @LoadBalanced permite usar nomes de serviços (ex: lb://telepizza)
 * em vez de URLs fixas. O Eureka resolve o endereço automaticamente.
 */
@Configuration
public class WebClientConfig {

    /**
     * Cria um WebClient configurado com Load Balancer
     * 
     * Permite fazer chamadas como:
     * webClient.get().uri("lb://telepizza/auth/validate")
     * 
     * O "lb://" indica que deve usar load balancing via Eureka
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
