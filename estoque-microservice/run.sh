#!/bin/bash
# Script para executar o microserviço de estoque no Linux/Mac

echo "==============================================="
echo "  Iniciando Microservico de Estoque"
echo "  Porta: 8081"
echo "  Banco: H2 (estoquedb)"
echo "==============================================="
echo ""

cd "$(dirname "$0")"
./mvnw spring-boot:run
