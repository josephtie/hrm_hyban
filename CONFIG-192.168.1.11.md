# Configuration pour l'adresse IP 192.168.1.11

## ✅ Configuration mise à jour

Toutes les configurations ont été mises à jour pour utiliser l'adresse IP `192.168.1.11` :

### Frontend (Vue.js)
- ✅ `.env.development` : `VITE_KEYCLOAK_URL=http://192.168.1.11:8080`
- ✅ `.env.docker` : `VITE_KEYCLOAK_URL=http://192.168.1.11:8080`
- ✅ `vite.config.ts` : Proxy vers `192.168.1.11:8080`
- ✅ `keycloak-auth.service.ts` : Utilisation des variables d'environnement

### Backend (Spring Boot)
- ✅ `AuthController.java` : URL Keycloak par défaut `192.168.1.11:8080`
- ✅ `WebSecurityConfig.java` : CORS autorise `192.168.1.11:*`
- ✅ `application-docker.properties` : Configuration Keycloak `192.168.1.11:8080`

### Docker Compose
- ✅ Variables d'environnement pour frontend et backend
- ✅ Suppression de la dépendance Keycloak pour le backend (utilisé en externe)

## 🚀 Pour démarrer

### Option 1 : Avec Docker (recommandé)
```bash
# Démarrer PostgreSQL et Backend seulement
docker-compose up -d postgres backend

# Démarrer le frontend en local
cd vue3rhpaie-app
npm run dev
```

### Option 2 : Tout en local
1. Démarrer PostgreSQL local
2. Démarrer Keycloak sur `192.168.1.11:8080`
3. Démarrer le backend sur `192.168.1.11:7200`
4. Démarrer le frontend sur le port 7153

## 🌐 Adresses d'accès

- **Frontend** : http://localhost:7153
- **Backend API** : http://192.168.1.11:7200/api
- **Keycloak Admin** : http://192.168.1.11:8080/admin
- **Keycloak Auth** : http://192.168.1.11:8080/realms/hyban

## 🔍 Vérification

### 1. Tester Keycloak
```bash
curl http://192.168.1.11:8080/realms/hyban/.well-known/openid-configuration
```

### 2. Tester le Backend
```bash
curl http://192.168.1.11:7200/actuator/health
```

### 3. Tester l'authentification
```bash
curl -X POST http://192.168.1.11:7200/auth/login \
  -d "username=votre-user&password=votre-pass"
```

## 📝 Configuration Keycloak requise

Assurez-vous que dans Keycloak :
- Realm `hyban` existe
- Client `hrm_frontend` configuré avec :
  - Valid Redirect URIs : `http://localhost:7153/*`
  - Web Origins : `*`
  - Standard Flow Enabled : ON

## 🐛 Dépannage

Si erreur "Network Error" :
1. Vérifier que Keycloak est accessible sur `192.168.1.11:8080`
2. Vérifier les CORS dans Keycloak
3. Redémarrer le frontend après modification des .env

Si erreur CORS du backend :
1. Vérifier WebSecurityConfig autorise `192.168.1.11:*`
2. Redémarrer le backend

La configuration est maintenant optimisée pour votre nouvelle machine !
