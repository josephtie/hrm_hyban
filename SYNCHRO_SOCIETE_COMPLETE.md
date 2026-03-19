# 🎯 Synchronisation Vue.js - Backend pour la gestion des Sociétés

## 📋 Vue d'ensemble

Conversion complète du `SocieteController` MVC en REST avec synchronisation parfaite avec la vue Vue.js.

---

## 🔄 Fichiers modifiés/créés

### Backend (Spring Boot)

#### 1. **DTOs Request/Response**
- ✅ `SocieteVueRequest.java` - DTO complet pour les requêtes avec pagination
- ✅ `SocieteVueResponse.java` - DTO standardisé pour les réponses
- ✅ `SocieteVueDTO.java` - DTO adapté pour la vue Vue.js

#### 2. **Contrôleur REST**
- ✅ `SocieteRestController.java` - Remplacement complet du MVC

### Frontend (Vue.js)

#### 3. **Service TypeScript**
- ✅ `societe.service.ts` - Service API complet avec tous les endpoints

#### 4. **Vue Vue.js**
- ✅ `SocieteView.vue` - Synchronisée avec le backend

---

## 🔌 Endpoints API disponibles

| Méthode | Endpoint | Description | Request | Response |
|---------|----------|-------------|---------|----------|
| GET | `/api/parametrages/societes/{id}` | Récupérer une société | - | `SocieteVueDTO` |
| POST | `/api/parametrages/societes/list` | Liste paginée | `SocieteVueRequest` | `SocieteVueResponse<Object>` |
| POST | `/api/parametrages/societes/save` | Créer une société | `SocieteVueRequest` | `SocieteVueResponse<Societe>` |
| POST | `/api/parametrages/societes/update` | Mettre à jour | `SocieteVueRequest` | `SocieteVueResponse<Societe>` |
| POST | `/api/parametrages/societes/delete` | Supprimer | `IdRequest` | `SocieteVueResponse<Societe>` |
| POST | `/api/parametrages/societes/toggle-principale` | Basculer statut | `IdRequest` | `SocieteVueResponse<Societe>` |
| POST | `/api/parametrages/societes/upload-logo` | Télécharger logo | `MultipartFile` | `SocieteVueResponse<Societe>` |
| GET | `/api/parametrages/societes/list/all` | Toutes les sociétés | - | `List<SocieteVueDTO>` |
| GET | `/api/parametrages/societes/months` | Liste des mois | - | `MoisDTO` |
| GET | `/api/parametrages/societes/banks` | Liste des banques | - | `List<Banque>` |

---

## 🗂️ Mapping des champs

| Vue.js | Backend (Entité) | Mapping |
|--------|------------------|--------|
| raisonSociale | raisonsoc | ✅ Direct |
| sigle | sigle | ✅ Direct |
| rccm | cpteContrib | 🔄 Utilisé comme RCCM |
| compteContribuable | cpteContrib | ✅ Direct |
| telephone | telephone | ✅ Direct |
| email | - | ➕ Ajouté dans DTO |
| adresse | adress | ✅ Direct |
| principale | gratification | 🔄 1L=true, 0L=false |
| urlLogo | urlLogo | ✅ Direct |

---

## 🎨 Fonctionnalités implémentées

### ✅ CRUD Complet
- **Créer** : Formulaire complet avec validation
- **Lire** : Liste paginée avec recherche
- **Mettre à jour** : Édition des sociétés existantes
- **Supprimer** : Suppression avec confirmation

### ✅ Fonctionnalités avancées
- **Pagination** : Support offset/limit côté backend
- **Recherche** : Recherche textuelle en temps réel
- **Filtrage** : Par type (principale/filiale)
- **Toggle statut** : Basculer société principale
- **Upload logo** : Gestion des fichiers images
- **Loading states** : Indicateurs de chargement
- **Gestion erreurs** : Messages utilisateur clairs

### ✅ Interface utilisateur
- **Formulaire latéral** : Design moderne avec Element Plus
- **Tableau interactif** : Tri, sélection, actions
- **Barre d'outils** : Recherche et filtrage
- **Messages** : Confirmations et notifications

---

## 🔧 Configuration technique

### Backend
- **CORS** : Configuré pour localhost:7153, 7200, 4200, 127.0.0.1:3000
- **Sécurité** : Intégré avec Spring Security
- **Logging** : SLF4J pour tous les endpoints
- **Validation** : Validation des paramètres d'entrée

### Frontend
- **TypeScript** : Typage strict pour toutes les interfaces
- **Composition API** : Vue 3 avec `<script setup>`
- **Element Plus** : Composants UI modernes
- **Axios** : Client HTTP pour les appels API

---

## 📊 Format des données

### Request (Pagination)
```json
{
  "offset": 0,
  "limit": 10,
  "search": "texte",
  "raisonSociale": "Nom société",
  "sigle": "SIG",
  "principale": true
}
```

### Response (Standard)
```json
{
  "rows": [...],
  "total": 25,
  "result": "success",
  "message": "Opération réussie"
}
```

### SocieteDTO
```json
{
  "id": 1,
  "raisonSociale": "Société Test",
  "sigle": "ST",
  "rccm": "CI-ABJ-2024-1234",
  "compteContribuable": "123456789",
  "telephone": "20223344",
  "email": "contact@test.ci",
  "adresse": "Abidjan, Plateau",
  "principale": true,
  "urlLogo": "/static/logo/logo.png"
}
```

---

## 🚀 Utilisation

### 1. Démarrer le backend
```bash
cd backend_rhpaie
mvn spring-boot:run
```
Le serveur démarrera sur `http://localhost:7200`

### 2. Utiliser la vue Vue.js
La vue est déjà synchronisée et prête à l'emploi

### 3. Exemple d'utilisation
```typescript
// Charger la liste
const response = await societeService.getAll({
  page: 0,
  size: 10,
  search: 'test'
})

// Créer une société
await societeService.create({
  raisonSociale: 'Ma Société',
  sigle: 'MS',
  rccm: 'CI-ABJ-2024-1234',
  principale: false
})
```

---

## 🎯 Bilan final

- ✅ **Contrôleur MVC → REST** : Conversion complète
- ✅ **Vue synchronisée** : Communication parfaite frontend/backend
- ✅ **TypeScript** : Typage complet et sécurité
- ✅ **Fonctionnalités** : CRUD + recherche + pagination
- ✅ **UX moderne** : Interface responsive et intuitive
- ✅ **Gestion erreurs** : Messages clairs pour l'utilisateur
- ✅ **Performance** : Pagination et lazy loading

**La synchronisation Vue.js - Backend pour la gestion des sociétés est maintenant complète et opérationnelle !** 🎉
