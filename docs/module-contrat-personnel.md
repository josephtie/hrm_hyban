# Module RHPAIE - Gestion des Contrats de Travail (État actuel)

**Version :** 1.0  
**Statut :** Document fonctionnel  
**Auteur :** Nectux Ingénieries

---

# 1. Objectif

Le module **Gestion des contrats de travail** permet de gérer l'ensemble du cycle de vie administratif des contrats des collaborateurs tout en assurant une parfaite cohérence avec la paie.

Le système accompagne les utilisateurs RH dans le suivi des échéances contractuelles sans se substituer aux décisions de gestion des ressources humaines.

---

# 2. Principes de fonctionnement

Le contrat constitue le référentiel juridique entre l'entreprise et le collaborateur.

Le logiciel :

- enregistre les contrats ;
- suit leur évolution ;
- génère des alertes automatiques ;
- permet les renouvellements ;
- historise les modifications.

La décision de renouveler, clôturer ou maintenir un salarié relève exclusivement de l'entreprise.

---

# 3. Types de contrats

Le système permet notamment la gestion des :

- CDI
- CDD
- Contrat d'essai
- Stage
- Consultant
- Prestataire
- Intérim
- Apprentissage
- Autres contrats configurables

---

# 4. Informations du contrat

Chaque contrat comporte notamment :

## Identification

- Numéro du contrat
- Employé
- Matricule
- Fonction
- Service
- Département

## Informations juridiques

- Type de contrat
- Date de signature
- Date de début
- Date de fin
- Durée
- Période d'essai

## Informations salariales

- Salaire de base
- Devise
- Mode de paiement
- Banque

## Affectation

- Site
- Chantier
- Direction
- Responsable

---

# 5. Statut du contrat

Dans la version actuelle, le système utilise deux niveaux.

## Statut technique

| Valeur | Signification |
|---------|---------------|
| true | Contrat actif |
| false | Contrat inactif |

Le statut technique est principalement utilisé par les traitements de paie.

---

## Situation métier

Un contrat inactif peut correspondre à plusieurs situations :

- Suspendu
- Expiré
- Résilié
- Terminé

Cette distinction est aujourd'hui réalisée par la gestion RH.

---

# 6. Gestion des échéances

Le système calcule automatiquement les échéances des contrats.

Des tableaux de bord permettent d'identifier :

## Contrats arrivant à échéance

- dans 90 jours
- dans 60 jours
- dans 30 jours
- dans 15 jours

Ces listes permettent aux gestionnaires RH d'anticiper les renouvellements.

---

## Contrats arrivés à échéance

Lorsque la date de fin est dépassée, le contrat apparaît dans la liste des contrats nécessitant une revue RH.

Exemple :

| Employé | Date fin | Retard |
|----------|----------|---------|
| KALLO Mamadou | 12/01/2025 | 569 jours |

Le système ne modifie pas automatiquement le contrat.

---

# 7. Alertes automatiques

Chaque mois, RHPAIE génère des alertes concernant les contrats arrivant à échéance.

Les alertes permettent :

- d'anticiper les renouvellements ;
- de préparer les avenants ;
- de planifier les recrutements.

Ces alertes sont visibles dans le tableau de bord RH.

---

# 8. Renouvellement

Lorsqu'un contrat doit être prolongé, le gestionnaire RH peut :

- créer un nouveau contrat ;
- créer un avenant ;
- modifier la date de fin selon les procédures internes.

L'ensemble des opérations est historisé.

---

# 9. Clôture du contrat

Le système permet la clôture administrative d'un contrat.

Les motifs de clôture peuvent être :

- Fin de CDD
- Démission
- Licenciement
- Retraite
- Décès
- Autre

La clôture est réalisée exclusivement par un utilisateur habilité.

---

# 10. Documents

Chaque contrat peut contenir :

- Contrat signé
- Avenants
- Pièces administratives
- Diplômes
- Pièces d'identité
- Autorisations

Tous les documents sont archivés.

---

# 11. Historique

Toutes les modifications sont historisées :

- création
- modification
- renouvellement
- avenant
- clôture

L'historique indique :

- utilisateur
- date
- heure
- opération réalisée

---

# 12. Tableau de bord

Le module présente notamment :

## Contrats actifs

Nombre de contrats en cours.

## Contrats arrivant à échéance

Liste des contrats expirant prochainement.

## Contrats arrivés à échéance

Liste des contrats dont la date de fin est dépassée.

---

# 13. Rôle de RHPAIE

RHPAIE est un outil d'aide à la gestion.

Le logiciel :

- calcule les échéances ;
- produit les alertes ;
- fournit les tableaux de suivi ;
- historise les opérations.

Les décisions relatives :

- au renouvellement,
- à la clôture,
- au maintien temporaire d'un collaborateur,
- ou à toute autre décision RH,

relèvent exclusivement de l'entreprise utilisatrice.

---

# 14. Cas particulier des contrats arrivés à échéance

Dans certaines organisations, notamment les entreprises intervenant sur plusieurs sites ou chantiers, un collaborateur peut continuer son activité alors que son nouveau contrat est en cours de préparation ou de validation.

Dans ce contexte :

- le contrat initial peut apparaître comme arrivé à échéance ;
- le salarié peut continuer à être rémunéré conformément aux décisions internes de l'entreprise ;
- RHPAIE conserve les alertes afin d'informer les gestionnaires RH.

Le logiciel n'effectue aucune clôture automatique afin de laisser à l'entreprise la maîtrise de ses décisions administratives.

---

# 15. Responsabilités

## RHPAIE

- Calcul des échéances
- Alertes
- Historique
- Gestion documentaire
- Suivi contractuel

## Utilisateur RH

- Vérification des alertes
- Renouvellement
- Création des avenants
- Clôture des contrats
- Validation des informations administratives

---

# 16. Évolutions envisagées

Les évolutions futures pourront inclure :

- Workflow de validation des contrats
- Signature électronique
- Notifications par e-mail
- Gestion des contrats en régularisation
- Indicateurs RH avancés
- Tableaux de bord décisionnels
- Gestion des motifs de maintien après échéance
- Journal des alertes envoyées
