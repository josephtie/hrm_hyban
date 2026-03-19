import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface ExerciceRestDto {
  id?: number
  annee?: string
  moisDebut?: string
  moisFin?: string
  statut?: 'OUVERT' | 'CLOTURE'
  description?: string
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface ExerciceRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'annee' | 'statut' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class ExerciceRestService {
  private readonly endpoint = '/exercices'

  // Lister les exercices avec pagination
  async getExercices(filter?: ExerciceRestFilter): Promise<ApiResponse<ExerciceRestDto[]>> {
    try {
      const paginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      const response = await api.post('/exercices/listexercicejson', paginationRequest)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Exercices retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching exercices:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch exercices'
      }
    }
  }

  // Récupérer un exercice par son ID
  async getExerciceById(id: number): Promise<ApiResponse<ExerciceRestDto>> {
    try {
      const response = await api.post('/exercices/getexercice', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as ExerciceRestDto,
        message: 'Exercice retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching exercice:', error)
      return {
        success: false,
        data: {} as ExerciceRestDto,
        message: 'Failed to fetch exercice'
      }
    }
  }

  // Créer un nouvel exercice
  async createExercice(exercice: ExerciceRestDto): Promise<ApiResponse<ExerciceRestDto>> {
    try {
      const response = await api.post('/exercices/saveexercice', exercice)
      const data = response.data
      
      return {
        success: true,
        data: data as ExerciceRestDto,
        message: 'Exercice created successfully'
      }
    } catch (error) {
      console.error('Error creating exercice:', error)
      return {
        success: false,
        data: {} as ExerciceRestDto,
        message: 'Failed to create exercice'
      }
    }
  }

  // Mettre à jour un exercice
  async updateExercice(id: number, exercice: ExerciceRestDto): Promise<ApiResponse<ExerciceRestDto>> {
    try {
      const response = await api.post('/exercices/updateexercice', { ...exercice, id })
      const data = response.data
      
      return {
        success: true,
        data: data as ExerciceRestDto,
        message: 'Exercice updated successfully'
      }
    } catch (error) {
      console.error('Error updating exercice:', error)
      return {
        success: false,
        data: {} as ExerciceRestDto,
        message: 'Failed to update exercice'
      }
    }
  }

  // Supprimer un exercice
  async deleteExercice(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/exercices/deleteexercice', { id })
      
      return {
        success: true,
        data: true,
        message: 'Exercice deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting exercice:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete exercice'
      }
    }
  }
}

export const exerciceRestService = new ExerciceRestService()
