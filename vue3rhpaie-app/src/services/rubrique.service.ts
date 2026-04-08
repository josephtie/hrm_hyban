import axios from 'axios'

const API_BASE_URL = 'http://192.168.1.8:7200/api/parametrages/rubriques'

export interface RubriqueDto {
  id: number
  code: string
  libelle: string
  type: string
  categorie: string
  formule?: string
  taux?: number
  montant?: number
  mtExedent?: number
  imposable: boolean
  cotisable: boolean
  active: boolean
  afficherBulletin: boolean
  description?: string
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  updatedAt?: string
  createdBy?: string
  updatedBy?: string
}

export interface RubriqueRequest {
  id?: number
  code: string
  libelle: string
  type: string
  categorie: string
  formule?: string
  taux?: number
  montant?: number
  mtExedent?: number
  imposable: boolean
  cotisable: boolean
  active: boolean
  afficherBulletin: boolean
  description?: string
}

export interface RubriqueResponse<T> {
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

class RubriqueService {
  private api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Récupérer la liste paginée des rubriques
  async getAll(request: PaginationRequest = {}): Promise<RubriqueResponse<RubriqueDto>> {
    try {
      const response = await this.api.get('/list', { params: request })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des rubriques:', error)
      throw error
    }
  }

  // Récupérer une rubrique par son ID
  async getById(id: number): Promise<RubriqueDto> {
    try {
      const response = await this.api.post('/trouver', { idR: id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de la rubrique:', error)
      throw error
    }
  }

  // Créer une nouvelle rubrique
  async create(rubrique: RubriqueRequest): Promise<RubriqueDto> {
    try {
      const response = await this.api.post('/enregistrer', rubrique)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création de la rubrique:', error)
      throw error
    }
  }

  // Mettre à jour une rubrique
  async update(rubrique: RubriqueRequest): Promise<RubriqueDto> {
    try {
      const response = await this.api.post('/enregistrer', rubrique)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour de la rubrique:', error)
      throw error
    }
  }

  // Supprimer une rubrique
  async delete(id: number): Promise<RubriqueDto> {
    try {
      const response = await this.api.post('/supprimer', { idR: id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression de la rubrique:', error)
      throw error
    }
  }

  // Récupérer toutes les rubriques (sans pagination)
  async getAllRubriques(): Promise<RubriqueDto[]> {
    try {
      const response = await this.api.post('/lister')
      const backendResponse = response.data as any
      
      console.log('Raw backend response for rubriques:', backendResponse)
      
      // Parser les objets PowerShell formatés
      let processedRows: RubriqueDto[] = []
      
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
      
      console.log('Processed rubriques rows:', processedRows)
      return processedRows
    } catch (error) {
      console.error('Erreur lors de la récupération de toutes les rubriques:', error)
      throw error
    }
  }

  // Parser un objet PowerShell formaté
  private parsePowerShellObject(powerShellStr: string): RubriqueDto {
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
        code: 'Error parsing',
        libelle: 'Error parsing',
        type: 'FIXE',
        categorie: 'GAIN',
        formule: '',
        taux: 0,
        montant: 0,
        imposable: true,
        cotisable: true,
        afficherBulletin: true,
        description: '',
        dateCreation: new Date().toISOString()
      }
    }
  }

  // Nettoyer un objet pour correspondre à RubriqueDto
  private cleanObject(obj: any): RubriqueDto {
    return {
      id: parseInt(obj.id) || 0,
      code: obj.code || '',
      libelle: obj.libelle || '',
      type: obj.type || 'FIXE',
      categorie: obj.categorie || 'GAIN',
      formule: obj.formule || '',
      taux: parseFloat(obj.taux) || 0,
      montant: parseFloat(obj.montant) || 0,
      imposable: obj.imposable === 'true' || obj.imposable === true,
      cotisable: obj.cotisable === 'true' || obj.cotisable === true,
      afficherBulletin: obj.afficherBulletin === 'true' || obj.afficherBulletin === true,
      description: obj.description || '',
      dateCreation: obj.dateCreation || obj.createdAt || new Date().toISOString(),
      createdAt: obj.createdAt,
      updatedAt: obj.updatedAt,
      createdBy: obj.createdBy,
      updatedBy: obj.updatedBy
    }
  }
}

export default new RubriqueService()
