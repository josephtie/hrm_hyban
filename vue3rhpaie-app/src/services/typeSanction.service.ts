import axios from 'axios'

const API_BASE_URL = 'http://192.168.1.8:7200/api/rh/carriere/types-sanctions'

export interface TypeSanctionDto {
  id: number
  libelle: string
  description?: string
  dateCreation?: string
  dateModification?: string
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
  message?: string
}

export interface PaginationRequest {
  offset?: number
  limit?: number
  search?: string
}

class TypeSanctionService {
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
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des types de sanctions:', error)
      throw error
    }
  }

  // Récupérer un type de sanction par son ID
  async getById(id: number): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/trouver', { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération du type de sanction:', error)
      throw error
    }
  }

  // Créer un nouveau type de sanction
  async create(typeSanction: TypeSanctionRequest): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/enregistrer', typeSanction)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création du type de sanction:', error)
      throw error
    }
  }

  // Mettre à jour un type de sanction
  async update(typeSanction: TypeSanctionRequest): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/enregistrer', typeSanction)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour du type de sanction:', error)
      throw error
    }
  }

  // Supprimer un type de sanction
  async delete(id: number): Promise<TypeSanctionDto> {
    try {
      const response = await this.api.post('/supprimer', { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression du type de sanction:', error)
      throw error
    }
  }

  // Récupérer tous les types de sanctions (sans pagination)
  async getAllTypeSanctions(): Promise<TypeSanctionDto[]> {
    try {
      const response = await this.api.post('/lister')
      return response.data.rows || []
    } catch (error) {
      console.error('Erreur lors de la récupération de tous les types de sanctions:', error)
      throw error
    }
  }
}

export default new TypeSanctionService()
