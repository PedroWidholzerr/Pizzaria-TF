package com.pasfinal.estoque.dto;

public class VerificarDisponibilidadeRequest {
    private Long ingredienteId;
    private Integer quantidadeNecessaria;

    public VerificarDisponibilidadeRequest() {
    }

    public VerificarDisponibilidadeRequest(Long ingredienteId, Integer quantidadeNecessaria) {
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
