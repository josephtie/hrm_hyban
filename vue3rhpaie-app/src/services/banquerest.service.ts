import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface BanqueRestDto {
  id?: number
  sigle?: string
  codbanq?: string
  libelle?: string
  rib?: number
  statut?: boolean
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface BanqueRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'libelle' | 'codbanq' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class BanqueRestService {
  private readonly endpoint = '/banques'

  // Lister les banques avec pagination
  async getBanques(filter?: BanqueRestFilter): Promise<ApiResponse<BanqueRestDto[]>> {
    try {
      const params = new URLSearchParams({
        limit: String(filter?.size ?? 10),
        offset: String(filter?.page ?? 0),
        search: filter?.search || ''
      })
      
      const response = await api.get(`/parametrages/banques/list?${params}`)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Banques retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching banques:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch banques'
      }
    }
  }

  // Alias pour compatibilité avec le code existant
  async getAll(filter?: BanqueRestFilter): Promise<ApiResponse<BanqueRestDto[]>> {
    return this.getBanques(filter)
  }

  // Récupérer une banque par son ID
  async getBanqueById(id: number): Promise<ApiResponse<BanqueRestDto>> {
    try {
      const response = await api.post('/parametrages/banques/trouver', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as BanqueRestDto,
        message: 'Banque retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching banque:', error)
      return {
        success: false,
        data: {} as BanqueRestDto,
        message: 'Failed to fetch banque'
      }
    }
  }

  // Créer une nouvelle banque
  async createBanque(banque: BanqueRestDto): Promise<ApiResponse<BanqueRestDto>> {
    try {
      const response = await api.post('/parametrages/banques/enregistrer', {
        id: 0,
        sigle: banque.sigle,
        libelle: banque.libelle,
        codbanq: banque.codbanq,
        statut: banque.statut
      })
      const data = response.data
      
      return {
        success: true,
        data: data as BanqueRestDto,
        message: 'Banque created successfully'
      }
    } catch (error) {
      console.error('Error creating banque:', error)
      return {
        success: false,
        data: {} as BanqueRestDto,
        message: 'Failed to create banque'
      }
    }
  }

  // Mettre à jour une banque
  async updateBanque(id: number, banque: BanqueRestDto): Promise<ApiResponse<BanqueRestDto>> {
    try {
      const response = await api.post('/parametrages/banques/enregistrer', {
        id: id,
        sigle: banque.sigle,
        libelle: banque.libelle,
        codbanq: banque.codbanq,
        statut: banque.statut
      })
      const data = response.data
      
      return {
        success: true,
        data: data as BanqueRestDto,
        message: 'Banque updated successfully'
      }
    } catch (error) {
      console.error('Error updating banque:', error)
      return {
        success: false,
        data: {} as BanqueRestDto,
        message: 'Failed to update banque'
      }
    }
  }

  // Supprimer une banque
  async deleteBanque(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/parametrages/banques/supprimer', { id })
      
      return {
        success: true,
        data: true,
        message: 'Banque deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting banque:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete banque'
      }
    }
  }

  // Méthodes alias pour compatibilité avec BanqueView.vue
  async update(id: number, banque: Partial<BanqueRestDto>): Promise<ApiResponse<BanqueRestDto>> {
    return this.updateBanque(id, banque as BanqueRestDto)
  }

  async create(banque: BanqueRestDto): Promise<ApiResponse<BanqueRestDto>> {
    return this.createBanque(banque)
  }

  async delete(id: number): Promise<ApiResponse<boolean>> {
    return this.deleteBanque(id)
  }
}

export const banquerestService = new BanqueRestService()
