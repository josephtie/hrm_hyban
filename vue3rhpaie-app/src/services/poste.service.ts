import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface PosteRestDto {
  id?: number
  libelle?: string
  description?: string
  statut?: boolean
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface PosteRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class PosteRestService {
  private readonly endpoint = '/postes'

  // Lister les postes avec pagination
  async getPostes(filter?: PosteRestFilter): Promise<ApiResponse<PosteRestDto[]>> {
    try {
      const paginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      const response = await api.post('/postes/listpostejson', paginationRequest)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Postes retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching postes:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch postes'
      }
    }
  }

  // Récupérer un poste par son ID
  async getPosteById(id: number): Promise<ApiResponse<PosteRestDto>> {
    try {
      const response = await api.post('/postes/getposte', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as PosteRestDto,
        message: 'Poste retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching poste:', error)
      return {
        success: false,
        data: {} as PosteRestDto,
        message: 'Failed to fetch poste'
      }
    }
  }

  // Créer un nouveau poste
  async createPoste(poste: PosteRestDto): Promise<ApiResponse<PosteRestDto>> {
    try {
      const response = await api.post('/postes/saveposte', poste)
      const data = response.data
      
      return {
        success: true,
        data: data as PosteRestDto,
        message: 'Poste created successfully'
      }
    } catch (error) {
      console.error('Error creating poste:', error)
      return {
        success: false,
        data: {} as PosteRestDto,
        message: 'Failed to create poste'
      }
    }
  }

  // Mettre à jour un poste
  async updatePoste(id: number, poste: PosteRestDto): Promise<ApiResponse<PosteRestDto>> {
    try {
      const response = await api.post('/postes/updateposte', { ...poste, id })
      const data = response.data
      
      return {
        success: true,
        data: data as PosteRestDto,
        message: 'Poste updated successfully'
      }
    } catch (error) {
      console.error('Error updating poste:', error)
      return {
        success: false,
        data: {} as PosteRestDto,
        message: 'Failed to update poste'
      }
    }
  }

  // Supprimer un poste
  async deletePoste(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/postes/deleteposte', { id })
      
      return {
        success: true,
        data: true,
        message: 'Poste deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting poste:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete poste'
      }
    }
  }
}

export const posteRestService = new PosteRestService()
