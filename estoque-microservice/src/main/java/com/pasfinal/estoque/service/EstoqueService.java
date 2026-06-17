package com.pasfinal.estoque.service;

import com.pasfinal.estoque.dto.ItemEstoqueDTO;
import com.pasfinal.estoque.entity.Ingrediente;
import com.pasfinal.estoque.entity.ItemEstoque;
import com.pasfinal.estoque.repository.IngredienteRepository;
import com.pasfinal.estoque.repository.ItemEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstoqueService {
    
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final IngredienteRepository ingredienteRepository;

    public EstoqueService(ItemEstoqueRepository itemEstoqueRepository, IngredienteRepository ingredienteRepository) {
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<ItemEstoqueDTO> listarTodos() {
        return itemEstoqueRepository.findAll().stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    public Optional<ItemEstoqueDTO> buscarPorId(Long id) {
        return itemEstoqueRepository.findById(id)
            .map(this::converterParaDTO);
    }

    public Optional<ItemEstoqueDTO> buscarPorIngredienteId(Long ingredienteId) {
        return itemEstoqueRepository.findByIngredienteId(ingredienteId)
            .map(this::converterParaDTO);
    }

    public boolean verificarDisponibilidade(Long ingredienteId, Integer quantidadeNecessaria) {
        Optional<ItemEstoque> itemOpt = itemEstoqueRepository.findByIngredienteId(ingredienteId);
        if (itemOpt.isEmpty()) {
            return false;
        }
        return itemOpt.get().getQuantidade() >= quantidadeNecessaria;
    }

    @Transactional
    public ItemEstoqueDTO baixarEstoque(Long ingredienteId, Integer quantidade) {
        Optional<ItemEstoque> itemOpt = itemEstoqueRepository.findByIngredienteId(ingredienteId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item de estoque não encontrado para o ingrediente: " + ingredienteId);
        }

        ItemEstoque item = itemOpt.get();
        if (item.getQuantidade() < quantidade) {
            throw new IllegalStateException("Quantidade insuficiente em estoque. Disponível: " + 
                item.getQuantidade() + ", Necessário: " + quantidade);
        }

        item.setQuantidade(item.getQuantidade() - quantidade);
        ItemEstoque itemAtualizado = itemEstoqueRepository.save(item);
        return converterParaDTO(itemAtualizado);
    }

    @Transactional
    public ItemEstoqueDTO adicionarEstoque(Long ingredienteId, Integer quantidade) {
        Optional<ItemEstoque> itemOpt = itemEstoqueRepository.findByIngredienteId(ingredienteId);
        
        ItemEstoque item;
        if (itemOpt.isEmpty()) {
            // Criar novo item de estoque
            Optional<Ingrediente> ingredienteOpt = ingredienteRepository.findById(ingredienteId);
            if (ingredienteOpt.isEmpty()) {
                throw new IllegalArgumentException("Ingrediente não encontrado: " + ingredienteId);
            }
            item = new ItemEstoque();
            item.setIngrediente(ingredienteOpt.get());
            item.setQuantidade(quantidade);
        } else {
            item = itemOpt.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
        }

        ItemEstoque itemAtualizado = itemEstoqueRepository.save(item);
        return converterParaDTO(itemAtualizado);
    }

    @Transactional
    public ItemEstoqueDTO criarItemEstoque(Long ingredienteId, Integer quantidade) {
        Optional<Ingrediente> ingredienteOpt = ingredienteRepository.findById(ingredienteId);
        if (ingredienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Ingrediente não encontrado: " + ingredienteId);
        }

        ItemEstoque item = new ItemEstoque();
        item.setIngrediente(ingredienteOpt.get());
        item.setQuantidade(quantidade);

        ItemEstoque itemSalvo = itemEstoqueRepository.save(item);
        return converterParaDTO(itemSalvo);
    }

    public com.pasfinal.estoque.dto.VerificarDisponibilidadeLoteResponse verificarDisponibilidadeLote(
            com.pasfinal.estoque.dto.VerificarDisponibilidadeLoteRequest request) {
        List<Long> indisponiveis = new ArrayList<>();
        
        for (var item : request.getItens()) {
            if (!verificarDisponibilidade(item.getIngredienteId(), item.getQuantidadeNecessaria())) {
                indisponiveis.add(item.getIngredienteId());
            }
        }
        
        return new com.pasfinal.estoque.dto.VerificarDisponibilidadeLoteResponse(
            indisponiveis.isEmpty(), 
            indisponiveis
        );
    }

    private ItemEstoqueDTO converterParaDTO(ItemEstoque item) {
        return new ItemEstoqueDTO(
            item.getId(),
            item.getQuantidade(),
            item.getIngrediente().getId(),
            item.getIngrediente().getDescricao()
        );
    }
}
