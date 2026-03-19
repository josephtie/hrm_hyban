import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface PeriodePaieRestDto {
  id?: number
  libelle?: string
  dateDebut?: string
  dateFin?: string
  statut?: 'OUVERT' | 'CLOTURE'
  description?: string
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface PeriodePaieRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle' | 'statut' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class PeriodePaieRestService {
  private readonly endpoint = '/periodepaies'

  // Lister les périodes de paie avec pagination
  async getPeriodePaies(filter?: PeriodePaieRestFilter): Promise<ApiResponse<PeriodePaieRestDto[]>> {
    try {
      const paginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      const response = await api.post('/periodepaies/listperiodepaiejson', paginationRequest)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'PeriodePaies retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching periodepaies:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch periodepaies'
      }
    }
  }

  // Récupérer une période de paie par son ID
  async getPeriodePaieById(id: number): Promise<ApiResponse<PeriodePaieRestDto>> {
    try {
      const response = await api.post('/periodepaies/getperiodepaie', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as PeriodePaieRestDto,
        message: 'PeriodePaie retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching periodepaie:', error)
      return {
        success: false,
        data: {} as PeriodePaieRestDto,
        message: 'Failed to fetch periodepaie'
      }
    }
  }

  // Créer une nouvelle période de paie
  async createPeriodePaie(periodepaie: PeriodePaieRestDto): Promise<ApiResponse<PeriodePaieRestDto>> {
    try {
      const response = await api.post('/periodepaies/saveperiodepaie', periodepaie)
      const data = response.data
      
      return {
        success: true,
        data: data as PeriodePaieRestDto,
        message: 'PeriodePaie created successfully'
      }
    } catch (error) {
      console.error('Error creating periodepaie:', error)
      return {
        success: false,
        data: {} as PeriodePaieRestDto,
        message: 'Failed to create periodepaie'
      }
    }
  }

  // Mettre à jour une période de paie
  async updatePeriodePaie(id: number, periodepaie: PeriodePaieRestDto): Promise<ApiResponse<PeriodePaieRestDto>> {
    try {
      const response = await api.post('/periodepaies/updateperiodepaie', { ...periodepaie, id })
      const data = response.data
      
      return {
        success: true,
        data: data as PeriodePaieRestDto,
        message: 'PeriodePaie updated successfully'
      }
    } catch (error) {
      console.error('Error updating periodepaie:', error)
      return {
        success: false,
        data: {} as PeriodePaieRestDto,
        message: 'Failed to update periodepaie'
      }
    }
  }

  // Supprimer une période de paie
  async deletePeriodePaie(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/periodepaies/deleteperiodepaie', { id })
      
      return {
        success: true,
        data: true,
        message: 'PeriodePaie deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting periodepaie:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete periodepaie'
      }
    }
  }
}

export const periodePaieRestService = new PeriodePaieRestService()
