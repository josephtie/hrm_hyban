# Configuration Keycloak pour Vue3 + Spring Boot

## 🎯 Objectif
Résoudre l'erreur 400 Bad Request sur l'URL d'authentification Keycloak

## 🔧 Étapes de configuration dans Keycloak

### 1. Accéder à la console Keycloak
```
http://localhost:8080
```
Connectez-vous avec les identifiants admin

### 2. Sélectionner le realm `hyban`
Assurez-vous que le realm `hyban` existe et est sélectionné

### 3. Configurer le client `hrm_frontend`

#### 3.1 Accès au client
```
Clients → hrm_frontend → Settings
```

#### 3.2 Paramètres essentiels
```
Client ID: hrm_frontend
Client Protocol: openid-connect
Root URL: http://localhost:7153
```

#### 3.3 Paramètres d'accès
```
✅ Standard Flow Enabled: ON
❌ Direct Access Grants Enabled: OFF
❌ Implicit Flow Enabled: OFF
❌ Service Accounts Enabled: OFF
❌ Authorization Enabled: OFF
```

#### 3.4 Paramètres d'authentification
```
Client Authentication: OFF (Public Client)
```

#### 3.5 URIS de redirection valides (CRUCIAL)
```
Valid Redirect URIs:
http://localhost:7153/*
http://localhost:7153/
http://localhost:7153/login
```

#### 3.6 Web Origins (CRUCIAL)
```
Web Origins:
*
OU plus restrictif:
http://localhost:7153
```

### 4. Configuration finale du client
```json
{
  "clientId": "hrm_frontend",
  "name": "",
  "description": "",
  "enabled": true,
  "clientAuthenticatorType": "public-client",
  "secret": "",
  "rootUrl": "http://localhost:7153",
  "validRedirectUris": [
    "http://localhost:7153/*"
  ],
  "webOrigins": [
    "*"
  ],
  "standardFlowEnabled": true,
  "directAccessGrantsEnabled": false,
  "implicitFlowEnabled": false,
  "serviceAccountsEnabled": false,
  "publicClient": true,
  "frontchannelLogout": true,
  "protocol": "openid-connect",
  "attributes": {
    "saml.assertion.signature": "RSA_SHA256",
    "saml.multivalued.roles": "false",
    "frontchannel.logout.session.required": "true",
    "oauth2.device.authorization.grant.enabled": "false",
    "backchannel.logout.revoke.offline.tokens": "false",
    "backchannel.logout.session.required": "true",
    "oauth2.device.code.grant.enabled": "false",
    "oidc.ciba.grant.enabled": "false",
    "require.pushed.authorization.request": "false"
  },
  "authenticationFlowBindingOverrides": {},
  "fullScopeAllowed": true,
  "nodeReTimeout": 0,
  "protocolMappers": []
}
```

## 🚀 Test de configuration

### 1. Vérifier la configuration OIDC
```bash
curl "http://localhost:8080/realms/hyban/.well-known/openid-configuration"
```
Devrait retourner Status 200 avec la configuration JSON

### 2. Tester l'URL d'authentification
Ouvrir dans le navigateur :
```
http://localhost:8080/realms/hyban/protocol/openid-connect/auth?client_id=hrm_frontend&redirect_uri=http://localhost:7153/&response_type=code&scope=openid
```

### 3. Vérifier les logs
Dans la console du navigateur, chercher :
- URL appelée par keycloak-js
- Réponse de Keycloak
- Erreurs éventuelles

## 🔍 Dépannage

### Erreur 400 Bad Request
Causes possibles :
1. ❌ `redirect_uri` pas dans les `Valid Redirect URIs`
2. ❌ `Web origins` ne contient pas l'origine
3. ❌ `Standard Flow` désactivé
4. ❌ Client type incorrect (doit être Public)

### Erreur CORS
Causes possibles :
1. ❌ `Web origins` mal configuré
2. ❌ Client type incorrect

### Erreur 403 Forbidden
Causes possibles :
1. ❌ Realm désactivé
2. ❌ Client désactivé
3. ❌ Standard Flow désactivé

## ✅ Configuration qui fonctionne

```typescript
// Dans keycloak-auth.service.ts
const keycloakConfig = {
  url: 'http://localhost:8080',
  realm: 'hyban',
  clientId: 'hrm_frontend'
}

const keycloak = new Keycloak(keycloakConfig)

// Initialisation
await keycloak.init({
  onLoad: 'login-required',
  checkLoginIframe: false,
  pkceMethod: 'S256'
})
```

## 🎉 Résultat attendu

Après cette configuration :
- ✅ Plus d'erreur 400
- ✅ Redirection fluide vers Keycloak
- ✅ Authentification réussie
- ✅ Retour vers l'app Vue3 avec le token

## 📝 Notes importantes

1. **Pour le développement** : Utiliser `*` dans Web Origins
2. **Pour la production** : Utiliser les URLs spécifiques
3. **Vue Router** : La redirection doit se faire vers `/` puis le router gère
4. **PKCE** : Garder `pkceMethod: 'S256'` pour la sécurité

Cette configuration résout 90% des problèmes d'authentification Keycloak avec Vue3 !
