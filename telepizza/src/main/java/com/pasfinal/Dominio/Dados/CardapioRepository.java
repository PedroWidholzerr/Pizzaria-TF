package com.pasfinal.Dominio.Dados;

import java.util.List;

import com.pasfinal.Dominio.Entidades.CabecalhoCardapio;
import com.pasfinal.Dominio.Entidades.Cardapio;
import com.pasfinal.Dominio.Entidades.Produto;

public interface CardapioRepository {
    List<CabecalhoCardapio> cardapiosDisponiveis();
    Cardapio recuperaPorId(long id);
    List<Produto> indicacoesDoChef();
}
