# Use official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy jar file to container
COPY target/authenservice-0.0.1-SNAPSHOT.jar app.jar

# Expose port app chạy (8082 trong file config)
EXPOSE 8083

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
