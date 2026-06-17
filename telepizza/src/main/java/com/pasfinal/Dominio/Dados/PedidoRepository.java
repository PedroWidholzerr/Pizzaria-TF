package com.pasfinal.Dominio.Dados;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pasfinal.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido recuperaPorId(long id);
    void salva(Pedido pedido);
    
    /**
     * Conta quantos pedidos um cliente tem a partir de uma data.
     * @return quantidade de pedidos do cliente após a data limite
     */
    int contarPedidosClienteApos(String cpfCliente, LocalDateTime dataLimite);
    List<Pedido> listarPedidosEntreguesEntreDatas(LocalDate dataInicio, LocalDate dataFim);
    List<Pedido> listarPedidosClienteEntreguesEntreDatas(String clienteCpf, LocalDate dataInicio, LocalDate dataFim);
}
