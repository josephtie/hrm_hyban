#!/bin/bash
# Script de diagnostic pour vérifier l'harmonisation Frontend-Backend

echo "🔍 DIAGNOSTIC API HARMONISATION"
echo "================================"
echo ""

# Définir les variables
BACKEND_IP="192.168.1.7"
BACKEND_PORT="7200"
FRONTEND_PORT="7153"
KEYCLOAK_PORT="8080"

echo "1. ✅ Vérification de la Connectivité"
echo "-----------------------------------"
echo "Tentative de ping vers $BACKEND_IP..."

if ping -c 1 $BACKEND_IP &> /dev/null; then
    echo "✅ Connectivité réseau OK"
else
    echo "❌ Impossible de pinger $BACKEND_IP"
    echo "   Vérifiez la connexion réseau!"
fi
echo ""

echo "2. ✅ Vérification des Ports"
echo "----------------------------"

# Vérifier Backend
echo "Backend (port $BACKEND_PORT)..."
if nc -z $BACKEND_IP $BACKEND_PORT 2>/dev/null; then
    echo "✅ Backend accessible sur $BACKEND_IP:$BACKEND_PORT"
else
    echo "❌ Backend NOT accessible sur $BACKEND_IP:$BACKEND_PORT"
fi

# Vérifier Frontend (seulement si on est en dev)
echo "Frontend (port $FRONTEND_PORT)..."
if nc -z localhost $FRONTEND_PORT 2>/dev/null; then
    echo "✅ Frontend accessible sur localhost:$FRONTEND_PORT"
else
    echo "⚠️ Frontend NOT accessible (normal si non lancé)"
fi

# Vérifier Keycloak
echo "Keycloak (port $KEYCLOAK_PORT)..."
if nc -z $BACKEND_IP $KEYCLOAK_PORT 2>/dev/null; then
    echo "✅ Keycloak accessible sur $BACKEND_IP:$KEYCLOAK_PORT"
else
    echo "❌ Keycloak NOT accessible sur $BACKEND_IP:$KEYCLOAK_PORT"
fi
echo ""

echo "3. ✅ Vérification des URLs"
echo "----------------------------"

# Vérifier l'API Backend
echo "Tentative de GET http://$BACKEND_IP:$BACKEND_PORT/api/health..."
response=$(curl -s -o /dev/null -w "%{http_code}" "http://$BACKEND_IP:$BACKEND_PORT/api/health" 2>/dev/null || echo "000")
if [ "$response" != "000" ]; then
    echo "✅ API Backend répond (HTTP $response)"
else
    echo "❌ API Backend ne répond pas"
fi

# Vérifier Keycloak
echo "Tentative de GET http://$BACKEND_IP:$KEYCLOAK_PORT/realms/hyban/.well-known/openid-configuration..."
response=$(curl -s -o /dev/null -w "%{http_code}" "http://$BACKEND_IP:$KEYCLOAK_PORT/realms/hyban/.well-known/openid-configuration" 2>/dev/null || echo "000")
if [ "$response" = "200" ]; then
    echo "✅ Keycloak répond correctement"
elif [ "$response" != "000" ]; then
    echo "⚠️ Keycloak répond (HTTP $response)"
else
    echo "❌ Keycloak ne répond pas"
fi
echo ""

echo "4. ✅ Vérification des Fichiers de Configuration"
echo "-----------------------------------------------"

# Vérifier .env.development
if [ -f ".env.development" ]; then
    api_url=$(grep "VITE_API_URL" .env.development | cut -d'=' -f2)
    if [ "$api_url" == "http://192.168.1.7:7200/api" ]; then
        echo "✅ .env.development: VITE_API_URL correct"
    else
        echo "❌ .env.development: VITE_API_URL = $api_url (devrait être http://192.168.1.7:7200/api)"
    fi
else
    echo "❌ .env.development non trouvé"
fi

# Vérifier application.properties
if [ -f "application.properties" ]; then
    cors=$(grep "spring.web.cors.allowed-origins" application.properties | grep "192.168.1.7:7153")
    if [ -n "$cors" ]; then
        echo "✅ application.properties: CORS inclut 192.168.1.7:7153"
    else
        echo "❌ application.properties: CORS ne contient pas 192.168.1.7:7153"
    fi
else
    echo "⚠️ application.properties non trouvé dans le répertoire courant"
fi
echo ""

echo "5. ✅ Résumé Configuration"
echo "------------------------"
echo "Backend API:  http://$BACKEND_IP:$BACKEND_PORT/api"
echo "Frontend Dev: http://localhost:$FRONTEND_PORT"
echo "Keycloak:    http://$BACKEND_IP:$KEYCLOAK_PORT"
echo ""

echo "🎯 Pour démarrer l'application:"
echo "1. Backend:  mvn spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=local'"
echo "2. Frontend: npm run dev (depuis vue3rhpaie-app)"
echo ""
