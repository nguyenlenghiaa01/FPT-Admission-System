# clean.ps1
# Danh sách tên thư mục các microservice
$services = @(
        "user-service",
        "authentication-service",
        "candidate-service",
        "api-gateway-service",
        "config-service",
        "discovery-service",
        "notification-service",
        "candidate-service",
        "program-service",
        "consultant-service",
        "blog-service",
        "websocket-service"
)

# Lặp qua từng service để xóa thư mục target
foreach ($service in $services) {
    Write-Host "🧹 Cleaning $service..."

    $servicePath = "./$service"

    if (Test-Path $servicePath) {
        Set-Location $servicePath

        if (Test-Path "target") {
            Remove-Item -Recurse -Force "target"
            Write-Host "✅ Removed 'target' in $service"
        } else {
            Write-Host "⚠️ No 'target' found in $service"
        }

        Set-Location ".."
    } else {
        Write-Host "❌ Folder $service does not exist."
    }

    Write-Host ""
}

Write-Host "🎉 Done cleaning all microservices."
