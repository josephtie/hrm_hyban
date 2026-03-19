import { api } from './api'

const API_BASE_URL = '/parametrages/banques'

export interface BanqueDto {
  id: number
  sigle: string
  libelle: string
  codeBanque?: string
  rib?: number
  statut?: boolean
  description?: string
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  updatedAt?: string
  createdBy?: string
  updatedBy?: string
}

export interface BanqueRequest {
  id?: number
  sigle: string
  libelle: string
  codeBanque?: string
  rib?: number
  statut?: boolean
  description?: string
}

export interface BanqueResponse<T> {
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

class BanqueService {
  // Utiliser l'instance API principale qui inclut les headers d'authentification
  private api = api

  // Récupérer la liste paginée des banques
  async getAll(request: PaginationRequest = {}): Promise<BanqueResponse<BanqueDto>> {
    try {
      const response = await this.api.get(`${API_BASE_URL}/list`, { params: request })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des banques:', error)
      throw error
    }
  }

  // Récupérer une banque par son ID
  async getById(id: number): Promise<BanqueDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/trouver`, { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de la banque:', error)
      throw error
    }
  }

  // Créer une nouvelle banque
  async create(banque: BanqueRequest): Promise<BanqueDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/save`, banque)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création de la banque:', error)
      throw error
    }
  }

  // Mettre à jour une banque
  async update(banque: BanqueRequest): Promise<BanqueDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/save`, banque)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour de la banque:', error)
      throw error
    }
  }

  // Supprimer une banque
  async delete(id: number): Promise<BanqueDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/supprimer`, { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression de la banque:', error)
      throw error
    }
  }

  // Récupérer toutes les banques (sans pagination)
  async getAllBanques(): Promise<BanqueDto[]> {
    try {
      const response = await this.api.get(`${API_BASE_URL}/lister`)
      const backendResponse = response.data as any
      
      console.log('Raw backend response for banques:', backendResponse)
      
      // Parser les objets PowerShell formatés
      let processedRows: BanqueDto[] = []
      
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
      
      console.log('Processed banques rows:', processedRows)
      return processedRows
    } catch (error) {
      console.error('Erreur lors de la récupération de toutes les banques:', error)
      throw error
    }
  }

  // Parser un objet PowerShell formaté
  private parsePowerShellObject(powerShellStr: string): BanqueDto {
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
        sigle: 'Error parsing',
        libelle: 'Error parsing',
        codeBanque: '',
        rib: 0,
        statut: true,
        description: '',
        dateCreation: new Date().toISOString()
      }
    }
  }

  // Nettoyer un objet pour correspondre à BanqueDto
  private cleanObject(obj: any): BanqueDto {
    return {
      id: parseInt(obj.id) || 0,
      sigle: obj.sigle || '',
      libelle: obj.libelle || '',
      codeBanque: obj.codeBanque || obj.codbanq || '',
      rib: parseInt(obj.rib) || 0,
      statut: obj.statut === 'true' || obj.statut === true,
      description: obj.description || '',
      dateCreation: obj.dateCreation || obj.createdAt || new Date().toISOString(),
      createdAt: obj.createdAt,
      updatedAt: obj.updatedAt,
      createdBy: obj.createdBy,
      updatedBy: obj.updatedBy
    }
  }
}

export default new BanqueService()
