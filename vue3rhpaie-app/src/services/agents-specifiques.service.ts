import { api } from './api'

export type SpecialContractType = 'STAFF_PDG' | 'DOZO' | 'STAGE' | 'AGENT_SECURITE'

export interface AgentSpecifique {
  id?: number
  matricule?: string
  nom?: string
  prenom?: string
  nomComplet?: string
  sexe?: string
  situationMatrimoniale?: number
  nombrEnfant?: number
  lieuHabitation?: string
  dofbrid?: string
  phoneNumber?: string
  email?: string
  actif?: boolean
  categorieSpeciale?: string
  nationnalite?: { id?: number; libelle?: string }
  fonction?: string
  netapayer?: string
}

export interface SpecialContract {
  id?: number
  employee?: AgentSpecifique
  typeContrat?: SpecialContractType
  fonction?: { id?: number; libelle?: string }
  site?: { id?: number; libelle?: string }
  modepaiement?: string
  paiementNumber?: string
  dateDebut?: string
  dateFin?: string
  dDeb?: string
  dFin?: string
  remunerationForfaitaire?: number
  actif?: boolean
}

export interface AgentSpecifiqueForm {
  idEmpl?: number
  matricule: string
  nom: string
  prenom: string
  sexe: string
  situationMatrimoniale: number
  nationalite: number
  lieuHabitation?: string
  dofbrid?: string
  phoneNumber?: string
  typeContrat: SpecialContractType
  fonction: number
  site: number
  dDeb: string
  dFin?: string
  modepaiement: string
  paiementNumber: string
  netpayer: number
}

export interface PaginatedResponse<T> {
  rows: T[]
  total: number
  row?: T
  data?: T[]
  result?: unknown
  status?: boolean
  message?: string
}

export interface ReferenceItem {
  id: number
  libelle?: string
  code?: string
  description?: string
}

const BASE_URL = '/personnels/specifique'

class AgentsSpecifiquesService {
  async list(params: { limit?: number; offset?: number; search?: string } = {}): Promise<PaginatedResponse<AgentSpecifique>> {
    const response = await api.get<PaginatedResponse<AgentSpecifique>>(`${BASE_URL}/listemployeejson`, { params })
    return response.data
  }

  async save(payload: AgentSpecifiqueForm): Promise<PaginatedResponse<SpecialContract>> {
    const formData = new URLSearchParams()
    Object.entries(payload).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        formData.append(key, String(value))
      }
    })

    const response = await api.post<PaginatedResponse<SpecialContract>>(`${BASE_URL}/enregisteremployee`, formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
    return response.data
  }

  async deactivate(id: number): Promise<PaginatedResponse<AgentSpecifique>> {
    const formData = new URLSearchParams()
    formData.append('idemp', String(id))
    const response = await api.post<PaginatedResponse<AgentSpecifique>>(`${BASE_URL}/deactivate`, formData, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
    return response.data
  }

  async getContractByEmployee(employeeId: number): Promise<PaginatedResponse<SpecialContract>> {
    const response = await api.get<PaginatedResponse<SpecialContract>>(`${BASE_URL}/special-contracts/employee/${employeeId}`)
    return response.data
  }

  async getNationalites(): Promise<ReferenceItem[]> {
    const response = await api.get<ReferenceItem[] | PaginatedResponse<ReferenceItem>>('/nationalites')
    const data = response.data
    return Array.isArray(data) ? data : data.data || data.rows || []
  }

  async getFonctions(): Promise<ReferenceItem[]> {
    const response = await api.post<PaginatedResponse<ReferenceItem>>('/personnels/fonctions/listfonctionjson', {
      limit: 1000,
      offset: 0
    })
    return response.data.rows || []
  }

  async getSites(): Promise<ReferenceItem[]> {
    const response = await api.post<PaginatedResponse<ReferenceItem>>('/rh/carriere/site/lister')
    return response.data.rows || []
  }
}

export const agentsSpecifiquesService = new AgentsSpecifiquesService()
export default agentsSpecifiquesService
