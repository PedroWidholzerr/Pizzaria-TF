package com.pasfinal.Adaptadores.Servicos;

import java.util.List;

/**
 * Response da verificação de disponibilidade em lote
 */
public class VerificarDisponibilidadeLoteResponse {
    private boolean todosDisponiveis;
    private List<Long> ingredientesIndisponiveis;

    public VerificarDisponibilidadeLoteResponse() {
    }

    public VerificarDisponibilidadeLoteResponse(boolean todosDisponiveis, List<Long> ingredientesIndisponiveis) {
        this.todosDisponiveis = todosDisponiveis;
        this.ingredientesIndisponiveis = ingredientesIndisponiveis;
    }

    public boolean isTodosDisponiveis() {
        return todosDisponiveis;
    }

    public void setTodosDisponiveis(boolean todosDisponiveis) {
        this.todosDisponiveis = todosDisponiveis;
    }

    public List<Long> getIngredientesIndisponiveis() {
        return ingredientesIndisponiveis;
    }

    public void setIngredientesIndisponiveis(List<Long> ingredientesIndisponiveis) {
        this.ingredientesIndisponiveis = ingredientesIndisponiveis;
    }
}
