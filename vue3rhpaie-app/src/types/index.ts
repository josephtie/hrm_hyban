// Types pour l'authentification
export interface User {
  id: string
  username: string
  email: string
  firstName: string
  lastName: string
  role: UserRole
  permissions: Permission[]
  isActive: boolean
  createdAt: string
  lastLogin?: string
}

export enum UserRole {
  ADMIN_RH = 'ADMIN_RH',
  PAIE_MANAGER = 'PAIE_MANAGER',
  MANAGER = 'MANAGER',
  READ_ONLY = 'READ_ONLY'
}

export enum Permission {
  // Paramétrage
  PARAM_ORGANISATION_READ = 'PARAM_ORGANISATION_READ',
  PARAM_ORGANISATION_WRITE = 'PARAM_ORGANISATION_WRITE',
  PARAM_REFERENTIELS_READ = 'PARAM_REFERENTIELS_READ',
  PARAM_REFERENTIELS_WRITE = 'PARAM_REFERENTIELS_WRITE',
  PARAM_TEMPS_ABSENCES_READ = 'PARAM_TEMPS_ABSENCES_READ',
  PARAM_TEMPS_ABSENCES_WRITE = 'PARAM_TEMPS_ABSENCES_WRITE',
  PARAM_PAIE_READ = 'PARAM_PAIE_READ',
  PARAM_PAIE_WRITE = 'PARAM_PAIE_WRITE',
  
  // Personnel
  PERSONNEL_READ = 'PERSONNEL_READ',
  PERSONNEL_WRITE = 'PERSONNEL_WRITE',
  PERSONNEL_DELETE = 'PERSONNEL_DELETE',
  
  // Paie
  PAIE_BULLETINS_READ = 'PAIE_BULLETINS_READ',
  PAIE_BULLETINS_WRITE = 'PAIE_BULLETINS_WRITE',
  PAIE_ETATS_READ = 'PAIE_ETATS_READ',
  
  // Reporting
  REPORTING_READ = 'REPORTING_READ',
  REPORTING_EXPORT = 'REPORTING_EXPORT'
}

export interface LoginCredentials {
  username: string
  password: string
}

// Types pour l'organisation
export interface Entreprise {
  id: string
  nom: string
  sigle: string
  nif: string
  stat: string
  rccm: string
  adresse: string
  telephone: string
  email: string
  logo?: string
  createdAt: string
  updatedAt: string
}

export interface Direction {
  id: string
  nom: string
  code: string
  description?: string
  directionParenteId?: string
  entrepriseId: string
  responsableId?: string
  createdAt: string
  updatedAt: string
}

export interface Service {
  id: string
  nom: string
  code: string
  description?: string
  directionId: string
  responsableId?: string
  createdAt: string
  updatedAt: string
}

// Types pour les référentiels RH
export interface CategorieProfessionnelle {
  id: string
  nom: string
  code: string
  description?: string
  salaireBaseReference?: number
  createdAt: string
  updatedAt: string
}

export interface Grade {
  id: string
  nom: string
  code: string
  categorieId: string
  echelonMin: number
  echelonMax: number
  salaireBase?: number
  createdAt: string
  updatedAt: string
}

export interface TypeContrat {
  id: string
  nom: string
  code: string
  description?: string
  dureeMaxMois?: number
  renouvellementMax?: number
  createdAt: string
  updatedAt: string
}

// Types pour le personnel
export interface Employe {
  id: string
  matricule: string
  nom: string
  prenoms: string
  dateNaissance: string
  lieuNaissance: string
  sexe: 'M' | 'F'
  nationalite: string
  telephone: string
  email: string
  adresse: string
  situationFamiliale: 'C' | 'M' | 'D' | 'V'
  nombreEnfants: number
  numeroCnps?: string
  numeroCni?: string
  dateEmbauche: string
  dateSortie?: string
  motifSortie?: string
  statut: 'ACTIF' | 'SORTI' | 'SUSPENDU'
  
  // Affectation
  serviceId: string
  directionId: string
  poste?: string
  categorieId: string
  gradeId: string
  typeContratId: string
  
  // Contrat
  salaireBase: number
  categorieProfessionnelle: string
  
  createdAt: string
  updatedAt: string
}

// Types pour la paie
export interface RubriquePaie {
  id: string
  code: string
  libelle: string
  type: 'GAIN' | 'RETENUE' | 'COTISATION'
  categorie: 'SALAIRE_BASE' | 'PRIME' | 'INDemnite' | 'RETENUE_LEGAL' | 'RETENUE_AUTRE'
  calcul: 'FIXE' | 'POURCENTAGE' | 'FORMULE'
  valeur?: number
  pourcentage?: number
  formule?: string
  baseCalcul?: string
  estImposable: boolean
  estSoumisCotisations: boolean
  estPeriodique: boolean
  createdAt: string
  updatedAt: string
}

export interface BulletinPaie {
  id: string
  employeId: string
  periode: string // YYYY-MM
  dateGeneration: string
  salaireBase: number
  totalGains: number
  totalRetenues: number
  salaireNet: number
  details: DetailBulletin[]
  statut: 'BROUILLON' | 'VALIDE' | 'PAYE'
  createdAt: string
  updatedAt: string
}

export interface DetailBulletin {
  id: string
  rubriqueId: string
  rubrique: RubriquePaie
  montant: number
  baseCalcul?: number
}

// Types pour les absences
export interface TypeAbsence {
  id: string
  code: string
  libelle: string
  description?: string
  estPayant: boolean
  estJustificatifRequis: boolean
  nombreJoursMaximum?: number
  couleur: string
  createdAt: string
  updatedAt: string
}

export interface Absence {
  id: string
  employeId: string
  typeAbsenceId: string
  dateDebut: string
  dateFin: string
  nombreJours: number
  motif?: string
  statut: 'EN_ATTENTE' | 'APPROUVE' | 'REFUSE'
  approbateurId?: string
  dateApprobation?: string
  commentaireApprobation?: string
  justificatif?: string
  createdAt: string
  updatedAt: string
}

// Types pour les congés
export interface SoldeConge {
  id: string
  employeId: string
  annee: number
  soldeAcquis: number
  soldePris: number
  soldeRestant: number
  soldeAnterieur: number
  updatedAt: string
}

export interface DemandeConge {
  id: string
  employeId: string
  dateDebut: string
  dateFin: string
  nombreJours: number
  typeConge: 'ANNUEL' | 'MATERNITE' | 'MALADIE' | 'SANS_SOLDE'
  motif?: string
  statut: 'EN_ATTENTE' | 'APPROUVE' | 'REFUSE'
  approbateurId?: string
  dateApprobation?: string
  commentaireApprobation?: string
  createdAt: string
  updatedAt: string
}
