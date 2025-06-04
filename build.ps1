# Danh sách tên các service
$services = @(
    "user-service",
    "authentication-service",
    "candidate-service",
    "api-gateway-service",
    "config-service",
    "discovery-service",
    "notification-service"
)

# Lưu thư mục hiện tại
$root = Get-Location

foreach ($service in $services) {
    Write-Host "Building $service ..." -ForegroundColor Cyan
    Set-Location $service

    # Build bằng Maven
    mvn clean package -DskipTests

    # Quay lại thư mục gốc
    Set-Location $root
    Write-Host "$service built." -ForegroundColor Green
}

Write-Host "All services built!" -ForegroundColor Yellow
