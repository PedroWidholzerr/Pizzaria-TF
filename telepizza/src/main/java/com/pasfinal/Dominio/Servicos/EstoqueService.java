package com.pasfinal.Dominio.Servicos;

import java.util.List;

import com.pasfinal.Dominio.Entidades.ItemPedido;

public interface EstoqueService {
    List<Long> verificarDisponibilidade(List<ItemPedido> itens);
    void baixarEstoque(List<ItemPedido> itens);
}
