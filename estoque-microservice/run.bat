@echo off
@rem Script para executar o microserviço de estoque no Windows

echo ===============================================
echo   Iniciando Microservico de Estoque
echo   Porta: 8081
echo   Banco: H2 (estoquedb)
echo ===============================================
echo.

cd /d "%~dp0"
call mvnw.cmd spring-boot:run
