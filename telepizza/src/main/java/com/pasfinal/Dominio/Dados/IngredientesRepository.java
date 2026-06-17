package com.pasfinal.Dominio.Dados;

import java.util.List;

import com.pasfinal.Dominio.Entidades.Ingrediente;

public interface IngredientesRepository {
    List<Ingrediente> recuperaTodos();
    List<Ingrediente> recuperaIngredientesReceita(long id);
}
