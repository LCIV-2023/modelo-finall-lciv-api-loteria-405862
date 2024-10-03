# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine
# Copia el archivo JAR de tu aplicación al contenedor
COPY target/lciii-scaffolding-0.0.1-SNAPSHOT.jar app.jar

# Define el comando para ejecutar tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]



