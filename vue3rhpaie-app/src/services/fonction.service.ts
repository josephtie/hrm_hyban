import { api } from './api'
import type { ApiResponse } from '@/types/auth'
import type { PaginationRequest } from '@/generated/api'

// Interface pour compatibilité avec le code existant
export interface FonctionRestDto {
  id?: number
  libelle?: string
  description?: string
  niveau?: string
  departement?: string
  statut?: 'ACTIF' | 'INACTIF'
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface FonctionRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class FonctionRestService {
  private readonly endpoint = '/fonctions'

  // Lister les fonctions avec pagination (modèle categories.service.ts)
  async getFonctions(filter?: FonctionRestFilter): Promise<ApiResponse<FonctionRestDto[]>> {
    try {
      const paginationRequest: PaginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      console.log('📤 Envoi requête pagination:', paginationRequest)
      
      const response = await api.post('/personnels/fonctions/listfonctionjson', paginationRequest)
      const data = response.data
      
      console.log('📥 Réponse API brute:', data)
      console.log('📊 Données extraites:', {
        rows: data.rows?.length || 0,
        total: data.total,
        status: data.status
      })
      
      return {
        success: true, // Toujours true comme categories.service.ts
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Fonctions retrieved successfully'
      }
    } catch (error) {
      console.error('💥 Erreur getFonctions:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch fonctions'
      }
    }
  }

  // Récupérer une fonction par son ID
  async getFonctionById(id: number): Promise<ApiResponse<FonctionRestDto>> {
    try {
      const response = await api.post('/fonctions/getfonction', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as FonctionRestDto,
        message: 'Fonction retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching fonction:', error)
      return {
        success: false,
        data: {} as FonctionRestDto,
        message: 'Failed to fetch fonction'
      }
    }
  }

  // Créer une nouvelle fonction
  async createFonction(fonction: Omit<FonctionRestDto, 'id' | 'createdAt' | 'createdBy' | 'updatedAt' | 'updatedBy'>): Promise<ApiResponse<FonctionRestDto>> {
    try {
      const response = await api.post('/fonctions', {
        libelle: fonction.libelle,
        description: fonction.description,
        niveau: fonction.niveau,
        departement: fonction.departement,
        statut: fonction.statut
      })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: data.row as FonctionRestDto || data as FonctionRestDto,
        message: data.message || 'Fonction created successfully'
      }
    } catch (error: any) {
      console.error('Error creating fonction:', error)
      return {
        success: false,
        data: {} as FonctionRestDto,
        message: error.response?.data?.message || 'Failed to create fonction'
      }
    }
  }

  // Mettre à jour une fonction (utilise le même endpoint que la création)
  async updateFonction(fonction: FonctionRestDto): Promise<ApiResponse<FonctionRestDto>> {
    try {
      const response = await api.post('/fonctions', {
        id: fonction.id,
        libelle: fonction.libelle,
        description: fonction.description,
        niveau: fonction.niveau,
        departement: fonction.departement,
        statut: fonction.statut
      })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: data.row as FonctionRestDto || data as FonctionRestDto,
        message: data.message || 'Fonction updated successfully'
      }
    } catch (error: any) {
      console.error('Error updating fonction:', error)
      return {
        success: false,
        data: {} as FonctionRestDto,
        message: error.response?.data?.message || 'Failed to update fonction'
      }
    }
  }

  // Supprimer une fonction
  async deleteFonction(id: number): Promise<ApiResponse<void>> {
    try {
      const response = await api.post('/fonctions/deletefonction', { id })
      const data = response.data
      
      return {
        success: data.result === true || data.result === 'success',
        data: undefined,
        message: data.message || 'Fonction deleted successfully'
      }
    } catch (error: any) {
      console.error('Error deleting fonction:', error)
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete fonction'
      }
    }
  }
}

export const fonctionRestService = new FonctionRestService()
