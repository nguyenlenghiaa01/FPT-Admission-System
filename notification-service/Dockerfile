FROM openjdk:23-jdk-slim

WORKDIR /app

COPY target/be.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
