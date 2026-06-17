package com.pasfinal.Adaptadores.Servicos;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Dados.PedidoRepository;
import com.pasfinal.Dominio.Servicos.DescontosService;

@Service
public class DescontosServiceImpl implements DescontosService {
    private final PedidoRepository pedidoRepo;

    public DescontosServiceImpl(PedidoRepository pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    @Override
    public double calcularDesconto(String cpfCliente, double valorBase) {
        // mais de 3 pedidos nos últimos 20 dias
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(20);
        int qtdPedidos = pedidoRepo.contarPedidosClienteApos(cpfCliente, dataLimite);
        
        if (qtdPedidos > 3) {
            // aplica 7% de desconto no custo de cada item
            return valorBase * 0.07;
        }
        
        return 0.0;
    }
}
