# RHPAIE - Application RH & Paie

Application web moderne de gestion RH et Paie développée avec Vue 3, destinée aux PME et grandes entreprises en Afrique francophone.

## 🚀 Fonctionnalités

### 🏢 Module Organisation
- Gestion des informations de l'entreprise
- Structure hiérarchique (Directions, Services)
- Affectation organisationnelle

### 👥 Module Personnel
- Dossier salarié complet
- Gestion des contrats et catégories
- Historique de carrière
- Statut actif/sorti

### 💰 Module Paie
- Génération automatique des bulletins
- Configuration des rubriques de paie
- Calcul des gains et retenues
- États de paie mensuels

### ⏱️ Module Temps & Absences
- Types d'absences configurables
- Gestion des congés
- Validation des demandes
- Solde de congés

### 📊 Reporting & Tableaux de bord
- Effectifs par direction/service
- Masse salariale
- Contrats à échéance
- Statistiques RH

## 🛠️ Stack Technique

- **Frontend**: Vue 3 (Composition API)
- **UI Framework**: Element Plus
- **Gestion d'état**: Pinia
- **Routing**: Vue Router
- **HTTP Client**: Axios
- **Charts**: Chart.js
- **Build Tool**: Vite
- **Language**: TypeScript

## 📦 Installation

### Prérequis
- Node.js 18+ 
- npm ou yarn

### Installation des dépendances
```bash
npm install
```

### Démarrage du serveur de développement
```bash
npm run dev
```

### Build pour production
```bash
npm run build
```

### Preview du build
```bash
npm run preview
```

## 🔐 Authentification

### Accès démo
- **Nom d'utilisateur**: `admin`
- **Mot de passe**: `admin123`

### Rôles et permissions
- **Admin RH**: Accès complet à tous les modules
- **Manager PAIE**: Gestion de la paie et états
- **Manager**: Gestion du personnel de son équipe
- **Lecture seule**: Consultation uniquement

## 📁 Structure du projet

```
src/
├── components/         # Composants réutilisables
├── views/             # Pages de l'application
│   ├── auth/          # Authentification
│   ├── layout/        # Layout principal
│   ├── parametrage/   # Paramétrage RH
│   ├── personnel/     # Gestion du personnel
│   ├── paie/          # Module paie
│   └── reporting/     # Reporting
├── stores/            # Gestion d'état (Pinia)
├── router/            # Configuration des routes
├── types/             # Types TypeScript
├── utils/             # Utilitaires
└── assets/            # Assets statiques
```

## 🎯 Modules de paramétrage

L'application inclut un module de paramétrage complet obligatoire avant toute exploitation :

### Organisation
- Informations entreprise
- Directions et sous-directions
- Services et hiérarchie

### Référentiels RH
- Catégories professionnelles
- Grades et échelons
- Types de contrats
- Types d'employés

### Temps & Absences
- Types d'absences
- Règles de congés
- Jours fériés

### Paie
- Rubriques de paie (gains, retenues, cotisations)
- Charges sociales
- Avantages et primes

## 🔧 Configuration

### Variables d'environnement
Créer un fichier `.env.local` à la racine :

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=RHPAIE
```

## 📱 Responsive Design

L'application est optimisée pour :
- Desktop (prioritaire)
- Tablettes
- Mobiles (limité)

## 🚨 Notes importantes

1. **Paramétrage requis**: L'application nécessite un paramétrage complet avant utilisation
2. **Permissions**: L'accès aux fonctionnalités dépend des rôles et permissions
3. **Backend**: Cette version utilise des données mockées, connectez-la à votre backend
4. **Navigateurs supportés**: Chrome, Firefox, Safari, Edge (versions récentes)

## 🤝 Contribuer

1. Fork le projet
2. Créer une branche feature
3. Commit les changements
4. Push vers la branche
5. Créer une Pull Request

## 📄 Licence

Ce projet est sous licence MIT.

## 📞 Support

Pour toute question ou support technique :
- Email: support@rhpaie.com
- Documentation: [Wiki du projet]

---

**RHPAIE** - La solution moderne pour la gestion RH & Paie en Afrique francophone
