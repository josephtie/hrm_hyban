import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface basée sur SanctionRequest du backend
export interface SanctionRestDto {
  id?: number
  idTypeSanction?: number
  faute?: string
  commentaire?: string
  typeSanction?: {
    id: number
    libelle?: string
    code?: string
  }
  dateCreation?: string
  dateModification?: string
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface SanctionRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'faute' | 'dateCreation' | 'typeSanction'
  sortOrder?: 'asc' | 'desc'
}

class SanctionRestService {
  async getSanctions(filter?: SanctionRestFilter): Promise<ApiResponse<SanctionRestDto[]>> {
    try {
      const params = new URLSearchParams({
        limit: String(filter?.size ?? 10),
        offset: String(filter?.page ?? 0),
        search: filter?.search || ''
      })
      
      const response = await api.get(`/rh/carriere/sanctions/list?${params}`)
      const data = response.data
      
      // Le backend renvoie { rows: [ [sanctions] ], total: X }
      // rows est une liste contenant une autre liste de sanctions
      let actualRows = []
      if (data.rows && Array.isArray(data.rows)) {
        // Si rows[0] existe et est un tableau, c'est la vraie liste
        if (data.rows.length > 0 && Array.isArray(data.rows[0])) {
          actualRows = data.rows[0]
        } else {
          // Sinon, rows est directement la liste
          actualRows = data.rows
        }
      }
      
      return {
        success: true,
        data: actualRows,
        total: data.total || 0,
        message: data.message || 'Sanctions retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching sanctions:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch sanctions'
      }
    }
  }

  async getAll(filter?: SanctionRestFilter): Promise<ApiResponse<SanctionRestDto[]>> {
    return this.getSanctions(filter)
  }

  async getSanctionById(id: number): Promise<ApiResponse<SanctionRestDto>> {
    try {
      const response = await api.post('/rh/carriere/sanctions/trouver', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as SanctionRestDto,
        message: 'Sanction retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching sanction:', error)
      return {
        success: false,
        data: {} as SanctionRestDto,
        message: 'Failed to fetch sanction'
      }
    }
  }

  async createSanction(sanction: SanctionRestDto): Promise<ApiResponse<SanctionRestDto>> {
    try {
      const response = await api.post('/rh/carriere/sanctions/enregistrer', {
        // Pas d'ID pour la création
        idTypeSanction: sanction.idTypeSanction,
        faute: sanction.faute,
        commentaire: sanction.commentaire
      })
      const data = response.data
      
      return {
        success: true,
        data: data as SanctionRestDto,
        message: 'Sanction created successfully'
      }
    } catch (error) {
      console.error('Error creating sanction:', error)
      return {
        success: false,
        data: {} as SanctionRestDto,
        message: 'Failed to create sanction'
      }
    }
  }

  async updateSanction(id: number, sanction: SanctionRestDto): Promise<ApiResponse<SanctionRestDto>> {
    try {
      const response = await api.post('/rh/carriere/sanctions/enregistrer', {
        id, // ID obligatoire pour la modification
        idTypeSanction: sanction.idTypeSanction,
        faute: sanction.faute,
        commentaire: sanction.commentaire
      })
      const data = response.data
      
      return {
        success: true,
        data: data as SanctionRestDto,
        message: 'Sanction updated successfully'
      }
    } catch (error) {
      console.error('Error updating sanction:', error)
      return {
        success: false,
        data: {} as SanctionRestDto,
        message: 'Failed to update sanction'
      }
    }
  }

  async deleteSanction(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/rh/carriere/sanctions/supprimer', { id })
      
      return {
        success: true,
        data: true,
        message: 'Sanction deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting sanction:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete sanction'
      }
    }
  }

  async getTypesSanctions(): Promise<ApiResponse<any[]>> {
    try {
      const response = await api.get('/rh/carriere/sanctions/types-sanctions')
      
      return {
        success: true,
        data: response.data as any[],
        message: 'Types sanctions retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching types sanctions:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch types sanctions'
      }
    }
  }

  // Alias pour compatibilité
  async create(sanction: SanctionRestDto): Promise<ApiResponse<SanctionRestDto>> {
    return this.createSanction(sanction)
  }

  async update(sanction: SanctionRestDto): Promise<ApiResponse<SanctionRestDto>> {
    if (!sanction.id) {
      throw new Error('ID is required for update operation')
    }
    return this.updateSanction(sanction.id, sanction)
  }

  async delete(id: number): Promise<ApiResponse<boolean>> {
    return this.deleteSanction(id)
  }

  async getById(id: number): Promise<ApiResponse<SanctionRestDto>> {
    return this.getSanctionById(id)
  }
}

export const sanctionrestService = new SanctionRestService()
export default sanctionrestService
