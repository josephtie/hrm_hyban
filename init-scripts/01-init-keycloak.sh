#!/bin/bash

# Script d'initialisation pour Keycloak
# Ce script crée le realm hyban et les clients nécessaires

set -e

echo "Initialisation de Keycloak..."

# Attendre que Keycloak soit prêt
until curl -f http://keycloak:8080/health/ready > /dev/null 2>&1; do
    echo "En attente de Keycloak..."
    sleep 5
done

echo "Keycloak est prêt, création du realm hyban..."

# Obtenir le token d'admin
ADMIN_TOKEN=$(curl -s -X POST "http://keycloak:8080/realms/master/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=admin&password=admin&grant_type=password&client_id=admin-cli" | \
    jq -r '.access_token')

if [ -z "$ADMIN_TOKEN" ]; then
    echo "Erreur: Impossible d'obtenir le token d'admin"
    exit 1
fi

# Créer le realm hyban
curl -s -X POST "http://keycloak:8080/admin/realms" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "realm": "hyban",
        "displayName": "Hyban RH & Paie",
        "enabled": true,
        "registrationAllowed": true,
        "loginWithEmailAllowed": true,
        "duplicateEmailsAllowed": false,
        "resetPasswordAllowed": true,
        "editUsernameAllowed": true,
        "bruteForceProtected": true
    }' || echo "Le realm hyban existe déjà"

# Créer le client hrm_frontend
curl -s -X POST "http://keycloak:8080/admin/realms/hyban/clients" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "clientId": "hrm_frontend",
        "name": "HRM Frontend",
        "description": "Client pour l''application frontend RHPAIE",
        "enabled": true,
        "clientAuthenticatorType": "client-secret",
        "secret": "b6cFLwyL2MakdzHxomjsamxesop9IbIE",
        "redirectUris": ["http://localhost:7153/*", "http://localhost:80/*"],
        "webOrigins": ["http://localhost:7153", "http://localhost:80"],
        "publicClient": false,
        "standardFlowEnabled": true,
        "directAccessGrantsEnabled": true,
        "serviceAccountsEnabled": true,
        "authorizationServicesEnabled": false,
        "fullScopeAllowed": false
    }' || echo "Le client hrm_frontend existe déjà"

# Créer le client keycloak-admin-client
curl -s -X POST "http://keycloak:8080/admin/realms/hyban/clients" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "clientId": "keycloak-admin-client",
        "name": "Keycloak Admin Client",
        "description": "Client admin pour la gestion des utilisateurs",
        "enabled": true,
        "clientAuthenticatorType": "client-secret",
        "secret": "b6cFLwyL2MakdzHxomjsamxesop9IbIE",
        "publicClient": false,
        "standardFlowEnabled": false,
        "directAccessGrantsEnabled": false,
        "serviceAccountsEnabled": true,
        "authorizationServicesEnabled": true,
        "fullScopeAllowed": true
    }' || echo "Le client keycloak-admin-client existe déjà"

echo "Initialisation de Keycloak terminée!"
