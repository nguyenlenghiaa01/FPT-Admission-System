# Use official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy correct jar (case-sensitive match)
COPY target/monitor-service-0.0.1-SNAPSHOT.jar app.jar

# Optional: install curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Expose correct port
EXPOSE 9000

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
