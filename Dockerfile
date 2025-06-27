# Etapa 1: build de la aplicación
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar pom y resolver dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar proyecto
COPY src ./src

# Compilar y empacar el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: imagen final
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=builder /app/target/deliveryorder-manager*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
