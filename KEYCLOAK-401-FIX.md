# Correction de l'erreur 401 Unauthorized Keycloak

## 🚨 Problème identifié
`POST http://192.168.1.11:8080/realms/hyban/protocol/openid-connect/token` → 401 Unauthorized

## 🔍 Causes possibles

### 1. Client `hrm_frontend` mal configuré
Le client est probablement configuré comme **public** mais la requête utilise **Resource Owner Password Credentials** qui nécessite :
- Soit un client **confidentiel** (avec secret)
- Soit l'activation de **Direct Access Grants** sur le client public

### 2. Direct Access Grants désactivé
Dans Keycloak, l'option `Direct Access Grants Enabled` doit être **ON** pour permettre les connexions username/password.

## 🛠️ Solutions

### Solution 1 : Activer Direct Access Grants (Recommandé)

1. **Accéder à la console Keycloak** :
   ```
   http://192.168.1.11:8080/admin
   ```

2. **Naviguer vers le client** :
   ```
   Realm: hyban → Clients → hrm_frontend
   ```

3. **Modifier les paramètres** :
   - **Settings** → **Access Settings**
   - ✅ **Direct Access Grants Enabled**: ON
   - ✅ **Standard Flow Enabled**: ON
   - **Save**

4. **Vérifier les credentials** :
   - **Credentials** → Notez le **Client Secret** si besoin

### Solution 2 : Utiliser un client confidentiel

1. **Créer un nouveau client** ou modifier `hrm_frontend` :
   - **Access Type**: **confidential**
   - **Client Authentication**: **ON**
   - **Direct Access Grants Enabled**: **ON**
   - **Standard Flow Enabled**: **ON**

2. **Récupérer le client secret** :
   ```
   Clients → hrm_frontend → Credentials → Client Secret
   ```

3. **Mettre à jour le code frontend** :

   ```typescript
   // Dans keycloak-auth.service.ts
   const keycloakConfig = {
     tokenUrl: import.meta.env.VITE_KEYCLOAK_URL + '/realms/hyban/protocol/openid-connect/token',
     clientId: 'hrm_frontend',
     clientSecret: 'votre-client-secret', // Ajouter cette ligne
     grantType: 'password',
     scope: 'openid profile email'
   }
   ```

   ```typescript
   // Dans la méthode login
   const response = await axios.post(
     keycloakConfig.tokenUrl,
     new URLSearchParams({
       client_id: keycloakConfig.clientId,
       client_secret: keycloakConfig.clientSecret, // Ajouter cette ligne
       grant_type: keycloakConfig.grantType,
       username: username,
       password: password,
       scope: keycloakConfig.scope
     }),
     {
       headers: {
         'Content-Type': 'application/x-www-form-urlencoded'
       }
     }
   )
   ```

### Solution 3 : Vérifier les URIs de redirection

Assurez-vous que dans Keycloak :
```
Valid Redirect URIs:
- http://localhost:7153/*
- http://192.168.1.11:7153/*

Web Origins:
- *
```

## 🔧 Test rapide

### Tester avec curl
```bash
curl -X POST http://192.168.1.11:8080/realms/hyban/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=hrm_frontend&grant_type=password&username=votre-user&password=votre-pass&scope=openid"
```

### Tester avec Postman
- **URL**: `http://192.168.1.11:8080/realms/hyban/protocol/openid-connect/token`
- **Method**: POST
- **Headers**: `Content-Type: application/x-www-form-urlencoded`
- **Body**: 
  ```
  client_id=hrm_frontend&grant_type=password&username=votre-user&password=votre-pass&scope=openid
  ```

## ✅ Étapes de vérification

1. **Vérifier la configuration du client** dans Keycloak
2. **Activer Direct Access Grants** si désactivé
3. **Tester avec curl** pour isoler le problème
4. **Mettre à jour le code** si nécessaire

## 🎯 Solution recommandée

**Activer Direct Access Grants** sur le client `hrm_frontend` :
- Plus simple
- Pas besoin de modifier le code
- Compatible avec l'architecture actuelle

Après modification, redémarrez le frontend et testez à nouveau !
