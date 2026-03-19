import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface ContratPersonnelRestDto {
  id?: number
  reference?: string
  dateDebut?: string
  dateFin?: string
  typeContrat?: string
  salaireBase?: number
  statut?: 'ACTIF' | 'INACTIF' | 'TERMINE'
  personnelId?: number
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface ContratPersonnelRestFilter {
  page?: number
  size?: number
  search?: string
  personnelId?: number
  statut?: string
  sortBy?: 'reference' | 'dateDebut' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class ContratPersonnelRestService {
  private readonly endpoint = '/contratpersonnel'

  // Lister les contrats avec pagination
  async getContrats(filter?: ContratPersonnelRestFilter): Promise<ApiResponse<ContratPersonnelRestDto[]>> {
    try {
      const paginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search,
        personnelId: filter?.personnelId,
        statut: filter?.statut
      }
      
      const response = await api.post('/contratpersonnel/listcontratpersonneljson', paginationRequest)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Contrats retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching contrats:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch contrats'
      }
    }
  }

  // Récupérer un contrat par son ID
  async getContratById(id: number): Promise<ApiResponse<ContratPersonnelRestDto>> {
    try {
      const response = await api.post('/contratpersonnel/getcontratpersonnel', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as ContratPersonnelRestDto,
        message: 'Contrat retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching contrat:', error)
      return {
        success: false,
        data: {} as ContratPersonnelRestDto,
        message: 'Failed to fetch contrat'
      }
    }
  }

  // Créer un nouveau contrat
  async createContrat(contrat: ContratPersonnelRestDto): Promise<ApiResponse<ContratPersonnelRestDto>> {
    try {
      const response = await api.post('/contratpersonnel/savecontratpersonnel', contrat)
      const data = response.data
      
      return {
        success: true,
        data: data as ContratPersonnelRestDto,
        message: 'Contrat created successfully'
      }
    } catch (error) {
      console.error('Error creating contrat:', error)
      return {
        success: false,
        data: {} as ContratPersonnelRestDto,
        message: 'Failed to create contrat'
      }
    }
  }

  // Mettre à jour un contrat
  async updateContrat(id: number, contrat: ContratPersonnelRestDto): Promise<ApiResponse<ContratPersonnelRestDto>> {
    try {
      const response = await api.post('/contratpersonnel/updatecontratpersonnel', { ...contrat, id })
      const data = response.data
      
      return {
        success: true,
        data: data as ContratPersonnelRestDto,
        message: 'Contrat updated successfully'
      }
    } catch (error) {
      console.error('Error updating contrat:', error)
      return {
        success: false,
        data: {} as ContratPersonnelRestDto,
        message: 'Failed to update contrat'
      }
    }
  }

  // Supprimer un contrat
  async deleteContrat(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/contratpersonnel/deletecontratpersonnel', { id })
      
      return {
        success: true,
        data: true,
        message: 'Contrat deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting contrat:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete contrat'
      }
    }
  }
}

export const contratPersonnelRestService = new ContratPersonnelRestService()
