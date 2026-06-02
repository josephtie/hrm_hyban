import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface RubriqueRestDto {
  id?: number
  code?: string
  libelle?: string
  typeRubrique?: string
  categorie?: string
  modeCalcul?: string
  valeur?: number
  montant?: number | undefined
  taux?: number | undefined
  formule?: string
  typeImposition?: string
  afficherBulletin?: boolean
  active?: boolean
  description?: string
  mtExedent?: number
  imposable?: boolean
  cotisable?: boolean
  dateCreation?: string
}

export interface RubriqueRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

class RubriqueRestService {
  
  async getAll(filter?: RubriqueRestFilter): Promise<ApiResponse<RubriqueRestDto[]>> {
    try {
      const response = await api.post('/parametrages/rubriques/lister', {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      })
      
      return {
        success: true,
        data: response.data || [],
        total: response.data?.length || 0,
        message: 'Rubriques retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [] as RubriqueRestDto[],
        message: error.response?.data?.message || 'Failed to retrieve rubriquerest'
      }
    }
  }

  async getById(id: number): Promise<ApiResponse<RubriqueRestDto>> {
    try {
      const response = await api.get(`/parametrages/rubriques/${id}`)
      
      return {
        success: true,
        data: response.data,
        message: 'Rubrique retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as RubriqueRestDto,
        message: error.response?.data?.message || 'Failed to retrieve rubrique'
      }
    }
  }

  async create(data: any): Promise<ApiResponse<RubriqueRestDto>> {
    try {
      // Préparation des données pour le backend avec les nouveaux champs
      const rubriqueRequest = {
        idR: null,
        code: data.code,
        libelle: data.libelle,
        typeRubrique: data.typeRubrique,
        categorie: data.categorie,
        modeCalcul: data.modeCalcul,
        valeur: data.valeur,
        montant: data.montant,
        taux: data.taux,
        formule: data.formule,
        typeImposition: data.typeImposition,
        afficherBulletin: data.afficherBulletin,
        active: data.active,
        description: data.description,
        mtExedent: data.mtExedent,
        imposable: data.imposable,
        cotisable: data.cotisable
      }
      
      const response = await api.post('/parametrages/rubriques/enregistrer', rubriqueRequest)
      
      return {
        success: true,
        data: response.data,
        message: 'Rubrique created successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as RubriqueRestDto,
        message: error.response?.data?.message || 'Failed to create rubrique'
      }
    }
  }

  async update(id: number, data: any): Promise<ApiResponse<RubriqueRestDto>> {
    try {
      // Préparation des données pour le backend avec les nouveaux champs
      const rubriqueRequest = {
        idR: id,
        code: data.code,
        libelle: data.libelle,
        typeRubrique: data.typeRubrique,
        categorie: data.categorie,
        modeCalcul: data.modeCalcul,
        valeur: data.valeur,
        montant: data.montant,
        taux: data.taux,
        formule: data.formule,
        typeImposition: data.typeImposition,
        afficherBulletin: data.afficherBulletin,
        active: data.active,
        description: data.description,
        mtExedent: data.mtExedent,
        imposable: data.imposable,
        cotisable: data.cotisable
      }
      
      const response = await api.put('/parametrages/rubriques/modifier', rubriqueRequest)
      
      return {
        success: true,
        data: response.data,
        message: 'Rubrique updated successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as RubriqueRestDto,
        message: error.response?.data?.message || 'Failed to update rubrique'
      }
    }
  }

  async delete(id: number | undefined): Promise<ApiResponse<void>> {
    try {
      await api.post('/parametrages/rubriques/supprimer', { idR: id })
      
      return {
        success: true,
        data: undefined,
        message: 'Rubrique deleted successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete rubrique'
      }
    }
  }
}

export const rubriquerestService = new RubriqueRestService()

