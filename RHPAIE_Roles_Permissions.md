# RHPAIE — Gestion des rôles et permissions

## 1. Objectif

Ce document définit une stratégie de mise en place de la gestion des rôles et permissions dans RHPAIE **sans reprendre ni réécrire le projet existant**.

RHPAIE possède :

- un **Frontend** ;
- un **Backend** ;
- une authentification existante, potentiellement basée sur **Keycloak** ;
- des rôles déjà existants.

L'objectif est d'ajouter progressivement une gestion fine des permissions tout en conservant les fonctionnalités métier actuelles.

---

# 2. Principe général

L'architecture cible est :

```text
                         ┌─────────────────┐
                         │     Keycloak    │
                         │ Authentification│
                         └────────┬────────┘
                                  │ JWT
                                  ▼
┌─────────────────┐       ┌─────────────────┐
│    Frontend     │──────▶│     Backend     │
│                 │ HTTP  │ Spring Security │
│ Affichage/menu  │       │ Autorisations   │
└─────────────────┘       └────────┬────────┘
                                   │
                                   ▼
                          ┌─────────────────┐
                          │ RHPAIE Database │
                          │ Roles/Perms     │
                          └─────────────────┘
```

### Règle fondamentale

Le **Frontend masque les fonctionnalités** auxquelles l'utilisateur n'a pas accès.

Le **Backend reste la source de vérité et doit toujours contrôler les permissions**.

> Ne jamais considérer le Frontend comme une protection de sécurité.

---

# 3. Modèle RBAC

RHPAIE utilise un modèle :

```text
Utilisateur
    │
    ▼
  Rôle
    │
    ▼
Permissions
```

Exemple :

```text
Utilisateur : Jean

Rôle : PAIE

Permissions :
    PAYROLL_READ
    PAYROLL_CALCULATE
    PAYROLL_VALIDATE
    PAYROLL_EXPORT
    EMPLOYEE_READ
```

Un utilisateur n'a donc pas besoin d'être configuré permission par permission.

On configure les permissions du rôle.

---

# 4. Conservation des rôles existants

Les rôles actuellement présents dans RHPAIE ne doivent pas être supprimés.

Exemple :

```text
ADMIN
RH
PAIE
MANAGER
USER
```

Ils deviennent simplement des regroupements de permissions.

Exemple :

```text
ADMIN
 ├── EMPLOYEE_READ
 ├── EMPLOYEE_CREATE
 ├── EMPLOYEE_UPDATE
 ├── EMPLOYEE_DELETE
 ├── CONTRACT_READ
 ├── CONTRACT_CREATE
 ├── CONTRACT_UPDATE
 ├── CONTRACT_CLOSE
 ├── PAYROLL_READ
 ├── PAYROLL_CALCULATE
 ├── PAYROLL_VALIDATE
 ├── PAYROLL_EXPORT
 └── ROLE_MANAGE
```

---

# 5. Convention de nommage

Utiliser une convention simple :

```text
MODULE_ACTION
```

Exemples :

```text
EMPLOYEE_READ
EMPLOYEE_CREATE
EMPLOYEE_UPDATE
EMPLOYEE_DELETE

CONTRACT_READ
CONTRACT_CREATE
CONTRACT_UPDATE
CONTRACT_CLOSE

PAYROLL_READ
PAYROLL_CALCULATE
PAYROLL_VALIDATE
PAYROLL_EXPORT
```

Pour les paramètres :

```text
PARAMETER_READ
PARAMETER_UPDATE
```

Pour les utilisateurs :

```text
USER_READ
USER_CREATE
USER_UPDATE
USER_DISABLE
```

Pour les rôles :

```text
ROLE_READ
ROLE_CREATE
ROLE_UPDATE
ROLE_DELETE
ROLE_ASSIGN
```

---

# 6. Catalogue initial des permissions RHPAIE

## 6.1 Employés

```text
EMPLOYEE_READ
EMPLOYEE_CREATE
EMPLOYEE_UPDATE
EMPLOYEE_DELETE
EMPLOYEE_EXPORT
```

## 6.2 Contrats

```text
CONTRACT_READ
CONTRACT_CREATE
CONTRACT_UPDATE
CONTRACT_CLOSE
CONTRACT_EXPORT
```

## 6.3 Paie

```text
PAYROLL_READ
PAYROLL_CALCULATE
PAYROLL_VALIDATE
PAYROLL_CANCEL
PAYROLL_EXPORT
```

## 6.4 Congés

```text
LEAVE_READ
LEAVE_CREATE
LEAVE_UPDATE
LEAVE_VALIDATE
LEAVE_CANCEL
```

## 6.5 Absences

```text
ABSENCE_READ
ABSENCE_CREATE
ABSENCE_UPDATE
ABSENCE_VALIDATE
```

## 6.6 Rapports

```text
REPORT_READ
REPORT_EXPORT
```

## 6.7 Paramétrage

```text
PARAMETER_READ
PARAMETER_UPDATE
```

## 6.8 Utilisateurs

```text
USER_READ
USER_CREATE
USER_UPDATE
USER_DISABLE
```

## 6.9 Rôles et permissions

```text
ROLE_READ
ROLE_CREATE
ROLE_UPDATE
ROLE_DELETE
ROLE_ASSIGN
PERMISSION_READ
```

---

# 7. Exemple de matrice des rôles

| Permission | ADMIN | RH | PAIE | MANAGER | USER |
|---|---:|---:|---:|---:|---:|
| EMPLOYEE_READ | ✓ | ✓ | ✓ | ✓ | ✓ |
| EMPLOYEE_CREATE | ✓ | ✓ | | | |
| EMPLOYEE_UPDATE | ✓ | ✓ | | | |
| EMPLOYEE_DELETE | ✓ | ✓ | | | |
| EMPLOYEE_EXPORT | ✓ | ✓ | ✓ | | |
| CONTRACT_READ | ✓ | ✓ | ✓ | ✓ | |
| CONTRACT_CREATE | ✓ | ✓ | | | |
| CONTRACT_UPDATE | ✓ | ✓ | | | |
| CONTRACT_CLOSE | ✓ | ✓ | | | |
| PAYROLL_READ | ✓ | ✓ | ✓ | | |
| PAYROLL_CALCULATE | ✓ | | ✓ | | |
| PAYROLL_VALIDATE | ✓ | | ✓ | | |
| PAYROLL_EXPORT | ✓ | ✓ | ✓ | | |
| LEAVE_READ | ✓ | ✓ | ✓ | ✓ | ✓ |
| LEAVE_CREATE | ✓ | ✓ | | ✓ | ✓ |
| LEAVE_VALIDATE | ✓ | ✓ | | ✓ | |
| REPORT_READ | ✓ | ✓ | ✓ | ✓ | |
| REPORT_EXPORT | ✓ | ✓ | ✓ | | |
| PARAMETER_READ | ✓ | ✓ | ✓ | | |
| PARAMETER_UPDATE | ✓ | | | | |
| ROLE_MANAGE | ✓ | | | | |

Cette matrice doit être adaptée aux règles réelles de chaque client.

---

# 8. Backend — principe de sécurité

Le Backend est responsable de la sécurité.

Avec Spring Security, les endpoints doivent progressivement être protégés par permission.

Exemple :

```java
@PreAuthorize("hasAuthority('EMPLOYEE_READ')")
@GetMapping
public List<Employee> getEmployees() {
    return employeeService.findAll();
}
```

Création :

```java
@PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
@PostMapping
public Employee create(@RequestBody Employee employee) {
    return employeeService.create(employee);
}
```

Modification :

```java
@PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
@PutMapping("/{id}")
public Employee update(
        @PathVariable Long id,
        @RequestBody Employee employee) {

    return employeeService.update(id, employee);
}
```

Suppression :

```java
@PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
@DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {
    employeeService.delete(id);
}
```

---

# 9. Exemple pour la paie

La paie doit être protégée plus fortement.

```java
@PreAuthorize("hasAuthority('PAYROLL_READ')")
@GetMapping
public List<LivreDePaie> getPayroll() {
    ...
}
```

Calcul :

```java
@PreAuthorize("hasAuthority('PAYROLL_CALCULATE')")
@PostMapping("/calculate")
public void calculate() {
    ...
}
```

Validation :

```java
@PreAuthorize("hasAuthority('PAYROLL_VALIDATE')")
@PostMapping("/validate")
public void validate() {
    ...
}
```

Export :

```java
@PreAuthorize("hasAuthority('PAYROLL_EXPORT')")
@GetMapping("/export")
public ResponseEntity<?> export() {
    ...
}
```

---

# 10. Frontend — gestion des permissions

Le Frontend doit récupérer les permissions de l'utilisateur connecté.

Exemple :

```json
{
  "username": "jean",
  "roles": [
    "PAIE"
  ],
  "permissions": [
    "EMPLOYEE_READ",
    "PAYROLL_READ",
    "PAYROLL_CALCULATE",
    "PAYROLL_VALIDATE",
    "PAYROLL_EXPORT"
  ]
}
```

Créer un service central :

```text
PermissionService
```

Exemple :

```typescript
hasPermission('PAYROLL_VALIDATE')
```

ou selon la technologie du Frontend :

```javascript
hasPermission("PAYROLL_VALIDATE")
```

---

# 11. Masquage des menus

Exemple :

```text
Tableau de bord
Personnel
Contrats
Paie
Congés
Rapports
Paramétrage
Administration
```

Le Frontend peut faire :

```text
Si EMPLOYEE_READ
    → afficher Personnel

Si CONTRACT_READ
    → afficher Contrats

Si PAYROLL_READ
    → afficher Paie

Si REPORT_READ
    → afficher Rapports

Si ROLE_MANAGE
    → afficher Administration
```

Cela évite de montrer à l'utilisateur des fonctionnalités auxquelles il n'a pas accès.

---

# 12. Masquage des boutons

Exemple sur la page Employés :

```text
[Ajouter]
[Modifier]
[Supprimer]
[Exporter]
```

Règles :

```text
EMPLOYEE_CREATE → bouton Ajouter

EMPLOYEE_UPDATE → bouton Modifier

EMPLOYEE_DELETE → bouton Supprimer

EMPLOYEE_EXPORT → bouton Exporter
```

Ainsi un utilisateur peut avoir :

```text
EMPLOYEE_READ
```

sans avoir :

```text
EMPLOYEE_UPDATE
```

Il peut donc consulter mais pas modifier.

---

# 13. Important : Frontend ≠ sécurité

Même si le bouton est masqué :

```text
[Modifier]
```

un utilisateur pourrait essayer d'appeler directement :

```http
PUT /api/employees/123
```

C'est pourquoi le Backend doit également vérifier :

```text
EMPLOYEE_UPDATE
```

Architecture :

```text
Frontend
    │
    │ Masque les boutons
    ▼
Backend
    │
    │ Vérifie les permissions
    ▼
Service métier
    │
    ▼
Database
```

---

# 14. Base de données

Si les permissions sont gérées dans RHPAIE, prévoir :

```sql
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);
```

Table des rôles :

```sql
CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);
```

Table de liaison :

```sql
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    PRIMARY KEY (role_id, permission_id),

    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);
```

Si une table `roles` existe déjà, **ne pas la recréer**.

Faire une migration adaptée au modèle actuel.

---

# 15. Entités JPA

Exemple :

```java
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    private boolean active;
}
```

Pour le rôle :

```java
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "role_permissions",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id")
)
private Set<Permission> permissions = new HashSet<>();
```

---

# 16. API d'administration

Prévoir progressivement les endpoints suivants :

```text
GET    /api/roles
GET    /api/roles/{id}
POST   /api/roles
PUT    /api/roles/{id}
DELETE /api/roles/{id}
```

Permissions :

```text
GET /api/permissions
```

Association :

```text
GET /api/roles/{id}/permissions

PUT /api/roles/{id}/permissions
```

Utilisateur :

```text
GET /api/users/{id}/roles

PUT /api/users/{id}/roles
```

Ces endpoints doivent être réservés aux administrateurs.

Exemple :

```java
@PreAuthorize("hasAuthority('ROLE_MANAGE')")
```

---

# 17. Écran Frontend « Rôles et permissions »

Créer une page :

```text
Administration
   └── Rôles & Permissions
```

Interface recommandée :

```text
┌─────────────────────────────────────────────┐
│ Rôles & Permissions                         │
├─────────────────────────────────────────────┤
│                                             │
│ Rôle : [ PAIE ▼ ]                           │
│                                             │
│ Personnel                                   │
│ ☑ Consulter                                 │
│ ☐ Ajouter                                   │
│ ☐ Modifier                                  │
│ ☐ Supprimer                                 │
│ ☑ Exporter                                  │
│                                             │
│ Contrats                                    │
│ ☑ Consulter                                 │
│ ☐ Créer                                     │
│ ☐ Modifier                                  │
│ ☐ Clôturer                                  │
│                                             │
│ Paie                                        │
│ ☑ Consulter                                 │
│ ☑ Calculer                                  │
│ ☑ Valider                                   │
│ ☑ Exporter                                  │
│                                             │
│              [Enregistrer]                  │
└─────────────────────────────────────────────┘
```

---

# 18. Ne pas donner directement les permissions aux utilisateurs

Éviter :

```text
Utilisateur
   └── 35 permissions
```

Préférer :

```text
Utilisateur
   └── PAIE
         └── permissions
```

Exception possible pour des besoins très particuliers, mais ce n'est pas recommandé pour la première version.

---

# 19. Gestion des utilisateurs

Écran :

```text
Administration
   └── Utilisateurs
```

Exemple :

```text
Jean Kouassi
Rôle : PAIE

[Modifier]
```

Modification :

```text
Nom utilisateur
Email
Statut

Rôles :
☑ PAIE
☐ RH
☐ MANAGER
☐ ADMIN
```

---

# 20. Migration sans interruption

La migration doit être progressive.

## Étape 1

Lister les rôles existants.

```text
ADMIN
RH
PAIE
MANAGER
USER
```

## Étape 2

Lister les fonctionnalités existantes.

```text
Personnel
Contrats
Paie
Congés
Absences
Rapports
Paramétrage
Administration
```

## Étape 3

Créer les permissions.

## Étape 4

Associer les permissions aux rôles.

## Étape 5

Protéger d'abord les endpoints sensibles.

Priorité :

```text
1. Paramétrage
2. Administration
3. Validation paie
4. Calcul paie
5. Clôture contrats
6. Suppression
7. Modification
8. Consultation
```

## Étape 6

Ajouter les contrôles Frontend.

## Étape 7

Tester chaque rôle.

---

# 21. Stratégie de compatibilité

Pendant la migration, conserver les anciens rôles.

Exemple temporaire :

```java
@PreAuthorize(
    "hasRole('ADMIN') OR hasAuthority('EMPLOYEE_READ')"
)
```

Une fois la migration terminée :

```java
@PreAuthorize("hasAuthority('EMPLOYEE_READ')")
```

Cette méthode permet de réduire le risque de casser RHPAIE.

---

# 22. Gestion des erreurs

Si l'utilisateur n'a pas la permission :

```http
403 Forbidden
```

Exemple de réponse :

```json
{
  "status": 403,
  "code": "ACCESS_DENIED",
  "message": "Permission PAYROLL_VALIDATE requise"
}
```

Le Frontend doit afficher un message compréhensible :

```text
Vous n'avez pas les droits nécessaires pour effectuer cette opération.
```

---

# 23. Audit des opérations sensibles

Pour RHPAIE, il est fortement recommandé de journaliser :

```text
Calcul de paie
Validation de paie
Annulation
Modification salaire
Modification contrat
Clôture contrat
Suppression employé
Modification paramétrage
Modification rôle
Attribution d'une permission
```

Exemple :

```text
Utilisateur : admin
Action      : PAYROLL_VALIDATE
Objet       : PAIE 07/2026
Date        : 10/08/2026 10:30
Résultat    : SUCCESS
```

---

# 24. Évolution future : permissions + périmètre

Une permission seule ne suffit pas toujours.

Exemple :

```text
RH_A
```

doit pouvoir consulter uniquement :

```text
Agence Abidjan
```

alors que :

```text
RH_B
```

voit :

```text
Agence Bouaké
```

On pourra alors introduire un périmètre :

```text
Permission
    +
Scope
```

Exemple :

```text
EMPLOYEE_READ
Scope = AGENCE_ABIDJAN
```

Autres périmètres possibles :

```text
Entreprise
Filiale
Agence
Département
Direction
Site
```

Cette fonctionnalité doit être développée dans une deuxième phase.

---

# 25. Architecture cible

```text
                         ┌──────────────────┐
                         │     Keycloak     │
                         │                  │
                         │ Authentication   │
                         └────────┬─────────┘
                                  │
                                  │ JWT
                                  ▼
┌─────────────────────────────────────────────────┐
│                    FRONTEND                     │
│                                                 │
│ PermissionService                               │
│                                                 │
│ hasPermission()                                 │
│                                                 │
│ Menus / boutons / actions                       │
└───────────────────────┬─────────────────────────┘
                        │ HTTP
                        ▼
┌─────────────────────────────────────────────────┐
│                    BACKEND                      │
│                                                 │
│ Spring Security                                 │
│                                                 │
│ @PreAuthorize(...)                              │
│                                                 │
│ Controllers                                     │
│ Services                                        │
└───────────────────────┬─────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────┐
│                 RHPAIE DATABASE                 │
│                                                 │
│ Users                                           │
│ Roles                                           │
│ Permissions                                     │
│ Role_Permissions                                │
│ Audit                                           │
└─────────────────────────────────────────────────┘
```

---

# 26. Règles de sécurité à respecter

1. Le Backend est toujours l'autorité finale.
2. Le Frontend ne doit jamais être considéré comme une protection.
3. Les permissions doivent être nommées de manière stable.
4. Ne pas créer un rôle pour chaque combinaison de permissions.
5. Ne pas supprimer les rôles existants pendant la migration.
6. Ne pas modifier le métier RHPAIE uniquement pour ajouter les permissions.
7. Protéger les opérations sensibles en priorité.
8. Tester chaque endpoint avec un utilisateur autorisé et non autorisé.
9. Journaliser les opérations critiques.
10. Prévoir ultérieurement les périmètres de données.

---

# 27. Plan d'implémentation recommandé

## Sprint 1 — Modèle

- [ ] Inventaire des rôles actuels
- [ ] Inventaire des endpoints
- [ ] Création du catalogue de permissions
- [ ] Création des tables nécessaires
- [ ] Association rôles / permissions
- [ ] Migration des rôles existants

## Sprint 2 — Backend

- [ ] Configuration Spring Security
- [ ] Conversion des permissions en authorities
- [ ] Protection des endpoints sensibles
- [ ] Gestion des erreurs 403
- [ ] Tests de sécurité

## Sprint 3 — Frontend

- [ ] Service de permissions
- [ ] Masquage des menus
- [ ] Masquage des boutons
- [ ] Gestion des actions interdites
- [ ] Page Rôles & Permissions

## Sprint 4 — Administration

- [ ] Gestion des rôles
- [ ] Attribution des permissions
- [ ] Attribution des rôles aux utilisateurs
- [ ] Audit
- [ ] Tests finaux

---

# 28. Checklist de recette

Pour chaque rôle :

### ADMIN

- [ ] Peut consulter les employés
- [ ] Peut créer
- [ ] Peut modifier
- [ ] Peut supprimer
- [ ] Peut gérer les contrats
- [ ] Peut gérer la paie
- [ ] Peut gérer les rôles

### RH

- [ ] Peut consulter les employés
- [ ] Peut créer les employés
- [ ] Peut modifier les employés
- [ ] Peut gérer les contrats
- [ ] Ne peut pas valider la paie si cette permission ne lui est pas attribuée
- [ ] Ne peut pas gérer les rôles

### PAIE

- [ ] Peut consulter les employés
- [ ] Peut consulter la paie
- [ ] Peut calculer
- [ ] Peut valider
- [ ] Peut exporter
- [ ] Ne peut pas supprimer un employé

### MANAGER

- [ ] Peut consulter les informations autorisées
- [ ] Peut effectuer uniquement les actions prévues

### USER

- [ ] Peut uniquement consulter les fonctionnalités prévues

---

# 29. Principe de déploiement

La mise en production doit suivre :

```text
DEV
 ↓
Tests permissions
 ↓
Préproduction
 ↓
Validation métier
 ↓
Production
```

Avant production :

```text
☑ ADMIN fonctionne
☑ RH fonctionne
☑ PAIE fonctionne
☑ MANAGER fonctionne
☑ USER fonctionne
☑ Accès refusés retournent 403
☑ Menus masqués correctement
☑ API protégées
☑ Aucune opération sensible accessible sans permission
```

---

# 30. Conclusion

La gestion des rôles et permissions de RHPAIE doit être ajoutée comme une **couche de sécurité**, et non comme une refonte du logiciel.

La stratégie recommandée est :

```text
Rôles existants
       +
Permissions
       +
Contrôle Backend
       +
Contrôle Frontend
       +
Audit
```

Cette approche permet de faire évoluer RHPAIE progressivement, de limiter les risques de régression et de proposer à terme une administration des droits adaptée aux différents clients.

**Priorité absolue :**

```text
BACKEND = sécurité réelle
FRONTEND = expérience utilisateur
KEYCLOAK = authentification / identité
RBAC = organisation des autorisations
AUDIT = traçabilité
```

