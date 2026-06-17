package com.pasfinal.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitário para gerenciar tokens JWT
 * 
 * JWT (JSON Web Token) é um padrão de token que contém:
 * - Header: tipo do token e algoritmo de assinatura
 * - Payload: dados do usuário (claims)
 * - Signature: assinatura para garantir integridade
 */
@Component
public class JwtUtil {
    
    // Chave secreta para assinar os tokens (em produção, deve estar em variável de ambiente)
    private static final String SECRET_KEY = "TelePizzaSecretKeyForJWTTokenGenerationAndValidation2024";
    
    // Tempo de expiração do token: 24 horas em milissegundos
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 horas
    
    // Gera a chave de assinatura a partir do secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    /**
     * Gera um token JWT para o usuário
     * 
     * @param username Email do usuário
     * @param tipo Tipo do usuário (USUARIO ou ADMIN)
     * @param cpf CPF do cliente
     * @return Token JWT assinado
     */
    public String generateToken(String username, String tipo, String cpf) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tipo", tipo);
        claims.put("cpf", cpf);
        
        return Jwts.builder()
                .setClaims(claims)                          // Informações do usuário
                .setSubject(username)                       // Email como identificador principal
                .setIssuedAt(new Date())                    // Data de criação
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Validade
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assinatura
                .compact();
    }
    
    /**
     * Valida se o token é válido
     * 
     * Verifica:
     * - Se a assinatura está correta
     * - Se não expirou
     * - Se não foi adulterado
     * 
     * @param token Token JWT
     * @return true se válido, false se inválido ou expirado
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Token inválido, expirado ou adulterado
            return false;
        }
    }
    
    /**
     * Extrai o email do usuário do token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    /**
     * Extrai o tipo do usuário do token
     */
    public String extractTipo(String token) {
        return (String) extractAllClaims(token).get("tipo");
    }
    
    /**
     * Extrai o CPF do token
     */
    public String extractCpf(String token) {
        return (String) extractAllClaims(token).get("cpf");
    }
    
    /**
     * Extrai todas as claims (informações) do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
