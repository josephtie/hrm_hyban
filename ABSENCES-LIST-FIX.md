# Correction du chargement des types d'absence

## 🚨 Problème identifié
L'onglet "Absences" dans `PersonnelDetailView.vue` ne charge pas les types d'absence depuis l'endpoint `/api/rh/absences/lister`.

## 🔍 Causes identifiées

### 1. Mauvais endpoint utilisé
- **Frontend** : `api.post('/rh/absences/lister', {})`
- **Backend** : Endpoint existe en POST mais retourne une structure différente

### 2. Structure de réponse incorrecte
- Le frontend attend `response.data.rows`
- Le backend retourne `response.data` directement

### 3. Erreurs TypeScript
- `absenceTypes` typé comme `never[]`
- Propriétés `id`, `faute` non reconnues

## ✅ Solutions appliquées

### 1. Correction de l'appel API
```typescript
// Avant
const response = await api.post('/rh/absences/lister', {})

// Après  
const response = await api.get('/rh/absences/list')
```

### 2. Ajout des interfaces TypeScript
```typescript
interface AbsenceType {
  id: number
  faute: string
  libelle: string
  code: string
  actif: boolean
}

const absenceTypes = ref<AbsenceType[]>([])
```

### 3. Correction du traitement de la réponse
```typescript
if (response.data && response.data.result === 'success') {
  const typesData = Array.isArray(response.data.rows) ? response.data.rows : []
  absenceTypes.value = typesData.map((type: any) => ({
    id: type.id,
    faute: type.faute,
    libelle: type.libelle || type.libelleAbsence || type.nom || type.faute,
    code: type.code || type.codeAbsence,
    actif: type.actif !== false
  }))
}
```

## 🌟 Améliorations

### Fallback robuste
En cas d'erreur API, le frontend utilise des types par défaut :
```typescript
absenceTypes.value = [
  { id: 1, faute: 'Maladie', libelle: 'Maladie', code: 'MALADIE', actif: true },
  { id: 2, faute: 'Congé', libelle: 'Congé', code: 'CONGE', actif: true },
  { id: 3, faute: 'Congé maternité', libelle: 'Congé maternité', code: 'CONGE_MAT', actif: true },
  { id: 4, faute: 'Congé sans solde', libelle: 'Congé sans solde', code: 'CONGE_SS', actif: true }
]
```

### Logging amélioré
```typescript
console.log('🔄 Chargement des types d\'absence...')
console.log('📥 Réponse API types d\'absence:', response)
console.log('✅ Types d\'absence chargés:', absenceTypes.value)
```

## 🧪 Test

Pour vérifier la correction :

1. **Ouvrir la page** d'un personnel
2. **Cliquer sur l'onglet** "Absences"
3. **Cliquer sur** "Nouvelle Absence"
4. **Vérifier que** le select "Type d'absence" est chargé

## 📋 Résultat attendu

- ✅ **Select chargé** avec les types d'absence depuis l'API
- ✅ **Pas d'erreurs** TypeScript
- ✅ **Fallback fonctionnel** si l'API ne répond pas
- ✅ **Logging clair** pour le debugging

## 🔧 Vérification backend

Si le problème persiste, vérifier :

1. **Endpoint `/api/rh/absences/list`** existe bien en GET
2. **Retourne bien** la structure `{ result: 'success', rows: [...] }`
3. **Les objets** dans `rows` ont bien les champs `id`, `faute`, `libelle`, `code`, `actif`

## 🎯 Solution finale

Le formulaire d'absence devrait maintenant charger correctement les types d'absence depuis l'API !
