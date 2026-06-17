package com.pasfinal.Dominio.Servicos;

import com.pasfinal.Dominio.Entidades.Pedido;

public interface PagamentoService {
    boolean processarPagamento(Pedido pedido);
}
