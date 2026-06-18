package com.pasfinal.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostosServiceImpl implements ImpostosService {

    private static final double TAXA_IMPOSTO = 0.10;

    @Override
    public double calcularImpostos(double valorBase) {
        return valorBase * TAXA_IMPOSTO;
    }
}
