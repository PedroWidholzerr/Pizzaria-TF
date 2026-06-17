#!/bin/bash

echo "=========================================="
echo "  Compilando todos os microserviços"
echo "=========================================="
echo ""

# Naming Server
echo "Compilando Naming Server..."
cd naming-server
./mvnw clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "Naming Server compilado com sucesso"
else
    echo "Erro ao compilar Naming Server"
    exit 1
fi
cd ..

echo ""

# API Gateway
echo "Compilando API Gateway..."
cd api-gateway
./mvnw clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "API Gateway compilado com sucesso"
else
    echo "Erro ao compilar API Gateway"
    exit 1
fi
cd ..

echo ""

# TelePizza
echo "Compilando TelePizza..."
cd telepizza
./mvnw clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "TelePizza compilado com sucesso"
else
    echo "Erro ao compilar TelePizza"
    exit 1
fi
cd ..

echo ""

# Delivery Service
echo "Compilando Delivery Service..."
cd delivery-service
./mvnw clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "Delivery Service compilado com sucesso"
else
    echo "Erro ao compilar Delivery Service"
    exit 1
fi
cd ..

echo ""
echo "=========================================="
echo "  Serviços compilados"
echo "=========================================="
echo ""
echo "Agora rodar o comando:"
echo "  docker compose up --scale delivery-service=3"
echo ""
