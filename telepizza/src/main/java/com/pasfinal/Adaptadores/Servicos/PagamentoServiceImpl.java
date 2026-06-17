package com.pasfinal.Adaptadores.Servicos;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Entidades.Pedido;
import com.pasfinal.Dominio.Servicos.PagamentoService;

@Service
public class PagamentoServiceImpl implements PagamentoService {
    
    @Override
    public boolean processarPagamento(Pedido pedido) {
        // Implementação fake que sempre retorna sucesso
        System.out.println("Processando pagamento do pedido " + pedido.getId() + " - Valor: R$ " + pedido.getValorCobrado());
        return true;
    }
}
