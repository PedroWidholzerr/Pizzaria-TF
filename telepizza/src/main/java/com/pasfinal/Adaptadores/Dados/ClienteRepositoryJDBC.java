package com.pasfinal.Adaptadores.Dados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pasfinal.Dominio.Dados.ClienteRepository;
import com.pasfinal.Dominio.Entidades.Cliente;

@Repository
public class ClienteRepositoryJDBC implements ClienteRepository {
    private JdbcTemplate jdbcTemplate;

    public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Cliente> mapperCliente = new RowMapper<Cliente>() {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            String cpf = rs.getString("cpf");
            String nome = rs.getString("nome");
            String celular = rs.getString("celular");
            String endereco = rs.getString("endereco");
            String email = rs.getString("email");
            return new Cliente(cpf, nome, celular, endereco, email);
        }
    };

    @Override
    public Cliente recuperaPorCpf(String cpf) {
        List<Cliente> lst = jdbcTemplate.query(
            "SELECT cpf, nome, celular, endereco, email FROM clientes WHERE cpf = ?",
            mapperCliente,
            cpf);
        
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public Cliente recuperaPorEmail(String email) {
        List<Cliente> lst = jdbcTemplate.query(
            "SELECT cpf, nome, celular, endereco, email FROM clientes WHERE email = ?",
            mapperCliente,
            email);
        
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    @Override
    public void salva(Cliente cliente) {
        try {
            jdbcTemplate.update(
                "INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES (?,?,?,?,?)",
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getCelular(),
                cliente.getEndereco(),
                cliente.getEmail()
            );
        } catch (DataAccessException ex) {
            throw new IllegalStateException("Erro ao salvar cliente: " + ex.getMessage());
        }
    }

}
