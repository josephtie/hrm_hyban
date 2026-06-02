# Script de diagnostic pour vérifier l'harmonisation Frontend-Backend (Windows PowerShell)

Write-Host "🔍 DIAGNOSTIC API HARMONISATION" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Définir les variables
$BACKEND_IP = "192.168.1.7"
$BACKEND_PORT = 7200
$FRONTEND_PORT = 7153
$KEYCLOAK_PORT = 8080

Write-Host "1. ✅ Vérification de la Connectivité" -ForegroundColor Green
Write-Host "-----------------------------------" -ForegroundColor Green
Write-Host "Tentative de ping vers $BACKEND_IP..."

$ping = Test-Connection -ComputerName $BACKEND_IP -Count 1 -Quiet
if ($ping) {
    Write-Host "✅ Connectivité réseau OK" -ForegroundColor Green
} else {
    Write-Host "❌ Impossible de pinger $BACKEND_IP" -ForegroundColor Red
    Write-Host "   Vérifiez la connexion réseau!" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "2. ✅ Vérification des Ports" -ForegroundColor Green
Write-Host "----------------------------" -ForegroundColor Green

# Vérifier Backend
Write-Host "Backend (port $BACKEND_PORT)..."
$backend = Test-NetConnection -ComputerName $BACKEND_IP -Port $BACKEND_PORT -WarningAction SilentlyContinue
if ($backend.TcpTestSucceeded) {
    Write-Host "✅ Backend accessible sur ${BACKEND_IP}:${BACKEND_PORT}" -ForegroundColor Green
} else {
    Write-Host "❌ Backend NOT accessible sur ${BACKEND_IP}:${BACKEND_PORT}" -ForegroundColor Red
}

# Vérifier Frontend
Write-Host "Frontend (port $FRONTEND_PORT)..."
$frontend = Test-NetConnection -ComputerName localhost -Port $FRONTEND_PORT -WarningAction SilentlyContinue
if ($frontend.TcpTestSucceeded) {
    Write-Host "✅ Frontend accessible sur localhost:$FRONTEND_PORT" -ForegroundColor Green
} else {
    Write-Host "⚠️ Frontend NOT accessible (normal si non lancé)" -ForegroundColor Yellow
}

# Vérifier Keycloak
Write-Host "Keycloak (port $KEYCLOAK_PORT)..."
$keycloak = Test-NetConnection -ComputerName $BACKEND_IP -Port $KEYCLOAK_PORT -WarningAction SilentlyContinue
if ($keycloak.TcpTestSucceeded) {
    Write-Host "✅ Keycloak accessible sur ${BACKEND_IP}:${KEYCLOAK_PORT}" -ForegroundColor Green
} else {
    Write-Host "❌ Keycloak NOT accessible sur ${BACKEND_IP}:${KEYCLOAK_PORT}" -ForegroundColor Red
}
Write-Host ""

Write-Host "3. ✅ Vérification des URLs" -ForegroundColor Green
Write-Host "----------------------------" -ForegroundColor Green

# Vérifier l'API Backend
Write-Host "Tentative de GET http://$BACKEND_IP:$BACKEND_PORT/api/health..."
try {
    $response = Invoke-WebRequest -Uri "http://$BACKEND_IP:$BACKEND_PORT/api/health" -Method Get -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ API Backend répond (HTTP $($response.StatusCode))" -ForegroundColor Green
    } else {
        Write-Host "⚠️ API Backend répond (HTTP $($response.StatusCode))" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ API Backend ne répond pas" -ForegroundColor Red
    Write-Host "   Erreur: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Vérifier Keycloak
Write-Host "Tentative de GET http://$BACKEND_IP:$KEYCLOAK_PORT/realms/hyban/.well-known/openid-configuration..."
try {
    $response = Invoke-WebRequest -Uri "http://$BACKEND_IP:$KEYCLOAK_PORT/realms/hyban/.well-known/openid-configuration" -Method Get -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Keycloak répond correctement (HTTP $($response.StatusCode))" -ForegroundColor Green
    } else {
        Write-Host "⚠️ Keycloak répond (HTTP $($response.StatusCode))" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Keycloak ne répond pas" -ForegroundColor Red
}
Write-Host ""

Write-Host "4. ✅ Vérification des Fichiers de Configuration" -ForegroundColor Green
Write-Host "-----------------------------------------------" -ForegroundColor Green

# Vérifier .env.development
if (Test-Path ".env.development") {
    $envContent = Get-Content ".env.development" -Raw
    if ($envContent -match "VITE_API_URL=http://192\.168\.1\.7:7200/api") {
        Write-Host "✅ .env.development: VITE_API_URL correct" -ForegroundColor Green
    } else {
        Write-Host "❌ .env.development: VITE_API_URL incorrect" -ForegroundColor Red
        $apiUrl = $envContent | Select-String "VITE_API_URL"
        Write-Host "   Actuel: $apiUrl" -ForegroundColor Yellow
    }
} else {
    Write-Host "❌ .env.development non trouvé" -ForegroundColor Red
}

# Vérifier application.properties
if (Test-Path "application.properties") {
    $appProps = Get-Content "application.properties" -Raw
    if ($appProps -match "192\.168\.1\.7:7153") {
        Write-Host "✅ application.properties: CORS inclut 192.168.1.7:7153" -ForegroundColor Green
    } else {
        Write-Host "❌ application.properties: CORS ne contient pas 192.168.1.7:7153" -ForegroundColor Red
    }
} else {
    Write-Host "⚠️ application.properties non trouvé dans le répertoire courant" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "5. ✅ Résumé Configuration" -ForegroundColor Green
Write-Host "------------------------" -ForegroundColor Green
Write-Host "Backend API:  http://$BACKEND_IP:$BACKEND_PORT/api" -ForegroundColor Cyan
Write-Host "Frontend Dev: http://localhost:$FRONTEND_PORT" -ForegroundColor Cyan
Write-Host "Keycloak:    http://$BACKEND_IP:$KEYCLOAK_PORT" -ForegroundColor Cyan
Write-Host ""

Write-Host "🎯 Pour démarrer l'application:" -ForegroundColor Magenta
Write-Host "1. Backend:  mvn spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=local'" -ForegroundColor Yellow
Write-Host "2. Frontend: npm run dev (depuis vue3rhpaie-app)" -ForegroundColor Yellow
Write-Host ""

Write-Host "📝 Appuyez sur une touche pour fermer..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
