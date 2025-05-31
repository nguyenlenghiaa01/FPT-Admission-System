# Use official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy jar file to container
COPY target/DiscoveryService-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Expose port app chạy (8082 trong file config)
EXPOSE 9099

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
