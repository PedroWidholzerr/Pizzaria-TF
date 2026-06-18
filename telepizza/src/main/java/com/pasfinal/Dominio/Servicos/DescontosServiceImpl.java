package com.pasfinal.Dominio.Servicos;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Dados.PedidoRepository;

@Service
public class DescontosServiceImpl implements DescontosService {

    private final PedidoRepository pedidoRepository;

    public DescontosServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double calcularDesconto(String cpfCliente, double valorBase) {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(20);
        int qtdPedidos = pedidoRepository.contarPedidosClienteApos(cpfCliente, dataLimite);
        if (qtdPedidos > 3) {
            return valorBase * 0.07;
        }
        return 0.0;
    }
}
