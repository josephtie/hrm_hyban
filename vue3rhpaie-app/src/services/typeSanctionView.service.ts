import axios from 'axios'

const API_BASE_URL = 'http://192.168.1.8:7200/api/rh/carriere/types-sanctions'

export interface TypeSanctionDto {
  id: number
  libelle: string
  description?: string
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  updatedAt?: string
  createdBy?: string
  updatedBy?: string
  dCreation?: string
  dModification?: string
}

export interface TypeSanctionRequest {
  id?: number
  libelle: string
  description?: string
}

export interface TypeSanctionResponse<T> {
  rows: T[]
  total: number
  result: string
  status?: boolean
  message?: string
  errors?: any
}

export interface PaginationRequest {
  offset?: number
  limit?: number
  search?: string
}

export interface TypeSanctionBackendResponse {
  row: TypeSanctionDto
  rows: TypeSanctionDto[]
  total: number
  result: boolean
  status: boolean
  message: string
  errors: any
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

class TypeSanctionViewService {
  private api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Récupérer la liste paginée des types de sanctions
  async getAll(request: PaginationRequest = {}): Promise<TypeSanctionResponse<TypeSanctionDto>> {
    try {
      const response = await this.api.get('/list', { params: request })
      
      // Adapter la réponse du backend au format attendu
      const backendResponse = response.data as any
      
      // La réponse contient rows[0] qui est un tableau des types de sanctions
      // Mais rows[0] peut être un tableau d'objets PowerShell formatés
      let actualRows = []
      
      if (backendResponse.rows && Array.isArray(backendResponse.rows)) {
        if (backendResponse.rows.length > 0 && Array.isArray(backendResponse.rows[0])) {
          actualRows = backendResponse.rows[0]
        } else if (backendResponse.rows.length > 0) {
          actualRows = backendResponse.rows
        }
      }
      
      // Si les données sont formatées comme des objets PowerShell, les parser
      if (actualRows.length > 0 && typeof actualRows[0] === 'string') {
        actualRows = actualRows.map((item: string) => {
          // Parser l'objet PowerShell formaté
          const cleanItem = item.replace(/@{/g, '{').replace(/}/g, '}')
          try {
            return JSON.parse(cleanItem)
          } catch {
            // Fallback si le parsing échoue
            return {}
          }
        })
      }
      
      return {
        rows: actualRows,
        total: backendResponse.total || 0,
        result: backendResponse.result || 'success',
        status: backendResponse.status,
        message: backendResponse.message
      }
    } catch (error) {
      console.error('Erreur lors de la récupération des types de sanctions:', error)
      throw error
    }
  }

  // Récupérer un type de sanction par son ID
  async getById(id: number): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/trouver', { id })
      return response.data.row || response.data
    } catch (error) {
      console.error('Erreur lors de la récupération du type de sanction:', error)
      throw error
    }
  }

  // Créer un nouveau type de sanction
  async create(typeSanction: TypeSanctionRequest): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/enregistrer', typeSanction)
      return response.data.row || response.data
    } catch (error) {
      console.error('Erreur lors de la création du type de sanction:', error)
      throw error
    }
  }

  // Mettre à jour un type de sanction
  async update(typeSanction: TypeSanctionRequest): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/enregistrer', typeSanction)
      return response.data.row || response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour du type de sanction:', error)
      throw error
    }
  }

  // Supprimer un type de sanction
  async delete(id: number): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/supprimer', { id })
      return response.data.row || response.data
    } catch (error) {
      console.error('Erreur lors de la suppression du type de sanction:', error)
      throw error
    }
  }

  // Récupérer tous les types de sanctions (sans pagination)
  async getAllTypeSanctions(): Promise<TypeSanctionDto[]> {
    try {
      const response = await this.api.post('/lister')
      const backendResponse = response.data as any
      
      console.log('Raw backend response:', backendResponse)
      console.log('Rows from backend:', backendResponse.rows)
      
      // Parser les objets PowerShell formatés
      let processedRows: TypeSanctionDto[] = []
      
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
      
      console.log('Processed rows:', processedRows)
      return processedRows
    } catch (error) {
      console.error('Erreur lors de la récupération de tous les types de sanctions:', error)
      throw error
    }
  }

  // Parser un objet PowerShell formaté
  private parsePowerShellObject(powerShellStr: string): TypeSanctionDto {
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
        description: '',
        dateCreation: new Date().toISOString()
      }
    }
  }

  // Nettoyer un objet pour correspondre à TypeSanctionDto
  private cleanObject(obj: any): TypeSanctionDto {
    // Gestion robuste des dates
    let dateCreation: string | undefined
    let createdAt: string | undefined
    let updatedAt: string | undefined
    
    // Parser dateCreation
    if (obj.dateCreation) {
      const date = new Date(obj.dateCreation)
      dateCreation = isNaN(date.getTime()) ? undefined : date.toISOString()
    }
    
    // Parser createdAt
    if (obj.createdAt) {
      const date = new Date(obj.createdAt)
      createdAt = isNaN(date.getTime()) ? undefined : date.toISOString()
    }
    
    // Parser updatedAt
    if (obj.updatedAt) {
      const date = new Date(obj.updatedAt)
      updatedAt = isNaN(date.getTime()) ? undefined : date.toISOString()
    }
    
    return {
      id: parseInt(obj.id) || 0,
      libelle: obj.libelle || '',
      description: obj.description || '',
      dateCreation: dateCreation,
      createdAt: createdAt,
      updatedAt: updatedAt,
      createdBy: obj.createdBy,
      updatedBy: obj.updatedBy
    }
  }

  // Vérifier si un libellé existe déjà
  async checkLibelleExists(libelle: string, excludeId?: number): Promise<boolean> {
    try {
      const allTypes = await this.getAllTypeSanctions()
      return allTypes.some(type => 
        type.libelle.toLowerCase() === libelle.toLowerCase() && 
        (excludeId === undefined || type.id !== excludeId)
      )
    } catch (error) {
      console.error('Erreur lors de la vérification du libellé:', error)
      return false
    }
  }

  // Récupérer les statistiques
  async getStatistics(): Promise<{ total: number; recentlyCreated: number }> {
    try {
      const allTypes = await this.getAllTypeSanctions()
      const oneWeekAgo = new Date()
      oneWeekAgo.setDate(oneWeekAgo.getDate() - 7)
      
      const recentlyCreated = allTypes.filter(type => {
        const creationDate = type.dateCreation || type.createdAt
        return creationDate && new Date(creationDate) > oneWeekAgo
      }).length
      
      return {
        total: allTypes.length,
        recentlyCreated
      }
    } catch (error) {
      console.error('Erreur lors de la récupération des statistiques:', error)
      return { total: 0, recentlyCreated: 0 }
    }
  }
}

export default new TypeSanctionViewService()
