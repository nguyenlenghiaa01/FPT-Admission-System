# Use official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy jar file to container
COPY target/websocket-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port app chạy (8082 trong file config)
EXPOSE 9001

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]