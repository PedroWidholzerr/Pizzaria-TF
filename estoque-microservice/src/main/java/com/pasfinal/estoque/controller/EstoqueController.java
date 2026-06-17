package com.pasfinal.estoque.controller;

import com.pasfinal.estoque.dto.AtualizarEstoqueRequest;
import com.pasfinal.estoque.dto.ItemEstoqueDTO;
import com.pasfinal.estoque.dto.VerificarDisponibilidadeRequest;
import com.pasfinal.estoque.service.EstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    /**
     * Lista todos os itens do estoque
     * GET /api/estoque
     */
    @GetMapping
    public ResponseEntity<List<ItemEstoqueDTO>> listarTodos() {
        return ResponseEntity.ok(estoqueService.listarTodos());
    }

    /**
     * Busca item de estoque por ID
     * GET /api/estoque/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return estoqueService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca item de estoque por ID do ingrediente
     * GET /api/estoque/ingrediente/{ingredienteId}
     */
    @GetMapping("/ingrediente/{ingredienteId}")
    public ResponseEntity<?> buscarPorIngredienteId(@PathVariable Long ingredienteId) {
        return estoqueService.buscarPorIngredienteId(ingredienteId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Verifica se há disponibilidade de um ingrediente
     * POST /api/estoque/verificar-disponibilidade
     */
    @PostMapping("/verificar-disponibilidade")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidade(
            @RequestBody VerificarDisponibilidadeRequest request) {
        boolean disponivel = estoqueService.verificarDisponibilidade(
            request.getIngredienteId(), 
            request.getQuantidadeNecessaria()
        );
        Map<String, Boolean> response = new HashMap<>();
        response.put("disponivel", disponivel);
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica disponibilidade de múltiplos ingredientes de uma vez
     * POST /api/estoque/verificar-disponibilidade-lote
     */
    @PostMapping("/verificar-disponibilidade-lote")
    public ResponseEntity<?> verificarDisponibilidadeLote(
            @RequestBody com.pasfinal.estoque.dto.VerificarDisponibilidadeLoteRequest request) {
        return ResponseEntity.ok(estoqueService.verificarDisponibilidadeLote(request));
    }

    /**
     * Dá baixa no estoque (remove quantidade)
     * POST /api/estoque/baixar
     */
    @PostMapping("/baixar")
    public ResponseEntity<?> baixarEstoque(@RequestBody AtualizarEstoqueRequest request) {
        try {
            ItemEstoqueDTO resultado = estoqueService.baixarEstoque(
                request.getIngredienteId(), 
                request.getQuantidade()
            );
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Adiciona ao estoque (aumenta quantidade)
     * POST /api/estoque/adicionar
     */
    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarEstoque(@RequestBody AtualizarEstoqueRequest request) {
        try {
            ItemEstoqueDTO resultado = estoqueService.adicionarEstoque(
                request.getIngredienteId(), 
                request.getQuantidade()
            );
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cria um novo item de estoque
     * POST /api/estoque
     */
    @PostMapping
    public ResponseEntity<?> criarItemEstoque(@RequestBody AtualizarEstoqueRequest request) {
        try {
            ItemEstoqueDTO resultado = estoqueService.criarItemEstoque(
                request.getIngredienteId(), 
                request.getQuantidade()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
