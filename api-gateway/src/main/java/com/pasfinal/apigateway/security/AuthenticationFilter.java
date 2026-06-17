package com.pasfinal.apigateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Filtro Global de Autenticação
 * 
 * Este filtro é executado ANTES de rotear a requisição para o microserviço.
 * 
 * Fluxo:
 * 1. Cliente → Gateway (este filtro)
 * 2. Filtro valida token JWT
 * 3. Se válido → adiciona headers → envia para telepizza
 * 4. Se inválido → retorna 401 Unauthorized
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // Lista de endpoints que NÃO precisam de autenticação (públicos)
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
        "/auth/login",
        "/auth/register",
        "/auth/validate",
        "/auth/internal-register",
        "/cardapio",          // Consultar cardápio é público
        "/h2-console",        // Console do H2 para desenvolvimento
        "/actuator"           // Endpoints de monitoramento
    );

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Método principal do filtro - executado em TODA requisição
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. Verifica se é um endpoint público (não precisa de autenticação)
        if (isPublicEndpoint(path)) {
            // Permite passar sem validar token
            return chain.filter(exchange);
        }

        // 2. Tenta extrair o token JWT do header Authorization
        String token = extractToken(request);

        // 3. Se não tem token, bloqueia a requisição
        if (token == null) {
            return onError(exchange, "Token não fornecido", HttpStatus.UNAUTHORIZED);
        }

        // 4. Valida o token
        if (!jwtUtil.validateToken(token)) {
            return onError(exchange, "Token inválido ou expirado", HttpStatus.UNAUTHORIZED);
        }

        // 5. Token válido! Extrai informações do usuário
        String username = jwtUtil.extractUsername(token);
        String tipo = jwtUtil.extractTipo(token);
        String cpf = jwtUtil.extractCpf(token);

        // 6. Adiciona headers com as informações do usuário para o microserviço downstream
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Email", username)     // Email do usuário
                .header("X-User-Type", tipo)          // Tipo: USUARIO ou ADMIN
                .header("X-User-CPF", cpf)            // CPF do cliente
                .build();

        // 7. Continua a cadeia de filtros com a requisição modificada
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    /**
     * Verifica se o endpoint é público (não precisa de autenticação)
     */
    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(path::startsWith);
    }

    /**
     * Extrai o token JWT do header Authorization
     * 
     * Formato esperado: "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     */
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " e retorna apenas o token
            return authHeader.substring(7);
        }
        
        return null;
    }

    /**
     * Retorna erro quando autenticação falha
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        
        // Adiciona mensagem de erro no corpo da resposta
        response.getHeaders().add("Content-Type", "application/json");
        String errorBody = String.format("{\"error\": \"%s\"}", message);
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
    }

    /**
     * Define a ordem de execução deste filtro
     * Número menor = executa primeiro
     * Este filtro deve executar ANTES dos outros
     */
    @Override
    public int getOrder() {
        return -100; // Alta prioridade
    }
}
