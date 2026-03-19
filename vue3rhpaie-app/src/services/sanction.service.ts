import axios from 'axios'

const API_BASE_URL = 'http://192.168.1.5:7200/api/rh/carriere/sanctions'

export interface SanctionDto {
  id: number
  faute: string
  commentaire?: string
  idTypeSanction: number
  typeSanction?: {
    id: number
    libelle: string
    description?: string
  }
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  updatedAt?: string
  createdBy?: string
  updatedBy?: string
}

export interface SanctionRequest {
  id?: number
  idTypeSanction: number
  faute: string
  commentaire?: string
}

export interface SanctionResponse<T> {
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

class SanctionService {
  private api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Récupérer la liste paginée des sanctions
  async getAll(request: PaginationRequest = {}): Promise<SanctionResponse<SanctionDto>> {
    try {
      const response = await this.api.get('/list', { params: request })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des sanctions:', error)
      throw error
    }
  }

  // Récupérer une sanction par son ID
  async getById(id: number): Promise<SanctionDto> {
    try {
      const response = await this.api.post('/trouver', { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de la sanction:', error)
      throw error
    }
  }

  // Créer une nouvelle sanction
  async create(sanction: SanctionRequest): Promise<SanctionDto> {
    try {
      const response = await this.api.post('/enregistrer', sanction)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création de la sanction:', error)
      throw error
    }
  }

  // Mettre à jour une sanction
  async update(sanction: SanctionRequest): Promise<SanctionDto> {
    try {
      const response = await this.api.post('/enregistrer', sanction)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour de la sanction:', error)
      throw error
    }
  }

  // Supprimer une sanction
  async delete(id: number): Promise<SanctionDto> {
    try {
      const response = await this.api.post('/supprimer', { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression de la sanction:', error)
      throw error
    }
  }

  // Récupérer toutes les sanctions (sans pagination)
  async getAllSanctions(): Promise<SanctionDto[]> {
    try {
      const response = await this.api.post('/lister')
      const backendResponse = response.data as any
      
      console.log('Raw backend response for sanctions:', backendResponse)
      console.log('Rows from backend:', backendResponse.rows)
      
      // Parser les objets PowerShell formatés
      let processedRows: SanctionDto[] = []
      
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
      
      console.log('Processed sanctions rows:', processedRows)
      return processedRows
    } catch (error) {
      console.error('Erreur lors de la récupération de toutes les sanctions:', error)
      throw error
    }
  }

  // Parser un objet PowerShell formaté
  private parsePowerShellObject(powerShellStr: string): SanctionDto {
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
        faute: 'Error parsing',
        commentaire: '',
        idTypeSanction: 0,
        dateCreation: new Date().toISOString()
      }
    }
  }

  // Nettoyer un objet pour correspondre à SanctionDto
  private cleanObject(obj: any): SanctionDto {
    return {
      id: parseInt(obj.id) || 0,
      faute: obj.faute || '',
      commentaire: obj.commentaire || '',
      idTypeSanction: parseInt(obj.idTypeSanction) || 0,
      dateCreation: obj.dateCreation || obj.createdAt || new Date().toISOString(),
      createdAt: obj.createdAt,
      updatedAt: obj.updatedAt,
      createdBy: obj.createdBy,
      updatedBy: obj.updatedBy
    }
  }

  // Récupérer les types de sanctions disponibles
  async getTypesSanctions(): Promise<any[]> {
    try {
      const response = await this.api.get('/types-sanctions')
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des types de sanctions:', error)
      throw error
    }
  }
}

export default new SanctionService()
