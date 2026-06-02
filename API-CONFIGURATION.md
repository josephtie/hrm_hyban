# 🎯 Configuration Harmonisée Frontend-Backend

## IP et Ports Standardisés

| Service | Host | Port | URL |
|---------|------|------|-----|
| **Backend API** | 192.168.1.2 | 7200 | http://192.168.1.2:7200 |
| **Frontend Dev** | 192.168.1.2 | 7153 | http://192.168.1.2:7153 |
| **Keycloak** | 192.168.1.2 | 8080 | http://192.168.1.2:8080 |
| **PostgreSQL** | localhost | 5432 | jdbc:postgresql://localhost:5432 |

## 📋 Configuration Files

### Frontend - Variables d'Environnement

#### `.env.development` (Développement local)
```
VITE_API_URL=http://192.168.1.2:7200/api
VITE_KEYCLOAK_URL=http://192.168.1.2:8080
```

#### `.env.docker` (Docker local)
```
VITE_API_URL=http://192.168.1.2:7200/api
VITE_KEYCLOAK_URL=http://192.168.1.2:8080
```

#### `vite.config.ts` (Proxy)
```typescript
server: {
  port: 7153,
  proxy: {
    '/api': {
      target: 'http://192.168.1.2:7200',
      changeOrigin: true,
      secure: false
    },
    '/realms': {
      target: 'http://192.168.1.2:8080',
      changeOrigin: true,
      secure: false
    }
  }
}
```

### Backend - Spring Boot Configuration

#### `application.properties` (Production)
```properties
server.port=7200

# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:7153,http://127.0.0.1:7153,http://192.168.1.2:7153,http://192.168.1.2:8080,http://127.0.0.1:3000,http://localhost:7200,http://127.0.0.1:7200,http://192.168.1.2:7200
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
spring.web.cors.max-age=3600
```

#### `application-dev.properties` (Développement)
```properties
server.port=7100

# CORS for Swagger
spring.web.cors.allowed-origins=http://localhost:7100,http://192.168.1.2:7100,http://localhost:7200,http://192.168.1.2:7200,http://localhost:4200,http://127.0.0.1:3000
```

#### `application-local.properties` (Local)
```properties
server.port=7200

# CORS
spring.web.cors.allowed-origins=http://localhost:7153,http://192.168.1.7:7153,http://localhost:7200,http://localhost:4200,http://127.0.0.1:3000

# Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://192.168.1.7:8080/realms/hyban
```

#### `application-prod.properties` (Production)
```properties
# Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://192.168.1.2:8080/realms/hyban
keycloak.admin.host=http://192.168.1.2:8080
```

## 🔧 Vérification des Configurations

### Frontend
```bash
# Vérifier VITE_API_URL
echo $env:VITE_API_URL  # PowerShell
# Devrait afficher: http://192.168.1.2:7200/api
```

### Backend
```bash
# Lors du démarrage, vérifier les logs
# Devrait avoir:
# - Server running on 192.168.1.2:7200
# - CORS origins configured
```

## 🐛 Troubleshooting

### Erreur CORS
```
Cross-Origin Request Blocked: The Same Origin Policy disallows reading the remote resource
```
**Solution**: Vérifier que l'IP du frontend est dans `spring.web.cors.allowed-origins`

### Erreur de Connexion API
```
Network Error: GET http://192.168.1.7:7200/api/...
```
**Solution**: 
# Vérifier la connectivité réseau: `ping 192.168.1.2`
# Vérifier que le backend tourne: `netstat -an | grep 7200`
# Vérifier le VITE_API_URL dans `.env.development`

### Token non envoyé
```
❌ Aucun token trouvé
```
**Solution**:
1. Vérifier Keycloak: http://192.168.1.2:8080
2. Vérifier que le token est stocké dans `localStorage`
3. Vérifier l'authentification Keycloak

## 📚 Endpoints API Standards

Tous les endpoints doivent suivre le format REST:

```
GET    /api/{resource}              # Lister tous
POST   /api/{resource}              # Créer
GET    /api/{resource}/{id}         # Récupérer un
PUT    /api/{resource}/{id}         # Modifier
DELETE /api/{resource}/{id}         # Supprimer
```

## 🚀 Démarrage des Services

### Backend
```bash
cd backend_rhpaie
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
# Accès: http://192.168.1.7:7200/api
```

### Frontend
```bash
cd vue3rhpaie-app
npm install
npm run dev
# Accès: http://192.168.1.7:7153
```

### Keycloak
```bash
# Vérifier sur http://192.168.1.7:8080
```

## ✅ Checklist de Vérification

- [ ] Fichiers `.env` utilisent `192.168.1.7`
- [ ] `application.properties` CORS inclut `192.168.1.7:7153`
- [ ] `vite.config.ts` proxy pointe vers `192.168.1.7:7200`
- [ ] Keycloak accessible sur `192.168.1.7:8080`
- [ ] Ping vers `192.168.1.7` fonctionne
- [ ] Backend répond sur port `7200`
- [ ] Frontend démarre sur port `7153`
- [ ] Token Keycloak généré et stocké
