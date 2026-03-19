import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface basée sur SocieteVueDTO du backend
export interface SocieteRestDto {
  id?: number
  raisonSociale: string
  sigle?: string
  rccm?: string
  compteContribuable?: string
  telephone?: string
  email?: string
  adresse?: string
  principale?: boolean
  dateCreation?: Date
  urlLogo?: string
}

export interface SocieteRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'raisonSociale' | 'sigle' | 'dateCreation'
  sortOrder?: 'asc' | 'desc'
}

class SocieteRestService {
  async getSocietes(filter?: SocieteRestFilter): Promise<ApiResponse<SocieteRestDto[]>> {
    try {
      const requestBody = {
        offset: filter?.page ?? 0,
        limit: filter?.size ?? 10,
        search: filter?.search || ''
      }
      
      const response = await api.post(`/parametrages/societes/list`, requestBody)
      const data = response.data
      
      // Le backend renvoie { rows: [societes], total: X, result: "success" }
      let actualRows = []
      if (data.rows && Array.isArray(data.rows)) {
        actualRows = data.rows
      }
      
      return {
        success: data.result === 'success',
        data: actualRows,
        total: data.total || 0,
        message: data.message || 'Societes retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching societes:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch societes'
      }
    }
  }

  async getAll(filter?: SocieteRestFilter): Promise<ApiResponse<SocieteRestDto[]>> {
    return this.getSocietes(filter)
  }

  async getSocieteById(id: number): Promise<ApiResponse<SocieteRestDto>> {
    try {
      const response = await api.get(`/parametrages/societes/trouver/${id}`)
      const data = response.data
      
      return {
        success: true,
        data: data as SocieteRestDto,
        message: 'Societe retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching societe:', error)
      return {
        success: false,
        data: {} as SocieteRestDto,
        message: 'Failed to fetch societe'
      }
    }
  }

  async createSociete(societe: SocieteRestDto): Promise<ApiResponse<SocieteRestDto>> {
    try {
      const response = await api.post('/parametrages/societes/enregistrer', {
        raisonSociale: societe.raisonSociale,
        sigle: societe.sigle,
        rccm: societe.rccm,
        compteContribuable: societe.compteContribuable,
        telephone: societe.telephone,
        email: societe.email,
        adresse: societe.adresse,
        principale: societe.principale || false,
        urlLogo: societe.urlLogo
      })
      const data = response.data
      
      return {
        success: true,
        data: data as SocieteRestDto,
        message: 'Societe created successfully'
      }
    } catch (error) {
      console.error('Error creating societe:', error)
      return {
        success: false,
        data: {} as SocieteRestDto,
        message: 'Failed to create societe'
      }
    }
  }

  async updateSociete(id: number, societe: SocieteRestDto): Promise<ApiResponse<SocieteRestDto>> {
    try {
      const response = await api.post('/parametrages/societes/enregistrer', {
        id,
        raisonSociale: societe.raisonSociale,
        sigle: societe.sigle,
        rccm: societe.rccm,
        compteContribuable: societe.compteContribuable,
        telephone: societe.telephone,
        email: societe.email,
        adresse: societe.adresse,
        principale: societe.principale || false,
        urlLogo: societe.urlLogo
      })
      const data = response.data
      
      return {
        success: true,
        data: data as SocieteRestDto,
        message: 'Societe updated successfully'
      }
    } catch (error) {
      console.error('Error updating societe:', error)
      return {
        success: false,
        data: {} as SocieteRestDto,
        message: 'Failed to update societe'
      }
    }
  }

  async deleteSociete(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.delete(`/parametrages/societes/supprimer/${id}`)
      
      return {
        success: true,
        data: true,
        message: 'Societe deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting societe:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete societe'
      }
    }
  }

  // Alias pour compatibilité
  async create(societe: SocieteRestDto): Promise<ApiResponse<SocieteRestDto>> {
    return this.createSociete(societe)
  }

  async update(societe: SocieteRestDto): Promise<ApiResponse<SocieteRestDto>> {
    if (!societe.id) {
      throw new Error('ID is required for update operation')
    }
    return this.updateSociete(societe.id, societe)
  }

  async delete(id: number): Promise<ApiResponse<boolean>> {
    return this.deleteSociete(id)
  }

  async getById(id: number): Promise<ApiResponse<SocieteRestDto>> {
    return this.getSocieteById(id)
  }
}

export const soceterestService = new SocieteRestService()
export default soceterestService
