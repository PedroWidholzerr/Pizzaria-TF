# Dockerfile para o Microserviço de Estoque
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar Maven Wrapper e arquivos de configuração do microserviço de estoque
COPY estoque-microservice/.mvn/ .mvn/
COPY estoque-microservice/mvnw estoque-microservice/pom.xml ./

# Ajustar permissões do mvnw
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Baixar dependências
RUN ./mvnw dependency:go-offline

# Copiar código fonte
COPY estoque-microservice/src src

# Compilar aplicação
RUN ./mvnw clean package -DskipTests

# Estágio final - imagem mínima
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar curl para healthcheck
RUN apk add --no-cache curl

# Copiar JAR da aplicação
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8081

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod

# Executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
