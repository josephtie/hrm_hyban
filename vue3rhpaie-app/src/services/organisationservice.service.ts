import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface basée sur OrganisationServiceDto du backend
export interface OrganisationServiceDto {
  id?: number
  libelle: string
  type: 'DIRECTION' | 'DEPARTEMENT' | 'SERVICE'
  idDirection?: number
  idDepartement?: number
  active?: boolean
  createdAt?: string
  direction?: OrganisationServiceDto
  departement?: OrganisationServiceDto
}

export interface OrganisationServiceFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle'
  sortOrder?: 'asc' | 'desc'
}

class OrganisationServiceRestService {
  async getOrganisationServices(filter?: OrganisationServiceFilter): Promise<ApiResponse<OrganisationServiceDto[]>> {
    try {
      const requestBody = {
        offset: filter?.page ?? 0,
        limit: filter?.size ?? 10,
        search: filter?.search || ''
      }
      
      const response = await api.post(`/personnels/organisation/list`, requestBody)
      const data = response.data
      
      // Le backend renvoie { rows: [services], total: X, result: "success" }
      let actualRows = []
      if (data.rows && Array.isArray(data.rows)) {
        actualRows = data.rows
      }
      
      return {
        success: data.result === 'success',
        data: actualRows,
        total: data.total || 0,
        message: data.message || 'Organisation services retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching organisation services:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch organisation services'
      }
    }
  }

  async getAll(filter?: OrganisationServiceFilter): Promise<ApiResponse<OrganisationServiceDto[]>> {
    return this.getOrganisationServices(filter)
  }

  async getOrganisationServiceById(id: number): Promise<ApiResponse<OrganisationServiceDto>> {
    try {
      // Pour l'instant, on simule avec getAll et filtre
      const response = await this.getAll()
      const service = response.data.find(s => s.id === id)
      
      return {
        success: !!service,
        data: service || {} as OrganisationServiceDto,
        message: service ? 'Organisation service retrieved successfully' : 'Organisation service not found'
      }
    } catch (error) {
      console.error('Error fetching organisation service:', error)
      return {
        success: false,
        data: {} as OrganisationServiceDto,
        message: 'Failed to fetch organisation service'
      }
    }
  }

  // Méthodes utilitaires pour la vue
  getDirections(services: OrganisationServiceDto[]): OrganisationServiceDto[] {
    return services.filter(s => s.type === 'DIRECTION')
  }

  getDepartements(services: OrganisationServiceDto[]): OrganisationServiceDto[] {
    return services.filter(s => s.type === 'DEPARTEMENT')
  }

  getServices(services: OrganisationServiceDto[]): OrganisationServiceDto[] {
    return services.filter(s => s.type === 'SERVICE')
  }

  getDepartementsByDirection(directionId: number, services: OrganisationServiceDto[]): OrganisationServiceDto[] {
    return services.filter(s => s.type === 'DEPARTEMENT' && s.idDirection === directionId)
  }

  getServicesByDepartement(departementId: number, services: OrganisationServiceDto[]): OrganisationServiceDto[] {
    return services.filter(s => s.type === 'SERVICE' && s.idDepartement === departementId)
  }

  // Mapper les types de service vers les IDs correspondants
  private getTypeIdByServiceType(type: string): number {
    switch (type) {
      case 'DIRECTION':
        return 1 // ID pour Direction
      case 'DEPARTEMENT':
        return 2 // ID pour Département  
      case 'SERVICE':
        return 3 // ID pour Service
      default:
        return 1 // Par défaut
    }
  }

  // Alias pour compatibilité
  async create(service: OrganisationServiceDto): Promise<ApiResponse<OrganisationServiceDto>> {
    try {
      const response = await api.post('/personnels/enregisterservice', {
        id: service.id,
        libelle: service.libelle,
        idDepartement: service.idDepartement,
        idDirection: service.idDirection,
        idTypeService: this.getTypeIdByServiceType(service.type),
        active: service.active ?? false
      })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: data as OrganisationServiceDto,
        message: 'Organisation service created successfully'
      }
    } catch (error) {
      console.error('Error creating organisation service:', error)
      return {
        success: false,
        data: {} as OrganisationServiceDto,
        message: 'Failed to create organisation service'
      }
    }
  }

  async update(service: OrganisationServiceDto): Promise<ApiResponse<OrganisationServiceDto>> {
    try {
      const typeId = this.getTypeIdByServiceType(service.type)
      console.log('✏️ Update service:', {
        ...service,
        idTypeService: typeId,
        mapping: `${service.type} → ID ${typeId}`
      })
      
      const response = await api.post('/personnels/enregisterservice', {
        id: service.id,
        libelle: service.libelle,
        idDepartement: service.idDepartement,
        idDirection: service.idDirection,
        idTypeService: typeId,
        active: service.active ?? false
      })
      const data = response.data
      
      console.log('📥 Réponse backend update:', data)
      
      return {
        success: data.result === true || data.result === 'success',
        data: data as OrganisationServiceDto,
        message: 'Organisation service updated successfully'
      }
    } catch (error) {
      console.error('Error updating organisation service:', error)
      return {
        success: false,
        data: {} as OrganisationServiceDto,
        message: 'Failed to update organisation service'
      }
    }
  }

  async delete(id: number): Promise<ApiResponse<boolean>> {
    try {
      const response = await api.post('/personnels/supprimerservice', { id })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: true,
        message: 'Organisation service deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting organisation service:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete organisation service'
      }
    }
  }

  async getById(id: number): Promise<ApiResponse<OrganisationServiceDto>> {
    return this.getOrganisationServiceById(id)
  }
}

export const organisationservicerestService = new OrganisationServiceRestService()
export default organisationservicerestService
