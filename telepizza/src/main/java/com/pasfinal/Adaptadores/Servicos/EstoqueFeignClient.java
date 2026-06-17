package com.pasfinal.Adaptadores.Servicos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Feign Client para comunicação declarativa com o microserviço de estoque.
 * Usa service discovery do Eureka para localizar o serviço automaticamente.
 */
@FeignClient(name = "estoque-microservice")
public interface EstoqueFeignClient {

    /**
     * Lista todos os itens do estoque
     */
    @GetMapping("/api/estoque")
    List<ItemEstoqueDTO> listarTodos();

    /**
     * Busca item de estoque por ID do ingrediente
     */
    @GetMapping("/api/estoque/ingrediente/{ingredienteId}")
    ItemEstoqueDTO buscarPorIngredienteId(@PathVariable("ingredienteId") Long ingredienteId);

    /**
     * Verifica se há disponibilidade de um ingrediente
     */
    @PostMapping("/api/estoque/verificar-disponibilidade")
    Map<String, Boolean> verificarDisponibilidade(@RequestBody VerificarDisponibilidadeRequest request);

    /**
     * Verifica disponibilidade de múltiplos ingredientes de uma vez
     */
    @PostMapping("/api/estoque/verificar-disponibilidade-lote")
    VerificarDisponibilidadeLoteResponse verificarDisponibilidadeLote(
            @RequestBody VerificarDisponibilidadeLoteRequest request);

    /**
     * Dá baixa no estoque (remove quantidade)
     */
    @PostMapping("/api/estoque/baixar")
    ItemEstoqueDTO baixarEstoque(@RequestBody AtualizarEstoqueRequest request);

    /**
     * Adiciona ao estoque (aumenta quantidade)
     */
    @PostMapping("/api/estoque/adicionar")
    ItemEstoqueDTO adicionarEstoque(@RequestBody AtualizarEstoqueRequest request);

    /**
     * Cria um novo item de estoque
     */
    @PostMapping("/api/estoque")
    ItemEstoqueDTO criarItemEstoque(@RequestBody AtualizarEstoqueRequest request);
}
