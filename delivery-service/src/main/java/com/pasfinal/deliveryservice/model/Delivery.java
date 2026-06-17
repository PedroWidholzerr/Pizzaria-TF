package com.pasfinal.deliveryservice.model;

import java.time.LocalDateTime;

public class Delivery {
    
    private Long id;
    private Long pedidoId;
    private String clienteNome;
    private String enderecoEntrega;
    private DeliveryStatus status;
    private LocalDateTime inicioEntrega;
    private LocalDateTime fimEntrega;
    private String entregadorNome;
    
    public enum DeliveryStatus {
        PENDENTE,
        EM_PREPARACAO,
        SAIU_PARA_ENTREGA,
        ENTREGUE,
        FALHOU
    }
    
    public Delivery() {
        this.status = DeliveryStatus.PENDENTE;
        this.inicioEntrega = LocalDateTime.now();
    }
    
    public Delivery(Long pedidoId, String clienteNome, String enderecoEntrega) {
        this();
        this.pedidoId = pedidoId;
        this.clienteNome = clienteNome;
        this.enderecoEntrega = enderecoEntrega;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalDateTime getInicioEntrega() {
        return inicioEntrega;
    }

    public void setInicioEntrega(LocalDateTime inicioEntrega) {
        this.inicioEntrega = inicioEntrega;
    }

    public LocalDateTime getFimEntrega() {
        return fimEntrega;
    }

    public void setFimEntrega(LocalDateTime fimEntrega) {
        this.fimEntrega = fimEntrega;
    }

    public String getEntregadorNome() {
        return entregadorNome;
    }

    public void setEntregadorNome(String entregadorNome) {
        this.entregadorNome = entregadorNome;
    }
}
