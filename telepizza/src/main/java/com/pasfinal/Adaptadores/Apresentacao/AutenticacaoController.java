package com.pasfinal.Adaptadores.Apresentacao;

import com.pasfinal.Dominio.Dados.ClienteRepository;
import com.pasfinal.Dominio.Entidades.Cliente;
import com.pasfinal.Dominio.Entidades.Usuario;
import com.pasfinal.Dominio.Servicos.AutenticacaoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AutenticacaoController {
    private final AutenticacaoService autenticacaoService;
    private final ClienteRepository clienteRepository;

    public AutenticacaoController(AutenticacaoService autenticacaoService, ClienteRepository clienteRepository) {
        this.autenticacaoService = autenticacaoService;
        this.clienteRepository = clienteRepository;
    }

    /**
     * ENDPOINT INTERNO - Chamado apenas pelo Gateway
     * Valida credenciais e retorna dados do usuário (incluindo CPF)
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateCredentials(@RequestBody Map<String, String> credentials) {
        String usuario = credentials.get("usuario");
        String senha = credentials.get("senha");

        Optional<Usuario> usuarioOpt = autenticacaoService.login(usuario, senha);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas"));
        }

        Usuario usuarioEntity = usuarioOpt.get();
        
        // Busca o CPF do cliente pelo email
        Cliente cliente = clienteRepository.recuperaPorEmail(usuario);
        String cpf = cliente != null ? cliente.getCpf() : "00000000000";

        // Retorna os dados necessários para o Gateway gerar o token
        return ResponseEntity.ok(Map.of(
            "usuario", usuarioEntity.getUsername(),
            "tipo", usuarioEntity.getTipo(),
            "cpf", cpf
        ));
    }

    /**
     * ENDPOINT LEGADO - Mantido para compatibilidade
     * Pode ser removido após migração completa para JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Usuario> usuarioOpt = autenticacaoService.login(username, password);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }

        Usuario usuario = usuarioOpt.get();
        session.setAttribute("usuario", usuario);

        return ResponseEntity.ok(Map.of(
            "message", "Login realizado com sucesso",
            "usuario", usuario.getUsername(),
            "tipo", usuario.getTipo()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String cpf = body.get("cpf");
        String nome = body.get("nome");
        String celular = body.get("celular");
        String endereco = body.get("endereco");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email é obrigatório"));
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Senha é obrigatória"));
        }
        if (cpf == null || cpf.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "CPF é obrigatório"));
        }
        if (nome == null || nome.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome é obrigatório"));
        }
        if (celular == null || celular.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Celular é obrigatório"));
        }
        if (endereco == null || endereco.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Endereço é obrigatório"));
        }

        try {
            Cliente clienteExistente = clienteRepository.recuperaPorCpf(cpf);
            if (clienteExistente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "CPF já cadastrado"));
            }
        } catch (Exception e) {
        }

        // criar usuario para autenticação
        Usuario usuario = new Usuario();
        usuario.setUsername(email);
        usuario.setPassword(password);
        usuario.setTipo("USUARIO");

        boolean ok = autenticacaoService.registra(usuario);
        if (!ok) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Email já cadastrado"));
        }

        // criar cliente
        try {
            Cliente cliente = new Cliente(cpf, nome, celular, endereco, email);
            clienteRepository.salva(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao cadastrar cliente: " + e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "message", "Cliente registrado com sucesso",
                    "email", email,
                    "cpf", cpf,
                    "nome", nome
                ));
    }

    /**
     * ENDPOINT INTERNO - Chamado apenas pelo Gateway
     * Registra novo usuário e cliente, retorna dados para o Gateway gerar token
     */
    @PostMapping("/internal-register")
    public ResponseEntity<?> registerInternal(@RequestBody Map<String, String> body) {
        String usuario = body.get("usuario");
        String senha = body.get("senha");
        String cpf = body.get("cpf");
        String nome = body.get("nome");
        String telefone = body.get("telefone");
        String endereco = body.get("endereco");

        // Validações
        if (usuario == null || usuario.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario é obrigatório"));
        }
        if (senha == null || senha.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Senha é obrigatória"));
        }
        if (cpf == null || cpf.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "CPF é obrigatório"));
        }
        if (nome == null || nome.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome é obrigatório"));
        }
        if (telefone == null || telefone.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Telefone é obrigatório"));
        }
        if (endereco == null || endereco.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Endereço é obrigatório"));
        }

        // Verifica se CPF já existe
        try {
            Cliente clienteExistente = clienteRepository.recuperaPorCpf(cpf);
            if (clienteExistente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "CPF já cadastrado"));
            }
        } catch (Exception e) {
        }

        // Cria usuário
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setUsername(usuario);
        usuarioEntity.setPassword(senha);
        usuarioEntity.setTipo("USUARIO");

        boolean ok = autenticacaoService.registra(usuarioEntity);
        if (!ok) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Usuario já cadastrado"));
        }

        // Cria cliente
        try {
            Cliente cliente = new Cliente(cpf, nome, telefone, endereco, usuario);
            clienteRepository.salva(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao cadastrar cliente: " + e.getMessage()));
        }

        // Retorna dados para o Gateway gerar o token
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "usuario", usuario,
                    "cpf", cpf,
                    "tipo", "USUARIO"
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null) {
            return ResponseEntity.ok(Map.of("logado", false, "tipo", "PUBLICO"));
        }

        return ResponseEntity.ok(Map.of(
            "logado", true,
            "usuario", usuario.getUsername(),
            "tipo", usuario.getTipo()
        ));
    }
}