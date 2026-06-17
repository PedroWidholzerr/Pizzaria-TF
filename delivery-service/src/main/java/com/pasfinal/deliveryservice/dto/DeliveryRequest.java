package com.pasfinal.deliveryservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class DeliveryRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long pedidoId;
    private String clienteNome;
    private String enderecoEntrega;
    private List<String> itens;
    private Double valorTotal;
    private LocalDateTime dataHoraPedido;
    
    public DeliveryRequest() {
    }
    
    public DeliveryRequest(Long pedidoId, String clienteNome, String enderecoEntrega, 
                          List<String> itens, Double valorTotal) {
        this.pedidoId = pedidoId;
        this.clienteNome = clienteNome;
        this.enderecoEntrega = enderecoEntrega;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.dataHoraPedido = LocalDateTime.now();
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

    public List<String> getItens() {
        return itens;
    }

    public void setItens(List<String> itens) {
        this.itens = itens;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataHoraPedido() {
        return dataHoraPedido;
    }

    public void setDataHoraPedido(LocalDateTime dataHoraPedido) {
        this.dataHoraPedido = dataHoraPedido;
    }

    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "pedidoId=" + pedidoId +
                ", clienteNome='" + clienteNome + '\'' +
                ", enderecoEntrega='" + enderecoEntrega + '\'' +
                ", itens=" + itens +
                ", valorTotal=" + valorTotal +
                ", dataHoraPedido=" + dataHoraPedido +
                '}';
    }
}
