import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec l'entité CpteVirmtBanque
export interface CompteVirementDto {
  id?: number
  numguichCpteVirmt?: string
  numcpteCpteVirmt?: string
  donneurOrdCpteVirmt?: string
  codEtablVirmt?: string
  ribCpteVirmt?: number
  bank?: {
    id: number
    sigle?: string
    codbanq?: string
    libelle?: string
  }
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface CompteVirementFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'codEtablVirmt' | 'idbank' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class CompteVirementService {
  // Lister les comptes de virement avec pagination
  async getComptesVirement(filter?: CompteVirementFilter): Promise<ApiResponse<CompteVirementDto[]>> {
    try {
      const params = new URLSearchParams({
        limit: String(filter?.size ?? 10),
        offset: String(filter?.page ?? 0),
        search: filter?.search || ''
      })
      
      const response = await api.get(`/parametrages/cpte-virement/list?${params}`)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Comptes de virement retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching comptes virement:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch comptes virement'
      }
    }
  }

  // Alias pour compatibilité
  async getAll(filter?: CompteVirementFilter): Promise<ApiResponse<CompteVirementDto[]>> {
    return this.getComptesVirement(filter)
  }

  // Récupérer un compte de virement par son ID
  async getCompteVirementById(id: number): Promise<ApiResponse<CompteVirementDto>> {
    try {
      const response = await api.post('/parametrages/cpte-virement/trouver', { id })
      const data = response.data
      
      return {
        success: true,
        data: data as CompteVirementDto,
        message: 'Compte de virement retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching compte virement:', error)
      return {
        success: false,
        data: {} as CompteVirementDto,
        message: 'Failed to fetch compte virement'
      }
    }
  }

  // Créer un nouveau compte de virement
  async createCompteVirement(compte: CompteVirementDto): Promise<ApiResponse<CompteVirementDto>> {
    try {
      const response = await api.post('/parametrages/cpte-virement/enregistrer', {
        // Pas d'ID pour la création
        codEtablVirmt: compte.codEtablVirmt,
        donneurOrdCpteVirmt: compte.donneurOrdCpteVirmt,
        idbank: compte.bank?.id,
        numcpteCpteVirmt: compte.numcpteCpteVirmt,
        numguichCpteVirmt: compte.numguichCpteVirmt,
        ribCpteVirmt: compte.ribCpteVirmt
      })
      const data = response.data
      
      return {
        success: true,
        data: data as CompteVirementDto,
        message: 'Compte de virement created successfully'
      }
    } catch (error) {
      console.error('Error creating compte virement:', error)
      return {
        success: false,
        data: {} as CompteVirementDto,
        message: 'Failed to create compte virement'
      }
    }
  }

  // Mettre à jour un compte de virement
  async updateCompteVirement(id: number, compte: CompteVirementDto): Promise<ApiResponse<CompteVirementDto>> {
    try {
      const response = await api.post('/parametrages/cpte-virement/enregistrer', {
        id: id,
        codEtablVirmt: compte.codEtablVirmt,
        donneurOrdCpteVirmt: compte.donneurOrdCpteVirmt,
        idbank: compte.bank?.id,
        numcpteCpteVirmt: compte.numcpteCpteVirmt,
        numguichCpteVirmt: compte.numguichCpteVirmt,
        ribCpteVirmt: compte.ribCpteVirmt
      })
      const data = response.data
      
      return {
        success: true,
        data: data as CompteVirementDto,
        message: 'Compte de virement updated successfully'
      }
    } catch (error) {
      console.error('Error updating compte virement:', error)
      return {
        success: false,
        data: {} as CompteVirementDto,
        message: 'Failed to update compte virement'
      }
    }
  }

  // Supprimer un compte de virement
  async deleteCompteVirement(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/parametrages/cpte-virement/supprimer', { id })
      
      return {
        success: true,
        data: true,
        message: 'Compte de virement deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting compte virement:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete compte virement'
      }
    }
  }

  // Méthodes alias pour compatibilité
  async create(compte: CompteVirementDto): Promise<ApiResponse<CompteVirementDto>> {
    return this.createCompteVirement(compte)
  }

  async update(compte: CompteVirementDto): Promise<ApiResponse<CompteVirementDto>> {
    return this.updateCompteVirement(compte.id || 0, compte)
  }

  async delete(id: number): Promise<ApiResponse<boolean>> {
    return this.deleteCompteVirement(id)
  }

  async getById(id: number): Promise<ApiResponse<CompteVirementDto>> {
    return this.getCompteVirementById(id)
  }

  // Récupérer les banques disponibles
  async getBanques(): Promise<ApiResponse<any[]>> {
    try {
      const response = await api.get('/parametrages/cpte-virement/banques')
      return {
        success: true,
        data: response.data,
        message: 'Banques retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching banques:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch banques'
      }
    }
  }

  // Récupérer les sociétés disponibles
  async getSocietes(): Promise<ApiResponse<any[]>> {
    try {
      const response = await api.get('/parametrages/cpte-virement/societes')
      return {
        success: true,
        data: response.data,
        message: 'Societes retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching societes:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch societes'
      }
    }
  }
}

export const compteVirementService = new CompteVirementService()
export default compteVirementService
