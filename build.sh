#!/bin/bash

# Danh sách tên các service (tên folder)
services=(
    user-service
    authentication-service
    candidate-service
    api-gateway-service
    config-service
    discovery-service
    notification-service
)

# Lưu thư mục gốc
root=$(pwd)

for service in "${services[@]}"
do
    echo "==== Building $service ===="
    cd "$service" || { echo "Cannot cd into $service"; exit 1; }
    mvn clean package -DskipTests
    cd "$root"
    echo "==== $service built ===="
done

echo "All services built!"
