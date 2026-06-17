package com.pasfinal.Aplicacao.Responses;

import java.util.List;

public class SubmeterPedidoResponse {
    private long id;
    private String status;
    private double valor;
    private double impostos;
    private double desconto;
    private double valorCobrado;
    private String enderecoEntrega;
    private List<Long> itensIndisponiveis;

    public SubmeterPedidoResponse(long id, String status, double valor, double impostos, double desconto, double valorCobrado, String enderecoEntrega, List<Long> itensIndisponiveis) {
        this.id = id;
        this.status = status;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
        this.enderecoEntrega = enderecoEntrega;
        this.itensIndisponiveis = itensIndisponiveis;
    }

    public long getId() { return id; }
    public String getStatus() { return status; }
    public double getValor() { return valor; }
    public double getImpostos() { return impostos; }
    public double getDesconto() { return desconto; }
    public double getValorCobrado() { return valorCobrado; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public List<Long> itensIndisponiveis() { return itensIndisponiveis; }

    public static SubmeterPedidoResponse pedidoAprovado(long id, double valor, double impostos, double desconto, double valorCobrado, String enderecoEntrega) {
        return new SubmeterPedidoResponse(id, "APROVADO", valor, impostos, desconto, valorCobrado, enderecoEntrega, List.of());
    }

    public static SubmeterPedidoResponse pedidoNegado(long id, List<Long> itensIndisponiveis) {
        return new SubmeterPedidoResponse(id, "NEGADO", 0, 0, 0, 0, null, itensIndisponiveis);
    }
}
