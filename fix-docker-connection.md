# Correction des problèmes de connexion Docker

## Problème identifié
L'erreur `connect ETIMEDOUT 192.168.1.8:8080` indique que votre frontend essaie de se connecter à une adresse IP fixe au lieu des services Docker.

## Corrections apportées

### 1. Configuration API (src/services/api.ts)
- Remplacement de l'IP fixe par une variable d'environnement
- Utilisation de `import.meta.env.VITE_API_URL`

### 2. Configuration Keycloak (src/services/keycloak-auth.service.ts)
- Remplacement du proxy Vite par une URL directe
- Utilisation de `import.meta.env.VITE_KEYCLOAK_URL`

### 3. Configuration Vite (vite.config.ts)
- Correction des proxies pour utiliser localhost
- Proxy pour `/realms` au lieu de `/keycloak`

### 4. Variables d'environnement
- Création de `.env.docker` pour le build Docker
- Création de `.env.development` pour le développement local

## Pour résoudre le problème

### Option 1: Développement local avec Docker
1. Arrêter tous les conteneurs :
```bash
docker-compose down
```

2. Reconstruire avec les nouvelles configurations :
```bash
docker-compose build --no-cache
```

3. Démarrer les services :
```bash
docker-compose up -d
```

### Option 2: Développement sans Docker (frontend local)
1. Démarrer seulement les services backend :
```bash
docker-compose up -d postgres keycloak backend
```

2. Démarrer le frontend en local :
```bash
cd vue3rhpaie-app
npm run dev
```

### Vérification
- Frontend : http://localhost:7153
- Backend : http://localhost:7200
- Keycloak : http://localhost:8080

## Configuration des variables d'environnement

Dans `.env.development` :
```
VITE_API_URL=http://localhost:7200/api
VITE_KEYCLOAK_URL=http://localhost:8080
```

Dans Docker, les variables sont automatiquement définies dans docker-compose.yml.

## Dépannage

Si le problème persiste :
1. Vérifier que les services sont bien démarrés : `docker-compose ps`
2. Vérifier les logs : `docker-compose logs frontend`
3. Tester la connexion : `curl http://localhost:8080/realms/hyban/.well-known/openid-configuration`
