# Correction de l'erreur de connexion Keycloak vers 192.168.1.2

## 🚨 Problème identifié

**Erreur** :
```
Caused by: org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://192.168.1.11:8080/realms/hyban/protocol/openid-connect/certs": Connection timed out: connect
```

**Cause** : Le backend essaie toujours de se connecter à l'ancienne adresse `192.168.1.11:8080` au lieu de la nouvelle `192.168.1.2:8080`.

## ✅ Configurations déjà mises à jour

### 1. Profiles Spring Boot
- ✅ `application-prod.properties` : `http://192.168.1.2:8080/realms/hyban`
- ✅ `application-local.properties` : `http://192.168.1.2:8080/realms/hyban`
- ✅ `application-docker.properties` : `http://192.168.1.2:8080/realms/hyban`

### 2. Profil actif
- ✅ `application.properties` : `spring.profiles.active=prod`

### 3. Autres configurations
- ✅ `AuthController.java` : `http://192.168.1.2:8080`
- ✅ `WebSecurityConfig.java` : CORS autorisé pour `192.168.1.2:*`
- ✅ `docker-compose.yml` : Variables d'environnement mises à jour

## 🔧 Actions requises

### 1. Redémarrer le backend (obligatoire)

**Option A : Développement local**
```bash
# Arrêter le backend s'il tourne
# Ctrl+C dans le terminal ou tuer le processus

# Redémarrer avec le profil prod
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

**Option B : Docker**
```bash
# Recréer les conteneurs pour prendre les nouvelles configurations
docker-compose down
docker-compose up -d --build

# Vérifier les logs
docker-compose logs backend
```

### 2. Vérifier la connectivité réseau

**Tester l'accès à Keycloak** :
```bash
curl http://192.168.1.2:8080/realms/hyban/.well-known/openid-configuration
```

**Tester l'accès aux certificats** :
```bash
curl http://192.168.1.2:8080/realms/hyban/protocol/openid-connect/certs
```

### 3. Vérifier les logs du backend

Après redémarrage, chercher ces logs :
```
Using issuer URI: http://192.168.1.2:8080/realms/hyban
Connected to Keycloak successfully
```

## 🧪 Tests de validation

### 1. Test de santé du backend
```bash
curl http://localhost:7200/actuator/health
```

### 2. Test d'authentification
```bash
curl -X POST http://localhost:7200/auth/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=votre-user&password=votre-pass"
```

### 3. Test frontend
1. Accéder à `http://localhost:7153`
2. Tenter de se connecter
3. Vérifier qu'il n'y a plus d'erreur de connexion Keycloak

## 📋 Dépannage avancé

### Si l'erreur persiste après redémarrage :

1. **Vérifier le profil utilisé** :
   ```bash
   curl http://localhost:7200/actuator/info
   ```

2. **Vérifier les variables d'environnement** :
   ```bash
   docker-compose exec backend env | grep KEYCLOAK
   ```

3. **Tester directement Keycloak** :
   ```bash
   # Depuis la machine du backend
   telnet 192.168.1.2 8080
   ```

4. **Vérifier la configuration réseau** :
   ```bash
   ping 192.168.1.2
   nmap -p 8080 192.168.1.2
   ```

### Configuration alternative (si nécessaire)

Si le profil ne change pas, forcer la configuration dans `application.properties` :
```properties
# Forcer la configuration Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://192.168.1.2:8080/realms/hyban
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/certs
```

## 🎯 Résultat attendu

Après redémarrage, le backend devrait :
- ✅ Se connecter à `http://192.168.1.2:8080/realms/hyban`
- ✅ Récupérer les certificats JWT avec succès
- ✅ Accepter les tokens d'authentification
- ✅ Permettre la connexion depuis le frontend

## 📊 État des configurations

| Composant | IP configurée | Statut |
|-----------|---------------|--------|
| Frontend (.env) | 192.168.1.2 | ✅ |
| Frontend (vite.config.ts) | 192.168.1.2 | ✅ |
| Backend (AuthController) | 192.168.1.2 | ✅ |
| Backend (WebSecurityConfig) | 192.168.1.2 | ✅ |
| Backend (application-prod.properties) | 192.168.1.2 | ✅ |
| Docker Compose | 192.168.1.2 | ✅ |

## 🚀 Actions immédiates

1. **Redémarrer le backend** (action critique)
2. **Vérifier les logs** pour confirmer la nouvelle IP
3. **Tester la connexion** frontend/backend

Le problème devrait être résolu après le redémarrage du backend !
