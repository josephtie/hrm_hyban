import { api } from './api'

const API_BASE_URL = '/parametrages/periodes'

export interface PeriodePaieDto {
  id: number
  mois: string
  datedeb: string
  datefin: string
  datecloture?: string
  affiche?: string
  exerciceId?: number
  description?: string
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  updatedAt?: string
  createdBy?: string
  updatedBy?: string
}

export interface PeriodePaieRequest {
  id?: number
  mois: string
  datedeb: string
  datefin: string
  datecloture?: string
  affiche?: string
  exerciceId?: number
  description?: string
}

export interface PeriodePaieResponse<T> {
  rows: T[]
  total: number
  result: string
  message?: string
}

export interface PaginationRequest {
  offset?: number
  limit?: number
  search?: string
}

class PeriodePaieService {
  // Utiliser l'instance API principale qui inclut les headers d'authentification
  private api = api

  // Récupérer la liste paginée des périodes de paie
  async getAll(request: PaginationRequest = {}): Promise<PeriodePaieResponse<PeriodePaieDto>> {
    try {
      const response = await this.api.get(`${API_BASE_URL}/list`, { params: request })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des périodes de paie:', error)
      throw error
    }
  }

  // Récupérer une période de paie par son ID
  async getById(id: number): Promise<PeriodePaieDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/trouver`, { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de la période de paie:', error)
      throw error
    }
  }

  // Créer une nouvelle période de paie
  async create(periode: PeriodePaieRequest): Promise<PeriodePaieDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/save`, {
        mois: periode.mois,
        affiche: periode.affiche || `Période ${periode.mois}`
      })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création de la période de paie:', error)
      throw error
    }
  }

  // Mettre à jour une période de paie
  async update(periode: PeriodePaieRequest): Promise<PeriodePaieDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/cloturer`, {
        id: periode.id
      })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour de la période de paie:', error)
      throw error
    }
  }

  // Supprimer une période de paie
  async delete(id: number): Promise<PeriodePaieDto> {
    try {
      // Le contrôleur n'a pas d'endpoint delete, on utilise cloturer pour l'instant
      const response = await this.api.post(`${API_BASE_URL}/cloturer`, { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression de la période de paie:', error)
      throw error
    }
  }

  // Récupérer toutes les périodes de paie (sans pagination)
  async getAllPeriodesPaie(): Promise<PeriodePaieDto[]> {
    try {
      // Essayer d'abord avec l'endpoint GET /list qui existe
      const response = await this.api.get(`${API_BASE_URL}/list`, { 
        params: { limit: 100, offset: 0 }
      })
      const backendResponse = response.data as any
      
      console.log('Raw backend response for periodes paie:', backendResponse)
      
      // Parser les objets PowerShell formatés
      let processedRows: PeriodePaieDto[] = []
      
      if (backendResponse.rows && Array.isArray(backendResponse.rows)) {
        processedRows = backendResponse.rows.map((item: any) => {
          // Si c'est une chaîne de caractères (objet PowerShell), la parser
          if (typeof item === 'string') {
            return this.parsePowerShellObject(item)
          }
          // Si c'est déjà un objet, le nettoyer
          return this.cleanObject(item)
        })
      }
      
      console.log('Processed periodes paie rows:', processedRows)
      return processedRows
    } catch (error) {
      console.error('Erreur lors de la récupération de toutes les périodes de paie:', error)
      throw error
    }
  }

  // Parser un objet PowerShell formaté
  private parsePowerShellObject(powerShellStr: string): PeriodePaieDto {
    try {
      // Remplacer les @{} par des {} pour le formatage JSON
      const cleanStr = powerShellStr.replace(/@{/g, '{').replace(/}$/g, '}')
      
      // Parser les paires clé=valeur
      const obj: any = {}
      const pairs = cleanStr.slice(1, -1).split(';')
      
      pairs.forEach(pair => {
        const [key, ...valueParts] = pair.split('=')
        const value = valueParts.join('=')
        if (key && value) {
          const cleanKey = key.trim()
          let cleanValue = value.trim()
          
          // Nettoyer les guillemets si présents
          if (cleanValue.startsWith('"') && cleanValue.endsWith('"')) {
            cleanValue = cleanValue.slice(1, -1)
          }
          
          obj[cleanKey] = cleanValue
        }
      })
      
      return this.cleanObject(obj)
    } catch (error) {
      console.error('Error parsing PowerShell object:', powerShellStr, error)
      return {
        id: 0,
        libelle: 'Error parsing',
        dateDebut: new Date().toISOString(),
        dateFin: new Date().toISOString(),
        statut: 'OUVERTE',
        exerciceId: 0,
        description: '',
        dateCreation: new Date().toISOString()
      }
    }
  }

  // Nettoyer un objet pour correspondre à PeriodePaieDto
  private cleanObject(obj: any): PeriodePaieDto {
    return {
      id: parseInt(obj.id) || 0,
      mois: obj.mois?.id ? obj.mois.id.toString() : obj.mois || '',
      datedeb: obj.datedeb || new Date().toISOString(),
      datefin: obj.datefin || new Date().toISOString(),
      datecloture: obj.datecloture,
      affiche: obj.affiche,
      exerciceId: obj.annee?.id || 0,  // Adapter depuis l'entité Exercice
      description: obj.affiche || '',
      dateCreation: obj.dateCreation || obj.createdAt || new Date().toISOString(),
      dateModification: obj.dateModification || obj.updatedAt,
      createdAt: obj.createdAt,
      updatedAt: obj.updatedAt,
      createdBy: obj.createdBy,
      updatedBy: obj.updatedBy
    }
  }

  // Récupérer la période active
  async getPeriodeActive(): Promise<PeriodePaieDto> {
    try {
      const response = await this.api.get('/active')
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de la période active:', error)
      throw error
    }
  }
}

export default new PeriodePaieService()
