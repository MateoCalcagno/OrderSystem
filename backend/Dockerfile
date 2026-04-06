# Usa una imagen de OpenJDK 21
FROM openjdk:21-jdk-slim

# Directorio de trabajo
WORKDIR /app

# Copia pom.xml y mvnw
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Da permisos al wrapper
RUN chmod +x mvnw

# Instala dependencias sin compilar aún
RUN ./mvnw dependency:resolve

# Copia el código
COPY src ./src

# Compila el proyecto (skip tests si querés)
RUN ./mvnw clean install -DskipTests

# Expone el puerto que usa Spring Boot
EXPOSE 8080

# Comando por defecto para ejecutar
CMD ["./mvnw", "spring-boot:run"]