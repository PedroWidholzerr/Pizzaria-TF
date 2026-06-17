package com.pasfinal.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pasfinal.Aplicacao.Responses.CardapioResponse;
import com.pasfinal.Dominio.Entidades.Cardapio;
import com.pasfinal.Dominio.Entidades.Produto;
import com.pasfinal.Dominio.Servicos.CardapioService;

@Component
public class RecuperarCardapioUC {
    private CardapioService cardapioService;

    @Autowired
    public RecuperarCardapioUC(CardapioService cardapioService){
        this.cardapioService = cardapioService;
    }

    public CardapioResponse run(long idCardapio){
        Cardapio cardapio = cardapioService.recuperaCardapio(idCardapio);
        List<Produto> sugestoes = cardapioService.recuperaSugestoesDoChef();
        return new CardapioResponse(cardapio,sugestoes);
    }
}
