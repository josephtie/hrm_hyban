import { api } from './api'

const API_BASE_URL = '/parametrages/exercices'

export interface ExerciceDto {
  id: number
  annee: string
  moisDebut: string
  moisFin: string
  statut: 'OUVERT' | 'CLOTURE'
  description?: string
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  updatedAt?: string
  createdBy?: string
  updatedBy?: string
}

export interface ExerciceRequest {
  id?: number
  annee: string
  moisDebut: string
  moisFin: string
  statut: 'OUVERT' | 'CLOTURE'
  description?: string
}

export interface ExerciceResponse<T> {
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

class ExerciceService {
  // Utiliser l'instance API principale qui inclut les headers d'authentification
  private api = api

  // Récupérer la liste paginée des exercices
  async getAll(request: PaginationRequest = {}): Promise<ExerciceResponse<ExerciceDto>> {
    try {
      const response = await this.api.get(`${API_BASE_URL}/list`, { params: request })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des exercices:', error)
      throw error
    }
  }

  // Récupérer un exercice par son ID
  async getById(id: number): Promise<ExerciceDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/trouver`, { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de l\'exercice:', error)
      throw error
    }
  }

  // Créer un nouvel exercice (utilise le même endpoint que update)
  async create(exercice: ExerciceRequest): Promise<ExerciceDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/enregistrer`, exercice)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création de l\'exercice:', error)
      throw error
    }
  }

  // Mettre à jour un exercice
  async update(exercice: ExerciceRequest): Promise<ExerciceDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/enregistrer`, exercice)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour de l\'exercice:', error)
      throw error
    }
  }

  // Supprimer un exercice (à implémenter si nécessaire dans le backend)
  async delete(id: number): Promise<ExerciceDto> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/supprimer`, { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression de l\'exercice:', error)
      throw error
    }
  }

  // Récupérer tous les exercices (sans pagination)
  async getAllExercices(): Promise<ExerciceDto[]> {
    try {
      const response = await this.api.post(`${API_BASE_URL}/lister`)
      const backendResponse = response.data as any
      
      console.log('Raw backend response for exercices:', backendResponse)
      
      // Parser les objets PowerShell formatés
      let processedRows: ExerciceDto[] = []
      
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
      
      console.log('Processed exercices rows:', processedRows)
      return processedRows
    } catch (error) {
      console.error('Erreur lors de la récupération de tous les exercices:', error)
      throw error
    }
  }

  // Récupérer l'exercice actif
  async getExerciceActif(): Promise<ExerciceDto> {
    try {
      const response = await this.api.get(`${API_BASE_URL}/actif`)
      const backendResponse = response.data as any
      
      console.log('Raw backend response for exercice actif:', backendResponse)
      
      // Parser la réponse qui contient row et rows
      if (backendResponse.row) {
        const processedRow = this.cleanObject(backendResponse.row)
        console.log('Processed exercice actif:', processedRow)
        return processedRow
      }
      
      // Fallback si pas de row
      return {} as ExerciceDto
    } catch (error) {
      console.error('Erreur lors de la récupération de l\'exercice actif:', error)
      throw error
    }
  }

  // Parser un objet PowerShell formaté
  private parsePowerShellObject(powerShellStr: string): ExerciceDto {
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
        annee: 'Error parsing',
        moisDebut: '1',
        moisFin: '12',
        statut: 'OUVERT',
        description: '',
        dateCreation: new Date().toISOString()
      }
    }
  }

  // Nettoyer un objet pour correspondre à ExerciceDto
  private cleanObject(obj: any): ExerciceDto {
    return {
      id: parseInt(obj.id) || 0,
      annee: obj.annee || '',
      moisDebut: obj.moisDebut || '1',
      moisFin: obj.moisFin || '12',
      statut: obj.statut || 'OUVERT',
      description: obj.description || '',
      dateCreation: obj.dateCreation || obj.createdAt || new Date().toISOString(),
      createdAt: obj.createdAt,
      updatedAt: obj.updatedAt,
      createdBy: obj.createdBy,
      updatedBy: obj.updatedBy
    }
  }

  // Récupérer l'exercice actif
  // async getExerciceActif(): Promise<ExerciceDto> {
  //   try {
  //     const response = await this.api.get('/actif')
  //     return response.data
  //   } catch (error) {
  //     console.error('Erreur lors de la récupération de l\'exercice actif:', error)
  //     throw error
  //   }
  // }

  // Clôturer un exercice
  async cloturerExercice(request: ExerciceRequest): Promise<ExerciceDto> {
    try {
      const response = await this.api.post('/cloturer', request)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la clôture de l\'exercice:', error)
      throw error
    }
  }
}

export default new ExerciceService()
