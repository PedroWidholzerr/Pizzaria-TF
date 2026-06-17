package com.pasfinal.estoque.dto;

import java.util.List;

/**
 * Request para verificar disponibilidade de múltiplos ingredientes de uma vez
 */
public class VerificarDisponibilidadeLoteRequest {
    private List<ItemVerificacao> itens;

    public VerificarDisponibilidadeLoteRequest() {
    }

    public VerificarDisponibilidadeLoteRequest(List<ItemVerificacao> itens) {
        this.itens = itens;
    }

    public List<ItemVerificacao> getItens() {
        return itens;
    }

    public void setItens(List<ItemVerificacao> itens) {
        this.itens = itens;
    }

    public static class ItemVerificacao {
        private Long ingredienteId;
        private Integer quantidadeNecessaria;

        public ItemVerificacao() {
        }

        public ItemVerificacao(Long ingredienteId, Integer quantidadeNecessaria) {
            this.ingredienteId = ingredienteId;
            this.quantidadeNecessaria = quantidadeNecessaria;
        }

        public Long getIngredienteId() {
            return ingredienteId;
        }

        public void setIngredienteId(Long ingredienteId) {
            this.ingredienteId = ingredienteId;
        }

        public Integer getQuantidadeNecessaria() {
            return quantidadeNecessaria;
        }

        public void setQuantidadeNecessaria(Integer quantidadeNecessaria) {
            this.quantidadeNecessaria = quantidadeNecessaria;
        }
    }
}
