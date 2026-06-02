# Configuration des environnements

## 📋 Description

Ce projet utilise une configuration centralisée pour gérer les différentes environnements (développement, staging, production).

## 🗂️ Fichiers d'environnement

### Fichiers disponibles :
- `env-template` : Template de configuration à copier
- `env-development` : Configuration pour le développement local
- `env-staging` : Configuration pour l'environnement de test
- `env-production` : Configuration pour la production

### Utilisation

1. **Pour le développement local :**
   ```bash
   # Copier le template
   cp env-template .env.local
   
   # Ou utiliser directement la config de développement
   cp env-development .env.local
   ```

2. **Pour le déploiement :**
   ```bash
   # Staging
   cp env-staging .env
   
   # Production
   cp env-production .env
   ```

## 🔧 Variables d'environnement

### API Backend
- `VITE_API_BASE_URL` : URL de base de l'API backend
- `VITE_API_PORT` : Port de l'API backend
- `VITE_API_TIMEOUT` : Timeout des requêtes API (ms)

### Keycloak
- `VITE_KEYCLOAK_URL` : URL du serveur Keycloak
- `VITE_KEYCLOAK_PORT` : Port du serveur Keycloak
- `VITE_KEYCLOAK_REALM` : Realm Keycloak
- `VITE_KEYCLOAK_CLIENT_ID` : ID du client Keycloak
- `VITE_KEYCLOAK_GRANT_TYPE` : Type de grant Keycloak
- `VITE_KEYCLOAK_SCOPE` : Scope Keycloak

### Application
- `VITE_APP_NAME` : Nom de l'application
- `VITE_APP_VERSION` : Version de l'application
- `VITE_APP_DEBUG` : Mode debug (true/false)

## 🏗️ Architecture de la configuration

La configuration est gérée par le fichier `src/config/api.ts` qui :

1. **Lit les variables d'environnement**
2. **Définit des valeurs par défaut**
3. **Construit les URLs des services**
4. **Exporte une configuration centralisée**

### Exemple d'utilisation dans un service :

```typescript
import { API_URLS, API_CONFIG } from '@/config/api'

// Utiliser les URLs pré-construites
const response = await axios.get(API_URLS.PRIMES)

// Ou construire une URL dynamique
const url = buildApiUrl('/api/custom', '/endpoint')
```

## 🚀 Déploiement

### Développement local
```bash
npm run dev
# Utilise automatiquement .env.local ou les valeurs par défaut
```

### Staging
```bash
cp env-staging .env
npm run build
npm run preview
```

### Production
```bash
cp env-production .env
npm run build
npm run preview
```

## 🔒 Sécurité

- **Ne jamais commiter** de fichier `.env.local` contenant des données sensibles
- **Utiliser des valeurs par défaut sécurisées** dans le code
- **Valider les variables d'environnement** au démarrage

## 🐛 Debug

Pour vérifier la configuration actuelle :

```typescript
import { API_CONFIG } from '@/config/api'
console.log('Config API:', API_CONFIG)
```

## 📝 Notes

- Les variables doivent commencer par `VITE_` pour être accessibles dans Vite
- Le fichier `.env.local` est prioritaire sur les autres fichiers `.env`
- Les variables sont automatiquement rechargées au redémarrage du serveur de développement
