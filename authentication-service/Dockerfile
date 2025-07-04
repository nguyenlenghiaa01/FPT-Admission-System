# Use official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim

# Cài redis-cli và curl ngay trong image
RUN apt-get update \
 && apt-get install -y redis-tools curl \
 && rm -rf /var/lib/apt/lists/*

# Set working directory in container
WORKDIR /app

# Copy jar file to container
COPY target/authenservice-0.0.1-SNAPSHOT.jar app.jar

# Expose port app chạy (8082 trong file config)
EXPOSE 8083

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
