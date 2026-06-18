package com.pasfinal.Dominio.Servicos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Cliente;
import com.pasfinal.Dominio.Entidades.ItemPedido;
import com.pasfinal.Dominio.Entidades.Pedido;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EstoqueService estoqueService;
    private final DescontosService descontosService;
    private final ImpostosService impostosService;
    private final PagamentoService pagamentoService;
    private final CozinhaService cozinhaService;

    public PedidoService(PedidoRepository pedidoRepository,
                         EstoqueService estoqueService,
                         DescontosService descontosService,
                         ImpostosService impostosService,
                         PagamentoService pagamentoService,
                         CozinhaService cozinhaService) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService = estoqueService;
        this.descontosService = descontosService;
        this.impostosService = impostosService;
        this.pagamentoService = pagamentoService;
        this.cozinhaService = cozinhaService;
    }

    public Pedido submeterPedido(long id, Cliente cliente, String enderecoEntrega, List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido precisa ter pelo menos um item");
        }

        List<Long> indisponiveis = estoqueService.verificarDisponibilidade(itens);
        if (!indisponiveis.isEmpty()) {
            throw new EstoqueInsuficienteException(indisponiveis);
        }

        double valor = calcularValorItens(itens);
        double desconto = descontosService.calcularDesconto(cliente.getCpf(), valor);
        double impostos = impostosService.calcularImpostos(valor);
        double valorCobrado = valor - desconto + impostos;

        LocalDateTime agora = LocalDateTime.now();
        Pedido pedidoNovo = new Pedido(id, cliente, enderecoEntrega, agora,
                null, List.of(), Pedido.Status.NOVO, 0, 0, 0, 0);
        pedidoRepository.salva(pedidoNovo);

        estoqueService.baixarEstoque(itens);

        Pedido pedidoAprovado = new Pedido(id, cliente, enderecoEntrega, agora,
                null, itens, Pedido.Status.APROVADO, valor, impostos, desconto, valorCobrado);
        pedidoRepository.salva(pedidoAprovado);

        return pedidoAprovado;
    }

    public Pedido consultarPedido(long pedidoId) {
        return pedidoRepository.recuperaPorId(pedidoId);
    }

    public Pedido cancelarPedido(long pedidoId) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalArgumentException("Pedido não pode ser cancelado. Status atual: " + pedido.getStatus());
        }
        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoRepository.salva(pedido);
        return pedidoRepository.recuperaPorId(pedidoId);
    }

    public Pedido pagarPedido(long pedidoId) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalArgumentException("Pedido precisa estar APROVADO para pagar");
        }
        if (!pagamentoService.processarPagamento(pedido)) {
            throw new IllegalArgumentException("Pagamento não aprovado");
        }

        Pedido pedidoPago = new Pedido(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getEnderecoEntrega(),
                pedido.getDataHoraPedido(),
                LocalDateTime.now(),
                pedido.getItens(),
                Pedido.Status.PAGO,
                pedido.getValor(),
                pedido.getImpostos(),
                pedido.getDesconto(),
                pedido.getValorCobrado());
        pedidoRepository.salva(pedidoPago);

        cozinhaService.chegadaDePedido(pedidoPago);
        return pedidoRepository.recuperaPorId(pedidoId);
    }

    public List<Pedido> listarEntregues(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.listarPedidosEntreguesEntreDatas(inicio, fim);
    }

    public List<Pedido> listarEntreguesDoCliente(String cpf, LocalDate inicio, LocalDate fim) {
        return pedidoRepository.listarPedidosClienteEntreguesEntreDatas(cpf, inicio, fim);
    }

    public Pedido atualizarStatus(long pedidoId, Pedido.Status novoStatus) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        pedido.setStatus(novoStatus);
        pedidoRepository.salva(pedido);
        return pedidoRepository.recuperaPorId(pedidoId);
    }

    private double calcularValorItens(List<ItemPedido> itens) {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getItem().getPreco() * item.getQuantidade();
        }
        return total;
    }
}
