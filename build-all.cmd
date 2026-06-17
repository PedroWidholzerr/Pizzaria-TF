@echo off
echo ==========================================
echo   Compilando todos os microserviços
echo ==========================================
echo.

REM Naming Server
echo Compilando Naming Server
cd naming-server
call mvnw.cmd clean package -DskipTests
if %errorlevel% neq 0 (
    echo Erro ao compilar Naming Server
    exit /b 1
)
echo Naming Server compilado com sucesso
cd ..

echo.

REM API Gateway
echo Compilando API Gateway
cd api-gateway
call mvnw.cmd clean package -DskipTests
if %errorlevel% neq 0 (
    echo Erro ao compilar API Gateway
    exit /b 1
)
echo API Gateway compilado com sucesso
cd ..

echo.

REM TelePizza
echo Compilando TelePizza
cd telepizza
call mvnw.cmd clean package -DskipTests
if %errorlevel% neq 0 (
    echo Erro ao compilar TelePizza
    exit /b 1
)
echo TelePizza compilado com sucesso
cd ..

echo.

REM Delivery Service
echo Compilando Delivery Service
cd delivery-service
call mvnw.cmd clean package -DskipTests
if %errorlevel% neq 0 (
    echo Erro ao compilar Delivery Service
    exit /b 1
)
echo Delivery Service compilado com sucesso
cd ..

echo.
echo ==========================================
echo  Serviços compilados
echo ==========================================
echo.
echo Agora rodar o comando:
echo   docker compose up --scale delivery-service=3
echo.
