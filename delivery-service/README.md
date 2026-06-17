# Módulo de Entregas - Microserviço

Microserviço responsável por processar entregas de pedidos do sistema TelePizza através usando filas com RabbitMQ.

## Arquitetura

- Cada instância do serviço se registra em uma **fila única com nome aleatório**
- Todas as filas consomem do mesmo **exchange** (telepizza.delivery.exchange)
- O serviço telepizza publica mensagens no exchange
- RabbitMQ distribui as mensagens entre as instâncias disponíveis

## Como Executar

### Localmente (uma instância)
```bash
cd delivery-service
./mvnw clean package
./mvnw spring-boot:run
```

### Com Docker Compose (múltiplas instâncias)

1. Compilar todos os serviços:
```bash
cd naming-server && ./mvnw clean package && cd ..
cd api-gateway && ./mvnw clean package && cd ..
cd telepizza && ./mvnw clean package && cd ..
cd delivery-service && ./mvnw clean package && cd ..
```

2. Iniciar com 3 instâncias do delivery-service:
```bash
docker compose up --scale delivery-service=3
```

3. Para alterar o número de instâncias:
```bash
docker compose up --scale delivery-service=5
```

## Endpoints

- **Porta**: 8083 (dinâmica no Docker)
- **Health Check**: `/actuator/health`
- **Eureka**: Registrado como `delivery-service`

## Monitoramento

- **RabbitMQ Management UI**: http://localhost:15672
  - Usuário: `guest`
  - Senha: `guest`
  
- **Eureka Dashboard**: http://localhost:8761

## Logs

Os logs mostram:
- Recebimento de mensagens da fila
- Status do processamento de entrega
- Fases da entrega (preparação, em rota, entregue)
- Nome do entregador atribuído
- Tempo de processamento

## Configuração RabbitMQ

O serviço cria automaticamente:
- **Exchange**: `telepizza.delivery.exchange` (TopicExchange)
- **Fila**: `telepizza.delivery.queue.instance-XXXX` (nome único por instância)
- **Routing Key**: `delivery.request`

## Processo de Entrega Simulado

1. **Em Preparação** (2-5 segundos)
2. **Saiu para Entrega** (5-10 segundos)
3. **Entregue** (95% de sucesso) ou **Falhou** (5% de falha)