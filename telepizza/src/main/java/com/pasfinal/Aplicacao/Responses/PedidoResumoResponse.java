package com.pasfinal.Aplicacao.Responses;

import java.time.LocalDateTime;

/**
 * DTO para representar um pedido resumido na listagem.
 */
public class PedidoResumoResponse {
    private long id;
    private String clienteCpf;
    private String enderecoEntrega;
    private LocalDateTime dataHoraPedido;
    private LocalDateTime dataHoraPagamento;
    private String status;
    private double valorCobrado;

    public PedidoResumoResponse() {
    }

    public PedidoResumoResponse(long id, String clienteCpf, String enderecoEntrega, 
            LocalDateTime dataHoraPedido, LocalDateTime dataHoraPagamento, 
            String status, double valorCobrado) {
        this.id = id;
        this.clienteCpf = clienteCpf;
        this.enderecoEntrega = enderecoEntrega;
        this.dataHoraPedido = dataHoraPedido;
        this.dataHoraPagamento = dataHoraPagamento;
        this.status = status;
        this.valorCobrado = valorCobrado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public LocalDateTime getDataHoraPedido() {
        return dataHoraPedido;
    }

    public void setDataHoraPedido(LocalDateTime dataHoraPedido) {
        this.dataHoraPedido = dataHoraPedido;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }
}
