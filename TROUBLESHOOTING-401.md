# 🔧 Guide de Dépannage - Erreur 401 Authentification

## 🎯 Le Problème
```
❌ API Error: POST /personnels/organisation/list 
Status: 401 - Request failed with status code 401
```

**Cela signifie**: Le backend a rejeté la requête parce que le token d'authentification est manquant, invalide, ou expiré.

---

## 🔍 Étape 1: Vérifier le Token en localStorage

### Via le Console Navigateur (F12 → Application → localStorage)

1. Ouvrez le navigateur → **F12**
2. Allez à l'onglet **"Application"** (ou "Storage")
3. Cliquez sur **"Local Storage"** → sélectionnez le domaine
4. Cherchez ces clés:
   - ✅ `access_token` (devrait être présente)
   - ✅ `refresh_token` (devrait être présente)
   - ✅ `user` (JSON de l'utilisateur)

### Via la Console JavaScript

Tapez dans la console:
```javascript
// Charger les utilitaires de débogage
import('http://localhost:7153/src/utils/debug-auth.ts').then(m => {
  m.debugToken()
})

// Ou directement:
console.log('Access Token:', localStorage.getItem('access_token'))
console.log('Refresh Token:', localStorage.getItem('refresh_token'))
console.log('User:', localStorage.getItem('user'))
```

**Résultat attendu:**
```
Access Token: eyJhbGciOiJSUzI1NiIsInR5... (long string)
Refresh Token: eyJhbGciOiJSUzI1NiIsInR5... (long string)
User: {"username":"admin","email":"..."}
```

**Si vide (MISSING)?** → Allez à **Étape 2**

---

## 🔍 Étape 2: Vérifier la Connexion Keycloak

### 1. Vérifier que Keycloak est accessible
```bash
# Dans PowerShell
Test-NetConnection -ComputerName 192.168.1.2 -Port 8080

# Résultat: TcpTestSucceeded: True ✅
```

### 2. Vérifier le répertoire realm
Visitez dans le navigateur:
```
http://192.168.1.2:8080/realms/hyban/.well-known/openid-configuration
```

**Résultat attendu:** JSON avec les URLs Keycloak

### 3. Via la Console JavaScript
```javascript
fetch('http://192.168.1.2:8080/realms/hyban/.well-known/openid-configuration')
  .then(r => r.json())
  .then(data => {
    console.log('Token Endpoint:', data.token_endpoint)
    console.log('Userinfo Endpoint:', data.userinfo_endpoint)
  })
```

---

## 🔍 Étape 3: Vérifier la Connexion

### Cas 1: Pas connecté du tout
**Symptômes:**
- localStorage est vide
- Pas de token

**Solution:**
1. Allez à la page de connexion: `http://192.168.1.2:7153/login`
2. Entrez vos identifiants (admin/admin ou autre utilisateur)
3. Cliquez sur **Connecter**
4. Vérifiez les logs dans la console (F12)

**Logs attendus:**
```
Tentative de connexion Keycloak vers: /realms/hyban/protocol/openid-connect/token
Response Keycloak status: 200
UserInfo parsed: {username: "admin", ...}
Connexion réussie - Utilisateur: {username: "admin", ...}
```

### Cas 2: Connecté mais token pas utilisé
**Symptômes:**
- localStorage a le token
- Mais encore 401 sur les requêtes

**Solution:**
Vérifiez que le token est envoyé dans le header:
```javascript
// Dans la console
fetch('/api/personnels/organisation/list', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('access_token')}`
  },
  body: JSON.stringify({})
})
  .then(r => r.json())
  .then(console.log)
  .catch(console.error)
```

**Résultat attendu:** Réponse 200, pas 401

### Cas 3: Token expiré
**Symptômes:**
- 401 error
- Token existe mais est ancien

**Solution:** 
Le système devrait automatiquement rafraîchir le token. Si ça ne fonctionne pas:
```javascript
// Force déconnexion et reconnexion
localStorage.removeItem('access_token')
localStorage.removeItem('refresh_token')
localStorage.removeItem('user')
window.location.href = '/login'
```

---

## 🔍 Étape 4: Vérifier la Configuration

### Fichiers à vérifier:

#### Frontend `.env.development`
```
VITE_API_URL=http://192.168.1.2:7200/api  ✅
VITE_KEYCLOAK_URL=http://192.168.1.2:8080 ✅
```

#### Frontend `vite.config.ts`
```typescript
proxy: {
  '/api': {
    target: 'http://192.168.1.2:7200',  // ✅ Doit être 192.168.1.2
    changeOrigin: true,
    secure: false
  }
}
```

#### Backend `application.properties`
```properties
spring.web.cors.allowed-origins=...192.168.1.2:7153...  ✅
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://192.168.1.2:8080/realms/hyban  ✅
```

---

## 🔍 Étape 5: Logs du Backend

### Vérifier les logs du backend (terminal Spring Boot)
Cherchez:
```
2024-01-21 10:30:45 INFO Keycloak authentication successful
ou
2024-01-21 10:30:45 ERROR Invalid JWT token
ou
2024-01-21 10:30:45 ERROR CORS request rejected
```

**Erreurs courantes:**
- `Invalid JWT signature` → Token invalide
- `CORS policy` → Frontend IP pas autorisée
- `Unauthorized` → Token manquant

---

## 🚀 Commandes de Débogage Complètes

### Console Navigateur - Tout vérifier
```javascript
// Importer les utilitaires de débogage (si disponibles)
// Ou exécutez individuellement:

console.group('🔐 Token Diagnostic')
console.log('Token exists:', !!localStorage.getItem('access_token'))
console.log('Token preview:', localStorage.getItem('access_token')?.substring(0, 50))
console.log('RefreshToken exists:', !!localStorage.getItem('refresh_token'))
console.log('User:', localStorage.getItem('user'))
console.groupEnd()

// Vérifier le token JWT
const token = localStorage.getItem('access_token')
if (token) {
  const payload = JSON.parse(atob(token.split('.')[1]))
  console.log('JWT Payload:', payload)
  console.log('Token expires at:', new Date(payload.exp * 1000))
}

// Tester une requête API avec le token
fetch('/api/personnels/organisation/list', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('access_token')}`
  },
  body: '{}'
})
.then(r => {
  console.log('Response status:', r.status)
  return r.json()
})
.then(console.log)
.catch(console.error)
```

---

## ✅ Checklist de Résolution

- [ ] Backend tourne et accessible (http://192.168.1.2:7200)
- [ ] Keycloak tourne et accessible (http://192.168.1.2:8080)
- [ ] Utilisateur connecté (token dans localStorage)
- [ ] Token valide (pas expiré)
- [ ] Frontend envoie Authorization header
- [ ] Backend autorise CORS pour 192.168.1.2:7153
- [ ] Les IPs/ports sont cohérents partout

---

## 🆘 Si Rien ne Fonctionne

### Étape 1: Redémarrer le Backend
```bash
cd backend_rhpaie
# Arrêter le processus actuel (Ctrl+C)
# Redémarrer:
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

### Étape 2: Vider le Cache & Réconnexion
```javascript
// Dans la console du navigateur
localStorage.clear()
sessionStorage.clear()
window.location.reload()
// Puis reconnectez-vous
```

### Étape 3: Vérifier les Logs
- Backend: Cherchez `ERROR` ou `Exception` dans la console
- Keycloak: `http://192.168.1.2:8080/admin/master/console/` (logs)
- Frontend: Console du navigateur (F12)

### Étape 4: Vérifier la Réponse du Backend
```javascript
// Faire une requête simple sans authentification
fetch('/api/health')
  .then(r => r.text())
  .then(console.log)
  .catch(console.error)
```

Si ça répond 200, le backend tourne bien.

---

## 📞 Information de Contact

- **Backend API**: http://192.168.1.2:7200
- **Frontend**: http://192.168.1.2:7153
- **Keycloak Admin**: http://192.168.1.2:8080/admin
- **Keycloak Realm Config**: http://192.168.1.2:8080/realms/hyban/.well-known/openid-configuration
