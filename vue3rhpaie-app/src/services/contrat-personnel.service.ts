import { api } from './api'

export interface ContratPersonnel {
  id: number
  personnel: Personnel
  typeContrat: string | { id: number; libelle: string }
  fonction: string | { id: number; libelle: string }
  dateDebut: string
  dateFin: string
  salaireCategoriel: number
  netAPayer: number
  statut: boolean
  categorie: { id: number; libelle: string; salaireBase: string | number }
  updatedAt?: string
  updatedBy?: string
}

export interface Personnel {
  id: number
  matricule: string
  nom: string
  prenom: string
  sexe: string
  cnps: string
  situationMatrimoniale: string
  nombreEnfant: number
}

export interface ContratPersonnelDTO {
  result: boolean | null
  data: ContratPersonnel[]
  rows: ContratPersonnel[]
  total: number
  status: boolean
  message: string | null
  errors: any
}

export interface ContratPersonnelRequest {
  id?: number
  idPersonnel: number
  idCategorie: number
  idFonction: number
  idTypeContrat: number
  dateDebut: string
  dateFin?: string
  netAPayer: number
  indemniteLogement: number
  ancienete: number
  sursalaire: number
  indemnitetransport: number
  indemniterespons: number
  indemniterepresent: number
}

export interface EndContractRequest {
  id: number
  dateFin: string
  dateMod: string
  permanent: boolean  // Corrigé: boolean au lieu de string
  observCtrat: string
}

export interface SursalaireRequest {
  idPersonnel: number
  sursalaire: number
}

export interface PaginationRequest {
  offset?: number
  limit?: number
  search?: string
}

export interface ContratPersonnelFilterRequest extends PaginationRequest {
  statut?: string // "active", "inactive", "expiresOnDate", "expiresInPeriod"
  typeContrat?: string // "CDI", "CDD", "Stage", "Apprentissage" (simple, déprécié)
  typeContrats?: string[] // Liste de types de contrats (choix multiple)
  salaireRange?: string // "low", "medium", "high", "veryhigh"
  carec?: boolean // État contractuel de l'employé
  expireDate?: string // Date d'expiration spécifique (YYYY-MM-DD)
  expireDateMax?: string // Date max d'expiration (dateFin <= YYYY-MM-DD)
  expirePeriodStart?: string // Début de période d'expiration (YYYY-MM-DD)
  expirePeriodEnd?: string // Fin de période d'expiration (YYYY-MM-DD)
}

export interface PaginationIdRequest extends PaginationRequest {
  id: number
}

export interface IdRequest {
  id: number
}

class ContratPersonnelService {
  private baseUrl = '/personnels' // Le baseURL est déjà configuré dans api.ts
  private useMockData = false // Mode fallback si le backend n'est pas accessible

  // Mode mock pour le développement
  private getMockData(): ContratPersonnelDTO {
    return {
      result: true,
      data: [
        {
          id: 1,
          personnel: {
            id: 1,
            matricule: 'EMP001',
            nom: 'Kouadio',
            prenom: 'Jean',
            sexe: 'Masculin',
            cnps: 'CNPS001',
            situationMatrimoniale: 'Marié',
            nombreEnfant: 2
          },
          typeContrat: 'CDI',
          fonction: 'Développeur Senior',
          dateDebut: '2023-01-15',
          dateFin: '2025-01-15',
          salaireCategoriel: 500000,
          netAPayer: 450000,
          statut: true,
          categorie: { id: 1, libelle: 'M1', salaireBase: '500000' },
          updatedAt: '2025-12-31T08:39:58.024958',
          updatedBy: 'admin'
        },
        {
          id: 2,
          personnel: {
            id: 2,
            matricule: 'EMP002',
            nom: 'Konan',
            prenom: 'Marie',
            sexe: 'Féminin',
            cnps: 'CNPS002',
            situationMatrimoniale: 'Célibataire',
            nombreEnfant: 0
          },
          typeContrat: 'CDD',
          fonction: 'Comptable',
          dateDebut: '2024-03-01',
          dateFin: '2024-12-31',
          salaireCategoriel: 350000,
          netAPayer: 320000,
          statut: true,
          categorie: { id: 2, libelle: 'M2', salaireBase: '148876' },
          updatedAt: '2025-12-31T08:40:07.526178',
          updatedBy: 'admin'
        }
      ],
      rows: [
        {
          id: 1,
          personnel: {
            id: 1,
            matricule: 'EMP001',
            nom: 'Kouadio',
            prenom: 'Jean',
            sexe: 'Masculin',
            cnps: 'CNPS001',
            situationMatrimoniale: 'Marié',
            nombreEnfant: 2
          },
          typeContrat: 'CDI',
          fonction: 'Développeur Senior',
          dateDebut: '2023-01-15',
          dateFin: '2025-01-15',
          salaireCategoriel: 500000,
          netAPayer: 450000,
          statut: true,
          categorie: { id: 1, libelle: 'M1', salaireBase: '500000' },
          updatedAt: '2025-12-31T08:39:58.024958',
          updatedBy: 'admin'
        },
        {
          id: 2,
          personnel: {
            id: 2,
            matricule: 'EMP002',
            nom: 'Konan',
            prenom: 'Marie',
            sexe: 'Féminin',
            cnps: 'CNPS002',
            situationMatrimoniale: 'Célibataire',
            nombreEnfant: 0
          },
          typeContrat: 'CDD',
          fonction: 'Comptable',
          dateDebut: '2024-03-01',
          dateFin: '2024-12-31',
          salaireCategoriel: 350000,
          netAPayer: 320000,
          statut: true,
          categorie: { id: 2, libelle: 'M2', salaireBase: '148876' },
          updatedAt: '2025-12-31T08:40:07.526178',
          updatedBy: 'admin'
        }
      ],
      total: 2,
      status: true,
      message: null,
      errors: null
    }
  }

  // Récupérer les contrats d'un personnel avec pagination
  async getContratsByPersonnel(personnelId: number, pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratparpersonneljson`, {
      id: personnelId,
      ...pagination
    })
    return response.data
  }

  // Récupérer tous les contrats actifs avec pagination
  async getContratsActifs(pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonnelActifjson`, pagination)
    return response.data
  }

  // Récupérer tous les contrats avec pagination
  async getAllContrats(pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonneljson`, pagination)
    return response.data
  }

  // Récupérer les contrats avec filtres multiples
  async getContratsWithFilters(filters: ContratPersonnelFilterRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonnelfilterjson`, filters)
    return response.data
  }

  // Récupérer les contrats expirés avec pagination
  async getContratsExpires(pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonnelDepartjson`, pagination)
    return response.data
  }

  // Récupérer les contrats expirés par mois
  async getContratsExpiresParMois(periodeId: number, pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonnelExpjson`, {
      id: periodeId,
      ...pagination
    })
    return response.data
  }

  // Récupérer les contrats expirés par période
  async getContratsExpiresParPeriode(periodeId: number, search: string, pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonnelExpPeriodejson`, {
      id: periodeId,
      search,
      ...pagination
    })
    return response.data
  }

  // Récupérer les contrats expirés par date
  async getContratsExpiresParDate(dateDebut: string, dateFin: string, pagination: PaginationRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listcontratpersonnelExpDatejson`, {
      id: dateDebut,
      search: dateFin,
      ...pagination
    })
    return response.data
  }

  // Récupérer les contrats qui expirent bientôt
  async getContratsExpirantBientot(): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/listexpirecontratpersonnel`)
    return response.data
  }

  // Récupérer les contrats qui expirent dans N jours
  async getContratsExpirantDansDelai(delai: number): Promise<ContratPersonnel[]> {
    const response = await api.post(`${this.baseUrl}/listexpirecontratpersonnelDelai`, { nbre: delai })
    return response.data
  }

  // Récupérer les contrats d'un personnel (sans pagination)
  async getContratsByPersonnelList(personnelId: number): Promise<ContratPersonnel[]> {
    const response = await api.post(`${this.baseUrl}/listcontratparpersonnel`, { id: personnelId })
    return response.data
  }

  // Rechercher un contrat actif par personnel
  async getContratActifByPersonnel(personnelId: number): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/cherchcontratpersonnelActif`, { id: personnelId })
    return response.data
  }

  // Sauvegarder un contrat
  async saveContrat(request: ContratPersonnelRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/savecontratpersonnel`, request)
    return response.data
  }

  // Mettre fin à un contrat
  async endContract(request: EndContractRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/endcontratpersonnel`, request)
    return response.data
  }

  // Mettre à jour le sursalaire d'un contrat
  async updateSursalaire(request: SursalaireRequest): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/updatecontratpersonnelSursal`, request)
    return response.data
  }

  // Supprimer un contrat
  async deleteContrat(contratId: number): Promise<ContratPersonnelDTO> {
    const response = await api.post(`${this.baseUrl}/deletecontratpersonnel`, { id: contratId })
    return response.data
  }
}

export const contratPersonnelService = new ContratPersonnelService()
