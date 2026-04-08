# Mise à jour de la configuration pour utiliser 192.168.1.2

## 🎯 Objectif
Reconfigurer le frontend et le backend pour utiliser l'adresse IP `192.168.1.2` au lieu de `192.168.1.11`.

## ✅ Modifications apportées

### 1. Frontend - Variables d'environnement

**Fichier `.env.docker`** :
```env
# Configuration pour l'environnement Docker
VITE_API_URL=http://192.168.1.2:7200/api
VITE_KEYCLOAK_URL=http://192.168.1.2:8080
```

**Fichier `.env.development`** :
```env
# Configuration pour l'environnement de développement local
VITE_API_URL=http://192.168.1.2:7200/api
VITE_KEYCLOAK_URL=http://192.168.1.2:8080
```

### 2. Frontend - Configuration Vite

**Fichier `vite.config.ts`** :
```typescript
server: {
  port: 7153,
  open: true,
  proxy: {
    '/api': {
      target: 'http://192.168.1.2:7200/api',  // ✅ Mis à jour
      changeOrigin: true,
      secure: false
    },
    '/realms': {
      target: 'http://192.168.1.2:8080',  // ✅ Mis à jour
      changeOrigin: true,
      secure: false
    }
  }
}
```

### 3. Backend - Controller d'authentification

**Fichier `AuthController.java`** :
```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
    String keycloakHost = System.getenv().getOrDefault("KEYCLOAK_ADMIN_HOST", "http://192.168.1.2:8080");  // ✅ Mis à jour
    String tokenUrl = keycloakHost + "/realms/hyban/protocol/openid-connect/token";
    // ...
}
```

### 4. Backend - Configuration CORS

**Fichier `WebSecurityConfig.java`** :
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:*",
        "http://127.0.0.1:*",
        "http://192.168.1.2:*",  // ✅ Mis à jour
        "http://frontend:*",
        "http://nginx:*"
    ));
    // ...
}
```

### 5. Backend - Configuration application-docker.properties

**Fichier `application-docker.properties`** :
```properties
# Configuration Keycloak pour Docker
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://192.168.1.2:8080/realms/hyban  # ✅ Mis à jour
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/certs

# Keycloak admin settings
keycloak.admin.host=http://192.168.1.2:8080  # ✅ Mis à jour

# Configuration CORS pour Docker
spring.web.cors.allowed-origins=http://localhost:7153,http://192.168.1.2:7153,http://frontend:80,http://nginx:80  # ✅ Mis à jour
```

### 6. Docker Compose

**Fichier `docker-compose.yml`** :
```yaml
backend:
  environment:
    SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://192.168.1.2:8080/realms/hyban  # ✅ Mis à jour
    KEYCLOAK_ADMIN_HOST: http://192.168.1.2:8080  # ✅ Mis à jour

frontend:
  environment:
    VITE_API_URL: http://192.168.1.2:7200/api  # ✅ Mis à jour
    VITE_KEYCLOAK_URL: http://192.168.1.2:8080  # ✅ Mis à jour
```

## 🌟 Récapitulatif des changements

| Fichier | Ancienne IP | Nouvelle IP | Statut |
|---------|-------------|------------|--------|
| `.env.docker` | 192.168.1.11 | 192.168.1.2 | ✅ |
| `.env.development` | 192.168.1.11 | 192.168.1.2 | ✅ |
| `vite.config.ts` | 192.168.1.11 | 192.168.1.2 | ✅ |
| `AuthController.java` | 192.168.1.11 | 192.168.1.2 | ✅ |
| `WebSecurityConfig.java` | 192.168.1.11 | 192.168.1.2 | ✅ |
| `application-docker.properties` | 192.168.1.11 | 192.168.1.2 | ✅ |
| `docker-compose.yml` | 192.168.1.11 | 192.168.1.2 | ✅ |

## 🚀 Instructions de démarrage

### 1. Développement local
```bash
# Frontend
cd vue3rhpaie-app
npm run dev

# Backend
cd ..
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### 2. Docker
```bash
# Arrêter les conteneurs existants
docker-compose down

# Démarrer avec la nouvelle configuration
docker-compose up -d --build

# Vérifier les logs
docker-compose logs -f
```

## 🔍 Vérification de la configuration

### 1. Frontend
- Accéder à `http://localhost:7153`
- Vérifier que les appels API vont vers `http://192.168.1.2:7200/api`
- Vérifier que l'authentification va vers `http://192.168.1.2:8080`

### 2. Backend
- Accéder à `http://localhost:7200` ou `http://192.168.1.2:7200`
- Vérifier les logs pour confirmer l'utilisation de la nouvelle IP

### 3. Keycloak
- Accéder à `http://192.168.1.2:8080`
- Vérifier que le backend peut s'y connecter

## 📋 Ports utilisés

| Service | Port interne | Port externe | IP |
|---------|--------------|--------------|-----|
| Frontend | 80 | 7153 | 192.168.1.2 |
| Backend | 7200 | 7200 | 192.168.1.2 |
| Keycloak | 8080 | 8080 | 192.168.1.2 |

## 🎯 Avantages de la nouvelle configuration

- ✅ **Cohérence totale** : Tous les fichiers utilisent `192.168.1.2`
- ✅ **CORS configuré** : Le frontend peut communiquer avec le backend
- ✅ **Proxy Vite** : Les appels API sont redirigés correctement
- ✅ **Keycloak aligné** : L'authentification fonctionne avec la nouvelle IP
- ✅ **Docker synchronisé** : Les conteneurs utilisent tous la même configuration

## ⚠️ Points d'attention

1. **Redémarrer les services** après les modifications
2. **Vider le cache** du navigateur si nécessaire
3. **Vérifier la connectivité** réseau vers `192.168.1.2`
4. **Confirmer que Keycloak** est accessible sur la nouvelle IP

La configuration est maintenant entièrement mise à jour pour utiliser `192.168.1.2` !
