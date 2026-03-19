import { api } from './api'
import type { ApiResponse } from '@/types/auth'
import type { Categorie, CategorieDTO, RequestCategory, PaginationRequest } from '@/generated/api'

// Interface pour compatibilité avec le code existant
export interface CategorieDto {
  id?: number
  libelle?: string
  salaireDeBase?: number
  indemniteLogement?: number
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface CategorieFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle' | 'salaireDeBase' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class CategoriesService {
  private readonly endpoint = '/categories'

  // Lister les catégories avec pagination (utilise directement notre instance api)
  async getCategories(filter?: CategorieFilter): Promise<ApiResponse<CategorieDto[]>> {
    try {
      const paginationRequest: PaginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      const response = await api.post('/categories/listcategoriejson', paginationRequest)
      const data: CategorieDTO = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Categories retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching categories:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch categories'
      }
    }
  }

  // Récupérer une catégorie par son ID (utilise l'API générée)
  async getCategorieById(id: number): Promise<ApiResponse<CategorieDto>> {
    try {
      const response = await this.generatedApi.getCategory({ id })
      const data: Categorie = response.data
      
      return {
        success: true,
        data: data as CategorieDto,
        message: 'Category retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as CategorieDto,
        message: error.response?.data?.message || 'Failed to retrieve category'
      }
    }
  }

  // Créer une nouvelle catégorie (utilise l'API directe)
  async createCategorie(categorie: Omit<CategorieDto, 'id' | 'createdAt' | 'createdBy' | 'updatedAt' | 'updatedBy'>): Promise<ApiResponse<CategorieDto>> {
    try {
      const response = await api.post('/categories', {
        libelle: categorie.libelle,
        salaireDeBase: categorie.salaireDeBase,
        indemniteLogement: categorie.indemniteLogement
      })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: data.row as CategorieDto || data as CategorieDto,
        message: data.message || 'Category created successfully'
      }
    } catch (error: any) {
      console.error('Error creating category:', error)
      return {
        success: false,
        data: {} as CategorieDto,
        message: error.response?.data?.message || 'Failed to create category'
      }
    }
  }

  // Mettre à jour une catégorie (utilise l'API directe)
  async updateCategorie(id: number, categorie: Partial<CategorieDto>): Promise<ApiResponse<CategorieDto>> {
    try {
      const response = await api.post('/categories', {
        id: id,
        libelle: categorie.libelle,
        salaireDeBase: categorie.salaireDeBase,
        indemniteLogement: categorie.indemniteLogement
      })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: data.row as CategorieDto || data as CategorieDto,
        message: data.message || 'Category updated successfully'
      }
    } catch (error: any) {
      console.error('Error updating category:', error)
      return {
        success: false,
        data: {} as CategorieDto,
        message: error.response?.data?.message || 'Failed to update category'
      }
    }
  }

  // Supprimer une catégorie (utilise l'API directe)
  async deleteCategorie(id: number): Promise<ApiResponse<void>> {
    try {
      const response = await api.post('/categories/supprimercategorie', { id })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: undefined,
        message: 'Category deleted successfully'
      }
    } catch (error: any) {
      console.error('Error deleting category:', error)
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete category'
      }
    }
  }

  // Récupérer les statistiques des catégories (méthode manquante)
  async getCategoriesStats(): Promise<ApiResponse<{
    totalCategories: number
    salaireMoyen: number
    indemniteMoyenne: number
    derniereMiseAJour: string
  }>> {
    try {
      // Pour l'instant, retourner des statistiques calculées
      const response = await this.generatedApi.getCategoryListJSON1({
        limit: 1000, // Obtenir toutes les catégories
        offset: 0
      })
      
      const categories = response.data.rows || []
      const totalCategories = categories.length
      const salaireMoyen = categories.length > 0 
        ? Math.round(categories.reduce((sum, cat) => sum + (cat.salaireDeBase || 0), 0) / categories.length)
        : 0
      const indemniteMoyenne = categories.length > 0
        ? Math.round(categories.reduce((sum, cat) => sum + (cat.indemniteLogement || 0), 0) / categories.length)
        : 0
      const derniereMiseAJour = categories.length > 0
        ? new Date(Math.max(...categories.map(cat => new Date(cat.updatedAt || cat.createdAt || '').getTime()))).toISOString()
        : new Date().toISOString()
      
      return {
        success: true,
        data: {
          totalCategories,
          salaireMoyen,
          indemniteMoyenne,
          derniereMiseAJour
        },
        message: 'Stats retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {
          totalCategories: 0,
          salaireMoyen: 0,
          indemniteMoyenne: 0,
          derniereMiseAJour: new Date().toISOString()
        },
        message: error.response?.data?.message || 'Failed to retrieve stats'
      }
    }
  }
}

export const categoriesService = new CategoriesService()
export default categoriesService
