import axios from 'axios'

// Interface pour la société
export interface SocieteDto {
  id?: number
  raisonSociale?: string
  sigle?: string
  rccm?: string
  compteContribuable?: string
  telephone?: string
  email?: string
  adresse?: string
  principale?: boolean
  dateCreation?: Date
  urlLogo?: string
  activitepp?: string
  formjuri?: string
  bp?: string
  commune?: string
  quartier?: string
  rue?: string
  lot?: string
  sectpartiell?: string
  centreImpot?: string
  codeEts?: string
  codeActivite?: string
  codeEmployeur?: string
  nomcomptable?: string
  telcomptable?: string
  adrcomptable?: string
  txprest?: number
  txacctr?: number
  txretraite?: number
  txgratif?: number
  gratification?: number
}

export interface SocieteFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

export interface SocieteListResponse {
  rows: SocieteDto[]
  total: number
  result: string
  message: string
}

export interface ApiResponse<T> {
  success: boolean
  data: T
  message: string
}

const API_BASE_URL = 'http://192.168.1.8:7200/api/parametrages/societes'

class SocieteService {
  // Récupérer la liste des sociétés avec pagination
  async getAll(filter?: SocieteFilter): Promise<ApiResponse<SocieteListResponse>> {
    try {
      const response = await axios.post(`${API_BASE_URL}/list`, {
        offset: filter?.page ?? 0,
        limit: filter?.size ?? 10,
        search: filter?.search
      })
      
      return {
        success: response.data.result === 'success',
        data: response.data,
        message: response.data.message || 'Sociétés récupérées avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: { rows: [], total: 0, result: 'error', message: '' },
        message: error.response?.data?.message || 'Erreur lors de la récupération des sociétés'
      }
    }
  }

  // Récupérer une société par son ID
  async getById(id: number): Promise<ApiResponse<SocieteDto>> {
    try {
      const response = await axios.get(`${API_BASE_URL}/${id}`)
      
      return {
        success: true,
        data: response.data,
        message: 'Société récupérée avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as SocieteDto,
        message: error.response?.data?.message || 'Erreur lors de la récupération de la société'
      }
    }
  }

  // Créer une nouvelle société
  async create(data: SocieteDto): Promise<ApiResponse<SocieteDto>> {
    try {
      const response = await axios.post(`${API_BASE_URL}/save`, data)
      
      return {
        success: response.data.result === 'success',
        data: {} as SocieteDto,
        message: response.data.message || 'Société créée avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as SocieteDto,
        message: error.response?.data?.message || 'Erreur lors de la création de la société'
      }
    }
  }

  // Mettre à jour une société
  async update(data: SocieteDto): Promise<ApiResponse<SocieteDto>> {
    try {
      const response = await axios.post(`${API_BASE_URL}/update`, data)
      
      return {
        success: response.data.result === 'success',
        data: {} as SocieteDto,
        message: response.data.message || 'Société mise à jour avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as SocieteDto,
        message: error.response?.data?.message || 'Erreur lors de la mise à jour de la société'
      }
    }
  }

  // Supprimer une société
  async delete(id: number): Promise<ApiResponse<void>> {
    try {
      const response = await axios.post(`${API_BASE_URL}/delete`, { id })
      
      return {
        success: response.data.result === 'success',
        data: undefined,
        message: response.data.message || 'Société supprimée avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Erreur lors de la suppression de la société'
      }
    }
  }

  // Basculer le statut de société principale
  async togglePrincipale(id: number): Promise<ApiResponse<void>> {
    try {
      const response = await axios.post(`${API_BASE_URL}/toggle-principale`, { id })
      
      return {
        success: response.data.result === 'success',
        data: undefined,
        message: response.data.message || 'Statut modifié avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Erreur lors de la modification du statut'
      }
    }
  }

  // Récupérer toutes les sociétés (sans pagination)
  async getAllSocietes(): Promise<ApiResponse<SocieteDto[]>> {
    try {
      const response = await axios.get(`${API_BASE_URL}/list/all`)
      
      return {
        success: true,
        data: response.data,
        message: 'Sociétés récupérées avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Erreur lors de la récupération des sociétés'
      }
    }
  }

  // Télécharger le logo
  async uploadLogo(file: File): Promise<ApiResponse<void>> {
    try {
      const formData = new FormData()
      formData.append('file', file)
      
      const response = await axios.post(`${API_BASE_URL}/upload-logo`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      
      return {
        success: response.data.result === 'success',
        data: undefined,
        message: response.data.message || 'Logo téléchargé avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Erreur lors du téléchargement du logo'
      }
    }
  }
}

export const societeService = new SocieteService()
export default societeService
