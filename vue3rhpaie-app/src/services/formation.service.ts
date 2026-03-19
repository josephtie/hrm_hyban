import { FormationControllerApi } from '@/generated/api'
import type { ApiResponse } from '@/types/auth'
import type * as GeneratedTypes from '@/generated/api'

// Interface pour compatibilité avec le code existant
export interface FormationDto {
  // TODO: Adapter selon les champs réels de l'entité
  id?: number
  // Ajouter les autres champs selon la documentation
}

export interface FormationFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

class FormationService {
  private generatedApi: FormationControllerApi

  constructor() {
    this.generatedApi = new FormationControllerApi()
  }

  // TODO: Implémenter les méthodes selon les endpoints disponibles
  // Consultez src/generated/docs/FormationControllerApi.md pour la documentation complète
  
  async getAll(filter?: FormationFilter): Promise<ApiResponse<FormationDto[]>> {
    try {
      // Adapter au endpoint réel (exemple avec pagination)
      const paginationRequest: GeneratedTypes.PaginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      // Adapter à la méthode réelle du contrôleur
      // const response = await this.generatedApi.getFormationList(paginationRequest)
      
      // Template à adapter:
      return {
        success: true,
        data: [],
        total: 0,
        message: 'Formation retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Failed to retrieve formation'
      }
    }
  }

  async getById(id: number): Promise<ApiResponse<FormationDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.getFormationById({ id })
      
      return {
        success: true,
        data: {} as FormationDto,
        message: 'Formation retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as FormationDto,
        message: error.response?.data?.message || 'Failed to retrieve formation'
      }
    }
  }

  async create(data: any): Promise<ApiResponse<FormationDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.saveFormation(data)
      
      return {
        success: true,
        data: {} as FormationDto,
        message: 'Formation created successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as FormationDto,
        message: error.response?.data?.message || 'Failed to create formation'
      }
    }
  }

  async update(id: number, data: any): Promise<ApiResponse<FormationDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.updateFormation({ id, ...data })
      
      return {
        success: true,
        data: {} as FormationDto,
        message: 'Formation updated successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as FormationDto,
        message: error.response?.data?.message || 'Failed to update formation'
      }
    }
  }

  async delete(id: number): Promise<ApiResponse<void>> {
    try {
      // Adapter au endpoint réel
      // await this.generatedApi.deleteFormation({ id })
      
      return {
        success: true,
        data: undefined,
        message: 'Formation deleted successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete formation'
      }
    }
  }
}

export const formationService = new FormationService()
export default formationService
