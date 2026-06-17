package com.pasfinal.Aplicacao.Requests;

import java.time.LocalDate;

public class ListarPedidosClienteEntreguesRequest {
    private String clienteCpf;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public ListarPedidosClienteEntreguesRequest() {
    }

    public ListarPedidosClienteEntreguesRequest(String clienteCpf, LocalDate dataInicio, LocalDate dataFim) {
        this.clienteCpf = clienteCpf;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}
