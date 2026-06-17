# Dockerfile para o Serviço Principal
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar Maven Wrapper e pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Baixar dependências
RUN ./mvnw dependency:go-offline

# Copiar código fonte
COPY src src

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
EXPOSE 8080

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV ESTOQUE_MICROSERVICE_URL=http://estoque-service:8081

# Executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
