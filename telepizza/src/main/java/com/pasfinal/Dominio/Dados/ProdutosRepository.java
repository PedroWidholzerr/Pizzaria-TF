package com.pasfinal.Dominio.Dados;

import java.util.List;

import com.pasfinal.Dominio.Entidades.Produto;

public interface ProdutosRepository {
    Produto recuperaProdutoPorid(long id);
    List<Produto> recuperaProdutosCardapio(long id);
}
