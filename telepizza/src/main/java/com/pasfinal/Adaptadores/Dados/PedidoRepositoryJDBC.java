package com.pasfinal.Adaptadores.Dados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Entidades.Cliente;
import com.pasfinal.Dominio.Entidades.Pedido;

@Repository
public class PedidoRepositoryJDBC implements PedidoRepository {
    private JdbcTemplate jdbcTemplate;

    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Pedido> mapperPedidoBasico = new RowMapper<Pedido>(){
        @Override
        public Pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String status = rs.getString("status");
            Pedido.Status st = Pedido.Status.valueOf(status);
            String enderecoEntrega = rs.getString("endereco_entrega");
            
            Timestamp tsDataHoraPedido = rs.getTimestamp("data_hora_pedido");
            LocalDateTime dataHoraPedido = tsDataHoraPedido != null ? tsDataHoraPedido.toLocalDateTime() : null;
            
            Timestamp tsDataHoraPagamento = rs.getTimestamp("data_hora_pagamento");
            LocalDateTime dataHoraPagamento = tsDataHoraPagamento != null ? tsDataHoraPagamento.toLocalDateTime() : null;
            
            double valor = rs.getDouble("valor");
            double impostos = rs.getDouble("impostos");
            double desconto = rs.getDouble("desconto");
            double valorCobrado = rs.getDouble("valor_cobrado");
            
            // Para simplificar, não carregamos itens agora
            return new Pedido(id, new Cliente(rs.getString("cliente_cpf"), "", "", "", ""), 
                enderecoEntrega, dataHoraPedido, dataHoraPagamento, 
                List.of(), st, valor, impostos, desconto, valorCobrado);
        }
    };

    @Override
    public Pedido recuperaPorId(long id) {
        List<Pedido> lst = jdbcTemplate.query(
            "SELECT id, cliente_cpf, endereco_entrega, data_hora_pedido, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado FROM pedidos WHERE id=?", 
            mapperPedidoBasico, id);
        if(lst.isEmpty()) return null;
        return lst.get(0);
    }

    @Override
    public void salva(Pedido pedido) {
        int updated = jdbcTemplate.update(
            "UPDATE pedidos SET status=?, endereco_entrega=?, data_hora_pedido=?, data_hora_pagamento=?, valor=?, impostos=?, desconto=?, valor_cobrado=? WHERE id=?", 
            pedido.getStatus().name(), 
            pedido.getEnderecoEntrega(),
            pedido.getDataHoraPedido(),
            pedido.getDataHoraPagamento(),
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado(),
            pedido.getId());
        
        if(updated == 0){
            jdbcTemplate.update(
                "INSERT INTO pedidos(id, cliente_cpf, endereco_entrega, data_hora_pedido, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado) VALUES(?,?,?,?,?,?,?,?,?,?)", 
                pedido.getId(), 
                pedido.getCliente().getCpf(), 
                pedido.getEnderecoEntrega(),
                pedido.getDataHoraPedido(),
                pedido.getDataHoraPagamento(),
                pedido.getStatus().name(),
                pedido.getValor(),
                pedido.getImpostos(),
                pedido.getDesconto(),
                pedido.getValorCobrado());
        }
    }

    @Override
    public int contarPedidosClienteApos(String cpfCliente, LocalDateTime dataLimite) {
        List<Integer> resultado = jdbcTemplate.query(
            "SELECT COUNT(*) as total FROM pedidos WHERE cliente_cpf=? AND data_hora_pedido >= ?",
            (rs, rowNum) -> rs.getInt("total"),
            cpfCliente,
            dataLimite);
        
        return resultado.isEmpty() ? 0 : resultado.get(0);
    }

    @Override
    public List<Pedido> listarPedidosEntreguesEntreDatas(LocalDate dataInicio, LocalDate dataFim) {
        return jdbcTemplate.query(
            "SELECT id, cliente_cpf, endereco_entrega, data_hora_pedido, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado " +
            "FROM pedidos " +
            "WHERE status = 'ENTREGUE' AND CAST(data_hora_pedido AS DATE) >= ? AND CAST(data_hora_pedido AS DATE) <= ? " +
            "ORDER BY data_hora_pedido DESC",
            mapperPedidoBasico,
            dataInicio,
            dataFim);
    }

    @Override
    public List<Pedido> listarPedidosClienteEntreguesEntreDatas(String clienteCpf, LocalDate dataInicio, LocalDate dataFim) {
        return jdbcTemplate.query(
            "SELECT id, cliente_cpf, endereco_entrega, data_hora_pedido, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado " +
            "FROM pedidos " +
            "WHERE cliente_cpf = ? AND status = 'ENTREGUE' AND CAST(data_hora_pedido AS DATE) >= ? AND CAST(data_hora_pedido AS DATE) <= ? " +
            "ORDER BY data_hora_pedido DESC",
            mapperPedidoBasico,
            clienteCpf,
            dataInicio,
            dataFim);
    }
}
