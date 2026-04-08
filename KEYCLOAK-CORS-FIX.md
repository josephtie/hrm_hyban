# Correction CORS Keycloak - Erreur Cross-Origin

## 🚨 Problème identifié
```
CORS Missing Allow Origin
Access-Control-Allow-Origin header is missing
```

Le frontend reçoit le token (status 200) mais le navigateur bloque la réponse à cause de CORS.

## 🔍 Cause
Keycloak n'autorise pas les requêtes cross-origin depuis `http://localhost:7153` vers `http://192.168.1.11:8080`.

## 🛠️ Solution : Configurer CORS dans Keycloak

### Étape 1 : Accéder à la configuration Keycloak

1. **Console d'administration** :
   ```
   http://192.168.1.11:8080/admin
   ```

2. **Naviguer** :
   ```
   Realm Settings → Security Defenses → CORS
   ```

### Étape 2 : Configurer les origines autorisées

Dans la section **CORS**, configurez :

```
✅ Enable CORS: ON

Allowed Origins:
- http://localhost:7153
- http://192.168.1.11:7153
- http://127.0.0.1:7153

Allowed Methods:
- GET
- POST
- PUT
- DELETE
- OPTIONS
- PATCH

Allowed Headers:
- *
- Authorization
- Content-Type
- X-Requested-With

Max Age: 3600
```

### Étape 3 : Configurer le client `hrm_frontend`

1. **Clients → hrm_frontend → Settings**
2. **Web Origins** :
   ```
   *
   ```
   ou plus restrictif :
   ```
   http://localhost:7153
   http://192.168.1.11:7153
   ```

3. **Valid Redirect URIs** :
   ```
   http://localhost:7153/*
   http://192.168.1.11:7153/*
   ```

### Étape 4 : Alternative - Configuration via CLI

Si l'interface ne fonctionne pas, utilisez la CLI Keycloak :

```bash
# Activer CORS sur le realm hyban
kcadm.sh update realms/hyban -s enabled=true -s attributes.cors=true

# Ajouter les origines autorisées
kcadm.sh update realms/hyban -s attributes.cors.allowedOrigins='["http://localhost:7153","http://192.168.1.11:7153"]'
```

## 🧪 Test après configuration

### Test 1 : Vérifier la configuration CORS
```bash
curl -H "Origin: http://localhost:7153" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: Content-Type" \
     -X OPTIONS \
     http://192.168.1.11:8080/realms/hyban/protocol/openid-connect/token
```

La réponse doit inclure :
```
Access-Control-Allow-Origin: http://localhost:7153
Access-Control-Allow-Methods: POST, GET, OPTIONS, etc.
```

### Test 2 : Test de connexion
Rafraîchissez le frontend et testez la connexion à nouveau.

## 🔄 Alternative : Utiliser un proxy

Si CORS persiste, utilisez le proxy Vite déjà configuré :

1. **Modifiez keycloak-auth.service.ts** :
   ```typescript
   const keycloakConfig = {
     tokenUrl: '/realms/hyban/protocol/openid-connect/token', // Utiliser le proxy
     clientId: 'hrm_frontend',
     grantType: 'password',
     scope: 'openid profile email'
   }
   ```

2. **Le proxy dans vite.config.ts** gérera déjà CORS** :
   ```typescript
   '/realms': {
     target: 'http://192.168.1.11:8080',
     changeOrigin: true,
     secure: false
   }
   ```

## ✅ Étapes de vérification

1. **Activer CORS dans Realm Settings**
2. **Configurer Web Origins sur le client**
3. **Vérifier les Valid Redirect URIs**
4. **Tester avec curl**
5. **Redémarrer le frontend**

## 🎯 Solution recommandée

**Activer CORS dans Keycloak** plutôt que d'utiliser le proxy :
- Plus propre
- Moins de configuration
- Compatible avec la production

Après configuration CORS, le frontend pourra récupérer le token sans erreur !
