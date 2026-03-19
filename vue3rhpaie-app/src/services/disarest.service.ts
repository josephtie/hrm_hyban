import { DisaRestControllerApi } from '@/generated/api'
import type { ApiResponse } from '@/types/auth'
import type * as GeneratedTypes from '@/generated/api'

// Interface pour compatibilité avec le code existant
export interface DisaRestDto {
  // TODO: Adapter selon les champs réels de l'entité
  id?: number
  // Ajouter les autres champs selon la documentation
}

export interface DisaRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

class DisaRestService {
  private generatedApi: DisaRestControllerApi

  constructor() {
    this.generatedApi = new DisaRestControllerApi()
  }

  // TODO: Implémenter les méthodes selon les endpoints disponibles
  // Consultez src/generated/docs/DisaRestControllerApi.md pour la documentation complète
  
  async getAll(filter?: DisaRestFilter): Promise<ApiResponse<DisaRestDto[]>> {
    try {
      // Adapter au endpoint réel (exemple avec pagination)
      const paginationRequest: GeneratedTypes.PaginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      // Adapter à la méthode réelle du contrôleur
      // const response = await this.generatedApi.getDisaRestList(paginationRequest)
      
      // Template à adapter:
      return {
        success: true,
        data: [],
        total: 0,
        message: 'DisaRest retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Failed to retrieve disarest'
      }
    }
  }

  async getById(id: number): Promise<ApiResponse<DisaRestDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.getDisaRestById({ id })
      
      return {
        success: true,
        data: {} as DisaRestDto,
        message: 'DisaRest retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as DisaRestDto,
        message: error.response?.data?.message || 'Failed to retrieve disarest'
      }
    }
  }

  async create(data: any): Promise<ApiResponse<DisaRestDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.saveDisaRest(data)
      
      return {
        success: true,
        data: {} as DisaRestDto,
        message: 'DisaRest created successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as DisaRestDto,
        message: error.response?.data?.message || 'Failed to create disarest'
      }
    }
  }

  async update(id: number, data: any): Promise<ApiResponse<DisaRestDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.updateDisaRest({ id, ...data })
      
      return {
        success: true,
        data: {} as DisaRestDto,
        message: 'DisaRest updated successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as DisaRestDto,
        message: error.response?.data?.message || 'Failed to update disarest'
      }
    }
  }

  async delete(id: number): Promise<ApiResponse<void>> {
    try {
      // Adapter au endpoint réel
      // await this.generatedApi.deleteDisaRest({ id })
      
      return {
        success: true,
        data: undefined,
        message: 'DisaRest deleted successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete disarest'
      }
    }
  }
}

export const disarestService = new DisaRestService()
export default disarestService
