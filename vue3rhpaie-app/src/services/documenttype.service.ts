import { DocumentTypeControllerApi } from '@/generated/api'
import type { ApiResponse } from '@/types/auth'
import type * as GeneratedTypes from '@/generated/api'

// Interface pour compatibilité avec le code existant
export interface DocumentTypeDto {
  // TODO: Adapter selon les champs réels de l'entité
  id?: number
  // Ajouter les autres champs selon la documentation
}

export interface DocumentTypeFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

class DocumentTypeService {
  private generatedApi: DocumentTypeControllerApi

  constructor() {
    this.generatedApi = new DocumentTypeControllerApi()
  }

  // TODO: Implémenter les méthodes selon les endpoints disponibles
  // Consultez src/generated/docs/DocumentTypeControllerApi.md pour la documentation complète
  
  async getAll(filter?: DocumentTypeFilter): Promise<ApiResponse<DocumentTypeDto[]>> {
    try {
      // Adapter au endpoint réel (exemple avec pagination)
      const paginationRequest: GeneratedTypes.PaginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      // Adapter à la méthode réelle du contrôleur
      // const response = await this.generatedApi.getDocumentTypeList(paginationRequest)
      
      // Template à adapter:
      return {
        success: true,
        data: [],
        total: 0,
        message: 'DocumentType retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Failed to retrieve documenttype'
      }
    }
  }

  async getById(id: number): Promise<ApiResponse<DocumentTypeDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.getDocumentTypeById({ id })
      
      return {
        success: true,
        data: {} as DocumentTypeDto,
        message: 'DocumentType retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as DocumentTypeDto,
        message: error.response?.data?.message || 'Failed to retrieve documenttype'
      }
    }
  }

  async create(data: any): Promise<ApiResponse<DocumentTypeDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.saveDocumentType(data)
      
      return {
        success: true,
        data: {} as DocumentTypeDto,
        message: 'DocumentType created successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as DocumentTypeDto,
        message: error.response?.data?.message || 'Failed to create documenttype'
      }
    }
  }

  async update(id: number, data: any): Promise<ApiResponse<DocumentTypeDto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.updateDocumentType({ id, ...data })
      
      return {
        success: true,
        data: {} as DocumentTypeDto,
        message: 'DocumentType updated successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as DocumentTypeDto,
        message: error.response?.data?.message || 'Failed to update documenttype'
      }
    }
  }

  async delete(id: number): Promise<ApiResponse<void>> {
    try {
      // Adapter au endpoint réel
      // await this.generatedApi.deleteDocumentType({ id })
      
      return {
        success: true,
        data: undefined,
        message: 'DocumentType deleted successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete documenttype'
      }
    }
  }
}

export const documenttypeService = new DocumentTypeService()
export default documenttypeService
