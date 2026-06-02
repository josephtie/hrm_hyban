# Déploiement Portainer - HRM Hyban

Ce guide explique comment déployer le backend Spring Boot, le frontend Vue, PostgreSQL, Keycloak et Carbone avec Portainer.

## Fichiers ajoutés

- `docker-compose.portainer.yml`
- `.env.example`
- `vue3rhpaie-app/Dockerfile.portainer`
- `vue3rhpaie-app/nginx.portainer.conf`

## 1. Préparer les variables

Dans Portainer, créez une stack puis ajoutez les variables d'environnement suivantes dans la section `Environment variables`.

Adaptez surtout `YOUR_VPS_IP`, les mots de passe et le secret Keycloak.

```env
POSTGRES_HRM_DB=hyban_db_v1
POSTGRES_HRM_USER=directus
POSTGRES_HRM_PASSWORD=mot_de_passe_fort

POSTGRES_KEYCLOAK_DB=keycloak_db
POSTGRES_KEYCLOAK_USER=keycloak
POSTGRES_KEYCLOAK_PASSWORD=mot_de_passe_fort

KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=mot_de_passe_fort
KEYCLOAK_CLIENT_SECRET=votre_secret_client_keycloak

FRONTEND_PUBLIC_PORT=7153
BACKEND_PUBLIC_PORT=7200
KEYCLOAK_PUBLIC_PORT=8083
CARBONE_PUBLIC_PORT=4000

VITE_API_BASE_URL=http://YOUR_VPS_IP:7200
VITE_KEYCLOAK_URL=http://YOUR_VPS_IP:8083
VITE_KEYCLOAK_REALM=hyban
VITE_KEYCLOAK_CLIENT_ID=hrm_frontend
VITE_API_TIMEOUT=30000

BACKEND_KEYCLOAK_ISSUER_URI=http://keycloak:8080/realms/hyban
JAVA_OPTS=-Xms256m -Xmx768m

CARBONE_EE_LICENSE=
CARBONE_EE_STUDIO=true
CARBONE_LOG_LEVEL=info
CARBONE_RENDER_TIMEOUT=60000
```

## 2. Créer la stack dans Portainer

Dans Portainer :

1. Allez dans `Stacks`.
2. Cliquez sur `Add stack`.
3. Nom de stack : `hrm-hyban`.
4. Choisissez `Repository` si votre projet est sur Git.
5. Renseignez :
   - Repository URL : URL de votre dépôt
   - Repository reference : `refs/heads/main` ou votre branche
   - Compose path : `docker-compose.portainer.yml`
6. Ajoutez les variables d'environnement.
7. Cliquez sur `Deploy the stack`.

## 3. URLs après déploiement

Avec une IP VPS `YOUR_VPS_IP` :

- Frontend : `http://YOUR_VPS_IP:7153`
- Backend : `http://YOUR_VPS_IP:7200`
- Swagger : `http://YOUR_VPS_IP:7200/swagger-ui/index.html`
- Keycloak : `http://YOUR_VPS_IP:8083`
- Carbone : `http://YOUR_VPS_IP:4000`

## 4. Configuration Keycloak obligatoire

Dans Keycloak, vérifiez le client `hrm_frontend`.

### Valid redirect URIs

```text
http://YOUR_VPS_IP:7153/*
```

### Web origins

```text
http://YOUR_VPS_IP:7153
```

ou :

```text
+
```

## 5. Ports à ouvrir sur le VPS

Si vous utilisez UFW :

```bash
sudo ufw allow 7153
sudo ufw allow 7200
sudo ufw allow 8083
sudo ufw allow 4000
```

## 6. Mise à jour de l'application

Dans Portainer :

1. Allez dans la stack `hrm-hyban`.
2. Cliquez sur `Editor`.
3. Cliquez sur `Update the stack`.
4. Activez l'option de rebuild/re-pull si disponible.

Important : les variables `VITE_*` sont injectées au moment du build frontend. Si vous modifiez `VITE_API_BASE_URL` ou `VITE_KEYCLOAK_URL`, il faut reconstruire l'image frontend.

## 7. Notes importantes

- Ne mettez pas de vrais mots de passe dans Git.
- Utilisez les variables Portainer pour les secrets.
- Pour une vraie production, ajoutez un reverse proxy HTTPS devant les services.
- Le frontend est servi par Nginx avec fallback SPA Vue.
- Le backend utilise le profil Spring `docker`.
