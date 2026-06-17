package com.pasfinal.Aplicacao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pasfinal.Aplicacao.Requests.ListarPedidosClienteEntreguesRequest;
import com.pasfinal.Aplicacao.Responses.ListarPedidosEntreguesResponse;
import com.pasfinal.Aplicacao.Responses.PedidoResumoResponse;
import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Pedido;

@Component
public class ListarPedidosClienteEntreguesUC {
    private final PedidoRepository pedidoRepository;

    public ListarPedidosClienteEntreguesUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public ListarPedidosEntreguesResponse run(ListarPedidosClienteEntreguesRequest request) {
        String clienteCpf = request.getClienteCpf();
        LocalDate dataInicio = request.getDataInicio();
        LocalDate dataFim = request.getDataFim();

        if (clienteCpf == null || clienteCpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do cliente é obrigatório");
        }

        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior a data de fim");
        }

        // buscar pedidos entregues do cliente no período
        List<Pedido> pedidos = pedidoRepository.listarPedidosClienteEntreguesEntreDatas(clienteCpf, dataInicio, dataFim);

        // converte para DTO de resposta
        List<PedidoResumoResponse> pedidosResumo = pedidos.stream()
            .map(this::converterParaResumo)
            .collect(Collectors.toList());

        return new ListarPedidosEntreguesResponse(pedidosResumo);
    }

    private PedidoResumoResponse converterParaResumo(Pedido pedido) {
        return new PedidoResumoResponse(
            pedido.getId(),
            pedido.getCliente().getCpf(),
            pedido.getEnderecoEntrega(),
            pedido.getDataHoraPedido(),
            pedido.getDataHoraPagamento(),
            pedido.getStatus().name(),
            pedido.getValorCobrado()
        );
    }
}
