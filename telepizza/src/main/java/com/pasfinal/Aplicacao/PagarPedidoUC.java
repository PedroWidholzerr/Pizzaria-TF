package com.pasfinal.Aplicacao;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Pedido;
import com.pasfinal.Dominio.Servicos.CozinhaService;
import com.pasfinal.Dominio.Servicos.PagamentoService;

@Component
public class PagarPedidoUC {
    private PedidoRepository pedidoRepository;
    private CozinhaService cozinhaService;
    private PagamentoService pagamentoService;

    public PagarPedidoUC(PedidoRepository pedidoRepository, CozinhaService cozinhaService, PagamentoService pagamentoService){
        this.pedidoRepository = pedidoRepository;
        this.cozinhaService = cozinhaService;
        this.pagamentoService = pagamentoService;
    }

    public boolean run(long idPedido){
        Pedido pedido = pedidoRepository.recuperaPorId(idPedido);
        if(pedido == null) {
            return false;
        }

        if(pedido.getStatus() != Pedido.Status.APROVADO){
            return false;
        }

        boolean pagamentoEfetuado = pagamentoService.processarPagamento(pedido);
        
        if(!pagamentoEfetuado) {
            return false;
        }
        
        Pedido pedidoPago = new Pedido(
            pedido.getId(),
            pedido.getCliente(),
            pedido.getEnderecoEntrega(),
            pedido.getDataHoraPedido(),
            LocalDateTime.now(), // data/hora do pagamento
            pedido.getItens(),
            Pedido.Status.PAGO,
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado()
        );
        pedidoRepository.salva(pedidoPago);
        
        // encaminha pra cozinha
        cozinhaService.chegadaDePedido(pedidoPago);

        return true;
    }
}
