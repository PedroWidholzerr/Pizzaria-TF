package com.pasfinal.Dominio.Dados;

import com.pasfinal.Dominio.Entidades.Receita;

public interface ReceitasRepository {
    Receita recuperaReceita(long id);
    
}
