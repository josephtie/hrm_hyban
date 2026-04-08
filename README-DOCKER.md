# RHPAIE - Docker Setup

Guide de déploiement de l'application RHPAIE avec Docker Compose.

## 🏗️ Architecture

L'application est composée de 4 services principaux :

- **PostgreSQL** : Base de données
- **Keycloak** : Serveur d'authentification
- **Backend** : API Spring Boot
- **Frontend** : Application Vue.js avec Nginx

## 📋 Prérequis

- Docker Desktop installé
- Docker Compose (version 3.8+)
- Au moins 4GB de RAM disponible
- Ports libres : 5432, 7200, 7153, 8080

## 🚀 Démarrage rapide

### 1. Cloner le projet
```bash
git clone <repository-url>
cd backend_rhpaie
```

### 2. Lancer tous les services
```bash
docker-compose up -d
```

### 3. Vérifier le statut
```bash
docker-compose ps
```

### 4. Accéder aux applications

- **Frontend Vue.js** : http://localhost:7153
- **Backend API** : http://localhost:7200
- **Keycloak Admin** : http://localhost:8080/admin
- **API Documentation** : http://localhost:7200/swagger-ui.html

## 🔧 Configuration

### Variables d'environnement

Vous pouvez personnaliser la configuration avec les variables d'environnement suivantes :

```bash
# Base de données
POSTGRES_DB=hyban_db_v1
POSTGRES_USER=directus
POSTGRES_PASSWORD=directus

# Keycloak
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin

# Mail (optionnel)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=votre-email@gmail.com
MAIL_PASSWORD=votre-mot-de-passe
```

### Fichiers de configuration

- `docker-compose.yml` : Configuration des services
- `application-docker.properties` : Configuration Spring Boot pour Docker
- `nginx.conf` : Configuration Nginx pour le frontend

## 📝 Commandes utiles

### Démarrer les services
```bash
docker-compose up -d
```

### Arrêter les services
```bash
docker-compose down
```

### Voir les logs
```bash
# Tous les services
docker-compose logs -f

# Service spécifique
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
docker-compose logs -f keycloak
```

### Reconstruire les images
```bash
docker-compose build --no-cache
```

### Nettoyer tout
```bash
docker-compose down -v --remove-orphans
docker system prune -f
```

## 🔍 Dépannage

### Problèmes courants

1. **Port déjà utilisé**
   ```bash
   # Vérifier les ports utilisés
   netstat -tulpn | grep :7200
   # Modifier les ports dans docker-compose.yml si nécessaire
   ```

2. **Mémoire insuffisante**
   - Augmenter la RAM allouée à Docker Desktop
   - Limiter les services actifs

3. **Keycloak ne démarre pas**
   ```bash
   # Vérifier les logs de Keycloak
   docker-compose logs keycloak
   # Attendre que PostgreSQL soit complètement démarré
   ```

4. **Backend ne se connecte pas à la base**
   ```bash
   # Vérifier la connexion réseau
   docker-compose exec backend ping postgres
   # Redémarrer le backend
   docker-compose restart backend
   ```

### Accès aux conteneurs

```bash
# Accès au backend
docker-compose exec backend sh

# Accès à PostgreSQL
docker-compose exec postgres psql -U directus -d hyban_db_v1

# Accès à Keycloak
docker-compose exec keycloak sh
```

## 🔄 Mise à jour

Pour mettre à jour l'application :

```bash
# Arrêter les services
docker-compose down

# Mettre à jour les images
docker-compose pull

# Reconstruire les images locales
docker-compose build

# Redémarrer
docker-compose up -d
```

## 🌐 Configuration en production

Pour un environnement de production, utilisez le profil `production` :

```bash
docker-compose --profile production up -d
```

Cela ajoutera :
- **Nginx reverse proxy** : Port 80/443
- Configuration SSL
- Optimisations de performance

## 📊 Monitoring

### Health checks
- PostgreSQL : Vérification de la connexion
- Keycloak : Endpoint `/health/ready`
- Backend : Actuator `/actuator/health`
- Frontend : Vérification Nginx

### Logs structurés
Les logs sont configurés pour être facilement parsables et peuvent être intégrés avec des systèmes comme ELK Stack ou Grafana.

## 🔐 Sécurité

- Utilisation d'utilisateurs non-root dans les conteneurs
- Variables d'environnement pour les secrets
- Configuration CORS restrictive
- Headers de sécurité configurés

## 📈 Performance

- Images multi-stage pour réduire la taille
- Cache des dépendances Maven/NPM
- Volumes persistants pour les données
- Configuration JVM optimisée

## 🆘 Support

En cas de problème :

1. Vérifier les logs avec `docker-compose logs`
2. Consulter la documentation des services respectifs
3. Vérifier la configuration réseau
4. S'assurer que les ports sont bien disponibles
