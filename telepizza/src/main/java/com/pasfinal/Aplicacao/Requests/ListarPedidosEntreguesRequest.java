package com.pasfinal.Aplicacao.Requests;

import java.time.LocalDate;

public class ListarPedidosEntreguesRequest {
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public ListarPedidosEntreguesRequest() {
    }

    public ListarPedidosEntreguesRequest(LocalDate dataInicio, LocalDate dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
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
