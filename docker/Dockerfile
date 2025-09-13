# Dockerfile básico para el servicio
FROM openjdk:17-jdk-slim
COPY target/ecommerce-order-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

