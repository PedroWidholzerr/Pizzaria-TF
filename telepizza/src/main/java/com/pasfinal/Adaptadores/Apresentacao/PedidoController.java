package com.pasfinal.Adaptadores.Apresentacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pasfinal.Aplicacao.ListarPedidosEntreguesUC;
import com.pasfinal.Aplicacao.ListarPedidosClienteEntreguesUC;
import com.pasfinal.Aplicacao.PagarPedidoUC;
import com.pasfinal.Aplicacao.RecuperarStatusPedidoUC;
import com.pasfinal.Aplicacao.CancelarPedidoUC;
import com.pasfinal.Aplicacao.Requests.ListarPedidosEntreguesRequest;
import com.pasfinal.Aplicacao.Requests.ListarPedidosClienteEntreguesRequest;
import com.pasfinal.Aplicacao.Responses.ListarPedidosEntreguesResponse;
import com.pasfinal.Aplicacao.Responses.StatusPedidoResponse;
import com.pasfinal.Dominio.Entidades.Pedido;
import org.springframework.web.bind.annotation.RequestBody;
import com.pasfinal.Aplicacao.Requests.SubmeterPedidoRequest;
import com.pasfinal.Aplicacao.Responses.SubmeterPedidoResponse;
import com.pasfinal.Aplicacao.SubmeterPedidoUC;

import com.pasfinal.Aplicacao.AtualizarStatusPedidoUC;
import com.pasfinal.Aplicacao.Requests.AtualizarStatusRequest;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private RecuperarStatusPedidoUC recuperarStatusUC;
    private PagarPedidoUC pagarPedidoUC;
    private SubmeterPedidoUC submeterPedidoUC;
    private CancelarPedidoUC cancelarPedidoUC;
    private ListarPedidosEntreguesUC listarPedidosEntreguesUC;
    private ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC;
    private AtualizarStatusPedidoUC atualizarStatusUC;

    public PedidoController(RecuperarStatusPedidoUC recuperarStatusUC, PagarPedidoUC pagarPedidoUC, 
            SubmeterPedidoUC submeterPedidoUC, CancelarPedidoUC cancelarPedidoUC,
            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
            ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC,
            AtualizarStatusPedidoUC atualizarStatusUC){
        this.recuperarStatusUC = recuperarStatusUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.submeterPedidoUC = submeterPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.listarPedidosClienteEntreguesUC = listarPedidosClienteEntreguesUC;
        this.atualizarStatusUC = atualizarStatusUC;
    }

    /**
     * Recupera status do pedido
     * 
     * Headers do Gateway: X-User-Email, X-User-Type, X-User-CPF
     */
    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public ResponseEntity<StatusPedidoResponse> recuperaStatus(
            @PathVariable("id") long id,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail){
        
        Pedido.Status st = recuperarStatusUC.run(id);
        if(st==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusPedidoResponse.naoEncontrado(id));
        }
        StatusPedidoResponse body = new StatusPedidoResponse(id, st.name(), descricaoStatus(st));
        return ResponseEntity.ok(body);
    }

    /**
     * Processa pagamento do pedido
     */
    @PostMapping("/{id}/pagamento")
    @CrossOrigin("*")
    public ResponseEntity<StatusPedidoResponse> pagarPedido(
            @PathVariable("id") long id,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail){
        
        boolean sucesso = pagarPedidoUC.run(id);
        if(!sucesso) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StatusPedidoResponse(id, "ERRO", "Não foi possível processar o pagamento. Verifique se o pedido existe e está em estado APROVADO."));
        }
        
        Pedido.Status st = recuperarStatusUC.run(id);
        StatusPedidoResponse body = new StatusPedidoResponse(id, st.name(), "Pagamento confirmado com sucesso! Pedido enviado para a cozinha.");
        return ResponseEntity.ok(body);
    }

    private String descricaoStatus(Pedido.Status st){
        switch (st){
            case NOVO: return "Pedido criado aguardando aprovação";
            case APROVADO: return "Pedido aprovado";
            case PAGO: return "Pagamento confirmado";
            case AGUARDANDO: return "Aguardando início do preparo";
            case PREPARACAO: return "Em preparação";
            case PRONTO: return "Pronto para envio";
            case TRANSPORTE: return "Em transporte";
            case ENTREGUE: return "Entregue ao cliente";
            case CANCELADO: return "Pedido cancelado";
            default: return st.name();
        }
    }

    /**
     * Cancela pedido
     */
    @PostMapping("/{id}/cancelamento")
    @CrossOrigin("*")
    public ResponseEntity<StatusPedidoResponse> cancela(
            @PathVariable("id") long id,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail){
        
        boolean ok = cancelarPedidoUC.run(id);
        if(!ok){
            Pedido.Status st = recuperarStatusUC.run(id);
            if(st==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StatusPedidoResponse(id, "NAO_ENCONTRADO", "Pedido não encontrado"));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new StatusPedidoResponse(id, st.name(), "Pedido não pode ser cancelado no status atual"));
        }
        return ResponseEntity.ok(new StatusPedidoResponse(id, Pedido.Status.CANCELADO.name(), "Pedido cancelado"));
    }
    
    /**
     * Submete novo pedido
     * 
     * Usa o email do header para recuperar dados do cliente
     */
    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<SubmeterPedidoResponse> submeterPedido(
            @RequestBody SubmeterPedidoRequest req,
            @RequestHeader("X-User-Email") String userEmail) {
        
        try {
            // Passa o email do header (vem do token JWT validado pelo Gateway)
            SubmeterPedidoResponse resp = submeterPedidoUC.run(req, userEmail);
            if ("NEGADO".equals(resp.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(SubmeterPedidoResponse.pedidoNegado(req.getId(), List.of()));
        }
    }

    @GetMapping("/entregues")
    @CrossOrigin("*")
    public ResponseEntity<ListarPedidosEntreguesResponse> listarPedidosEntregues(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            ListarPedidosEntreguesRequest request = new ListarPedidosEntreguesRequest(dataInicio, dataFim);
            ListarPedidosEntreguesResponse response = listarPedidosEntreguesUC.run(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/entregues/cliente/{cpf}")
    @CrossOrigin("*")
    public ResponseEntity<ListarPedidosEntreguesResponse> listarPedidosClienteEntregues(
            @PathVariable("cpf") String cpf,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            ListarPedidosClienteEntreguesRequest request = new ListarPedidosClienteEntreguesRequest(cpf, dataInicio, dataFim);
            ListarPedidosEntreguesResponse response = listarPedidosClienteEntreguesUC.run(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Atualiza status do pedido
     */
    @org.springframework.web.bind.annotation.PutMapping("/{id}/status")
    @CrossOrigin("*")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable("id") long id,
            @RequestBody AtualizarStatusRequest req) {
        try {
            Pedido.Status novoStatus = Pedido.Status.valueOf(req.getStatus());
            boolean sucesso = atualizarStatusUC.run(id, novoStatus);
            if (!sucesso) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
