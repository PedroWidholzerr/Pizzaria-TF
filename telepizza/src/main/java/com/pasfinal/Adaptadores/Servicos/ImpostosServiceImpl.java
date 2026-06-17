package com.pasfinal.Adaptadores.Servicos;

import org.springframework.stereotype.Service;

import com.pasfinal.Dominio.Servicos.ImpostosService;

@Service
public class ImpostosServiceImpl implements ImpostosService {
    
    private static final double TAXA_IMPOSTO = 0.10;

    @Override
    public double calcularImpostos(double valorBase) {
        return valorBase * TAXA_IMPOSTO;
    }
}
