import { api } from './api'

export interface HeureSupplementaire {
  id?: number
  personnel: { id: number; nom: string; prenom: string; matricule: string; nomComplet?: string }
  dateTravail: string
  nombreHeures: number
  typeHS: 'HS_15' | 'HS_50' | 'HS_75' | 'HS_100'
  tauxMajoration?: number
  tauxHoraire?: number
  coefficient?: number
  montant?: number
  motif?: string
  commentaire?: string
  periodePaie: { id: number; datedeb?: string; datefin?: string; ddeb?: string; dfin?: string }
  statut: 'BROUILLON' | 'A_VALIDER' | 'REJETE' | 'VALIDE' | 'INTEGRE_PAIE'
  motifRejet?: string
  dateSoumission?: string
  dateValidation?: string
  dateIntegrationPaie?: string
  validatedBy?: string
  createdBy?: string
}

export interface RegleHeureSupplementaire {
  id?: number
  code: string
  libelle: string
  typeHS: 'HS_15' | 'HS_50' | 'HS_75' | 'HS_100'
  tauxMajoration: number
  coefficient: number
  active: boolean
}

export interface HeureSuppFilters {
  employeId?: number
  periodePaie?: number
  statut?: string
  dateDebut?: string
  dateFin?: string
}

const BASE_URL = '/heures-supplementaires'

export const heureSupplementaireService = {
  async getAll(filters?: HeureSuppFilters): Promise<HeureSupplementaire[]> {
    const params: Record<string, any> = {}
    if (filters) {
      if (filters.employeId) params.employeId = filters.employeId
      if (filters.periodePaie) params.periodePaie = filters.periodePaie
      if (filters.statut) params.statut = filters.statut
      if (filters.dateDebut) params.dateDebut = filters.dateDebut
      if (filters.dateFin) params.dateFin = filters.dateFin
    }
    const { data } = await api.get<HeureSupplementaire[]>(BASE_URL, { params })
    return data
  },

  async getById(id: number): Promise<HeureSupplementaire> {
    const { data } = await api.get<HeureSupplementaire>(`${BASE_URL}/${id}`)
    return data
  },

  async create(hs: HeureSupplementaire): Promise<HeureSupplementaire> {
    const { data } = await api.post<HeureSupplementaire>(BASE_URL, hs)
    return data
  },

  async update(id: number, hs: HeureSupplementaire): Promise<HeureSupplementaire> {
    const { data } = await api.put<HeureSupplementaire>(`${BASE_URL}/${id}`, hs)
    return data
  },

  async soumettre(id: number): Promise<HeureSupplementaire> {
    const { data } = await api.post<HeureSupplementaire>(`${BASE_URL}/${id}/soumettre`)
    return data
  },

  async valider(id: number): Promise<HeureSupplementaire> {
    const { data } = await api.post<HeureSupplementaire>(`${BASE_URL}/${id}/valider`)
    return data
  },

  async rejeter(id: number, motifRejet: string): Promise<HeureSupplementaire> {
    const { data } = await api.post<HeureSupplementaire>(`${BASE_URL}/${id}/rejeter`, { motifRejet })
    return data
  },

  async integrerPaie(id: number): Promise<HeureSupplementaire> {
    const { data } = await api.post<HeureSupplementaire>(`${BASE_URL}/${id}/integrer-paie`)
    return data
  },

  async delete(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/${id}`)
  },

  async getRegles(): Promise<RegleHeureSupplementaire[]> {
    const { data } = await api.get<RegleHeureSupplementaire[]>(`${BASE_URL}/regles`)
    return data
  },
}
