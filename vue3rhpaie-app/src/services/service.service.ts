import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface ServiceRestDto {
  id?: number
  libelle?: string
  description?: string
  statut?: boolean
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface ServiceRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class ServiceRestService {
  private readonly endpoint = '/services'

  // Lister les services avec pagination
  async getServices(filter?: ServiceRestFilter): Promise<ApiResponse<ServiceRestDto[]>> {
    try {
      const paginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      const response = await api.post('/services/listservicejson', paginationRequest)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Services retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching services:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch services'
      }
    }
  }

  // Récupérer un service par son ID
  async getServiceById(id: number): Promise<ApiResponse<ServiceRestDto>> {
    try {
      const response = await api.post('/services/getservice', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as ServiceRestDto,
        message: 'Service retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching service:', error)
      return {
        success: false,
        data: {} as ServiceRestDto,
        message: 'Failed to fetch service'
      }
    }
  }

  // Créer un nouveau service
  async createService(service: ServiceRestDto): Promise<ApiResponse<ServiceRestDto>> {
    try {
      const response = await api.post('/services/saveservice', service)
      const data = response.data
      
      return {
        success: true,
        data: data as ServiceRestDto,
        message: 'Service created successfully'
      }
    } catch (error) {
      console.error('Error creating service:', error)
      return {
        success: false,
        data: {} as ServiceRestDto,
        message: 'Failed to create service'
      }
    }
  }

  // Mettre à jour un service
  async updateService(id: number, service: ServiceRestDto): Promise<ApiResponse<ServiceRestDto>> {
    try {
      const response = await api.post('/services/updateservice', { ...service, id })
      const data = response.data
      
      return {
        success: true,
        data: data as ServiceRestDto,
        message: 'Service updated successfully'
      }
    } catch (error) {
      console.error('Error updating service:', error)
      return {
        success: false,
        data: {} as ServiceRestDto,
        message: 'Failed to update service'
      }
    }
  }

  // Supprimer un service
  async deleteService(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/services/deleteservice', { id })
      
      return {
        success: true,
        data: true,
        message: 'Service deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting service:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete service'
      }
    }
  }
}

export const serviceRestService = new ServiceRestService()
