package com.pasfinal.Adaptadores.Servicos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Entidades.Ingrediente;
import com.pasfinal.Dominio.Entidades.ItemPedido;
import com.pasfinal.Dominio.Servicos.EstoqueService;

@Service
public class EstoqueServiceImpl implements EstoqueService {

    private final EstoqueFeignClient estoqueFeignClient;

    public EstoqueServiceImpl(EstoqueFeignClient estoqueFeignClient) {
        this.estoqueFeignClient = estoqueFeignClient;
    }

    @Override
    public List<Long> verificarDisponibilidade(List<ItemPedido> itens) {
        List<Long> indisponiveis = new ArrayList<>();
        for (ItemPedido item : itens) {
            boolean disponivel = true;
            for (Ingrediente ing : item.getItem().getReceita().getIngredientes()) {
                VerificarDisponibilidadeRequest request = new VerificarDisponibilidadeRequest(
                        ing.getId(), item.getQuantidade());
                var response = estoqueFeignClient.verificarDisponibilidade(request);
                if (response == null || !response.getOrDefault("disponivel", false)) {
                    disponivel = false;
                    break;
                }
            }
            if (!disponivel) {
                indisponiveis.add(item.getItem().getId());
            }
        }
        return indisponiveis;
    }

    @Override
    public void baixarEstoque(List<ItemPedido> itens) {
        for (ItemPedido item : itens) {
            for (Ingrediente ingrediente : item.getItem().getReceita().getIngredientes()) {
                AtualizarEstoqueRequest request = new AtualizarEstoqueRequest(
                        ingrediente.getId(), item.getQuantidade());
                estoqueFeignClient.baixarEstoque(request);
            }
        }
    }
}
