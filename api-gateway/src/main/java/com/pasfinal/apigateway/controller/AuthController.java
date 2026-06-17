package com.pasfinal.apigateway.controller;

import com.pasfinal.apigateway.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Controller de Autenticação no Gateway
 * 
 * Responsável por:
 * - Receber requisições de login/register
 * - Encaminhar para o microserviço telepizza
 * - Gerar token JWT após autenticação bem-sucedida
 * - Retornar token para o cliente
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final WebClient.Builder webClientBuilder;

    public AuthController(JwtUtil jwtUtil, WebClient.Builder webClientBuilder) {
        this.jwtUtil = jwtUtil;
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Endpoint de Login
     * 
     * Fluxo:
     * 1. Recebe credenciais (username, password)
     * 2. Encaminha para telepizza validar no banco
     * 3. Se válido, gera token JWT
     * 4. Retorna token para o cliente
     * 
     * Exemplo de requisição:
     * POST /auth/login
     * {
     *   "username": "usuario@email.com",
     *   "password": "123456"
     * }
     * 
     * Exemplo de resposta:
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "usuario": "usuario@email.com",
     *   "tipo": "USUARIO"
     * }
     */
    @PostMapping("/login")
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> credentials) {
        String usuario = credentials.get("usuario");
        String senha = credentials.get("senha");

        // Cria WebClient para chamar o telepizza
        // lb://telepizza = load balancer + nome do serviço registrado no Eureka
        WebClient webClient = webClientBuilder.build();

        // Chama o endpoint INTERNO de validação no telepizza
        return webClient.post()
                .uri("lb://telepizza/auth/validate")  // Endpoint interno (não público)
                .bodyValue(Map.of("usuario", usuario, "senha", senha))
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    // Telepizza retornou os dados do usuário validado
                    String usuarioEmail = (String) response.get("usuario");
                    String tipo = (String) response.get("tipo");
                    String cpf = (String) response.get("cpf");
                    
                    // Gera o token JWT
                    String token = jwtUtil.generateToken(usuarioEmail, tipo, cpf);
                    
                    // Retorna o token para o cliente
                    Map<String, String> responseBody = Map.of(
                        "token", token,
                        "usuario", usuarioEmail,
                        "tipo", tipo,
                        "message", "Login realizado com sucesso"
                    );
                    return Mono.just(ResponseEntity.ok(responseBody));
                })
                .onErrorResume(error -> {
                    // Erro na validação (credenciais inválidas ou erro no telepizza)
                    Map<String, String> errorBody = Map.of("error", "Credenciais inválidas");
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorBody));
                });
    }

    /**
     * Endpoint de Registro
     * 
     * Fluxo:
     * 1. Recebe dados do novo usuário
     * 2. Encaminha para telepizza criar usuário + cliente
     * 3. Se sucesso, gera token JWT
     * 4. Retorna token (já logado)
     * 
     * Exemplo de requisição:
     * POST /auth/register
     * {
     *   "email": "novo@email.com",
     *   "password": "senha123",
     *   "cpf": "12345678900",
     *   "nome": "João Silva",
     *   "celular": "51999999999",
     *   "endereco": "Rua X, 123"
     * }
     */
    @PostMapping("/register")
    @SuppressWarnings("unchecked")
    public Mono<ResponseEntity<Map<String, String>>> register(@RequestBody Map<String, String> body) {
        WebClient webClient = webClientBuilder.build();

        // Padroniza os campos para o formato esperado pelo telepizza
        Map<String, String> telepizzaBody = Map.of(
            "usuario", body.getOrDefault("email", body.getOrDefault("usuario", "")),
            "senha", body.getOrDefault("password", body.getOrDefault("senha", "")),
            "cpf", body.getOrDefault("cpf", ""),
            "nome", body.getOrDefault("nome", ""),
            "telefone", body.getOrDefault("celular", body.getOrDefault("telefone", "")),
            "endereco", body.getOrDefault("endereco", "")
        );

        // Encaminha para o telepizza criar o usuário e cliente
        return webClient.post()
                .uri("lb://telepizza/auth/internal-register")
                .bodyValue(telepizzaBody)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    // Registro bem-sucedido, gera token
                    String usuario = (String) response.get("usuario");
                    String cpf = (String) response.get("cpf");
                    String tipo = (String) response.get("tipo");
                    
                    String token = jwtUtil.generateToken(usuario, tipo, cpf);
                    
                    Map<String, String> responseBody = Map.of(
                        "token", token,
                        "usuario", usuario,
                        "tipo", tipo,
                        "message", "Usuário registrado com sucesso"
                    );
                    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(responseBody));
                })
                .onErrorResume(error -> {
                    Map<String, String> errorBody = Map.of("error", "Erro ao registrar usuário: " + error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(errorBody));
                });
    }

    /**
     * Endpoint para validar se o token ainda é válido
     * 
     * Cliente pode usar para verificar se ainda está autenticado
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "usuario", jwtUtil.extractUsername(token),
                    "tipo", jwtUtil.extractTipo(token)
                ));
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("valid", false, "error", "Token inválido"));
    }
}
