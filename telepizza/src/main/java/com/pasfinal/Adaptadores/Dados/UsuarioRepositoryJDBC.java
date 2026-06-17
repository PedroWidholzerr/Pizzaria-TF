package com.pasfinal.Adaptadores.Dados;

import com.pasfinal.Dominio.Dados.UsuarioRepository;
import com.pasfinal.Dominio.Entidades.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UsuarioRepositoryJDBC implements UsuarioRepository {
    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Usuario> usuarioRowMapper = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setUsername(rs.getString("username"));
            usuario.setPassword(rs.getString("password"));
            usuario.setTipo(rs.getString("tipo"));
            return usuario;
        }
    };

    @Override
    public Optional<Usuario> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, username, password);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, username);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void salva(Usuario usuario) {
        Long id = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id),0)+1 FROM usuarios", Long.class);
        usuario.setId(id);
        jdbcTemplate.update(
            "INSERT INTO usuarios (id, username, password, tipo) VALUES (?, ?, ?, ?)",
            id, usuario.getUsername(), usuario.getPassword(), usuario.getTipo());
    }

}