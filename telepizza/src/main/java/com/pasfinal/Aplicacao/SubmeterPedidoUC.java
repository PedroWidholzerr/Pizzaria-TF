package com.pasfinal.Aplicacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.pasfinal.Aplicacao.Requests.ItemPedidoRequest;
import com.pasfinal.Aplicacao.Requests.SubmeterPedidoRequest;
import com.pasfinal.Aplicacao.Responses.SubmeterPedidoResponse;
import com.pasfinal.Dominio.Dados.ClienteRepository;
import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Dados.ProdutosRepository;
import com.pasfinal.Dominio.Entidades.Cliente;
import com.pasfinal.Dominio.Entidades.ItemPedido;
import com.pasfinal.Dominio.Entidades.Pedido;
import com.pasfinal.Dominio.Entidades.Produto;
import com.pasfinal.Dominio.Servicos.EstoqueInsuficienteException;
import com.pasfinal.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {

    private final ProdutosRepository produtosRepo;
    private final PedidoRepository pedidoRepo;
    private final ClienteRepository clienteRepo;
    private final PedidoService pedidoService;

    public SubmeterPedidoUC(ProdutosRepository produtosRepo,
            PedidoRepository pedidoRepo,
            ClienteRepository clienteRepo,
            PedidoService pedidoService) {
        this.produtosRepo = produtosRepo;
        this.pedidoRepo = pedidoRepo;
        this.clienteRepo = clienteRepo;
        this.pedidoService = pedidoService;
    }

    public SubmeterPedidoResponse run(SubmeterPedidoRequest req, String emailUsuario) {
        long id = req.getId();

        Pedido pedidoExistente = pedidoRepo.recuperaPorId(id);
        if (pedidoExistente != null) {
            throw new IllegalArgumentException("Já existe um pedido com o ID " + id);
        }

        Cliente cliente = clienteRepo.recuperaPorEmail(emailUsuario);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado para o email: " + emailUsuario);
        }

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoRequest ipr : req.getItens()) {
            Produto p = produtosRepo.recuperaProdutoPorid(ipr.getProdutoId());
            if (p == null) {
                throw new IllegalArgumentException("Produto não encontrado: " + ipr.getProdutoId());
            }
            itens.add(new ItemPedido(p, ipr.getQuantidade()));
        }

        try {
            Pedido pedido = pedidoService.submeterPedido(id, cliente, cliente.getEndereco(), itens);
            return SubmeterPedidoResponse.pedidoAprovado(
                    pedido.getId(),
                    pedido.getValor(),
                    pedido.getImpostos(),
                    pedido.getDesconto(),
                    pedido.getValorCobrado(),
                    pedido.getEnderecoEntrega());
        } catch (EstoqueInsuficienteException e) {
            return SubmeterPedidoResponse.pedidoNegado(id, e.getIdsIndisponiveis());
        }
    }
}
