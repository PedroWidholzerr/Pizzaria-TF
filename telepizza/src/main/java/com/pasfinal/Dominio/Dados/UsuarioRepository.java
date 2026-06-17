package com.pasfinal.Dominio.Dados;

import com.pasfinal.Dominio.Entidades.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    Optional<Usuario> findByUsername(String username);
    void salva(Usuario usuario);
}