import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface basée sur RubriqueRequest du backend
export interface RubriqueRestDto {
  idR?: number
  code?: string
  libelle?: string
  type?: string // Pour recevoir le type du frontend (1-6)
  etatImposition?: number
  modeCalcul?: string
  valeurDef?: number
  cotisable?: boolean
  active?: boolean
  permanent?: boolean
  speciale?: boolean
  description?: string
  categorie?: string
  formule?: string
  montant?: number
  taux?: number
  mtExedent?: number
  imposable?: boolean
  afficherBulletin?: boolean
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export interface RubriqueRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'code' | 'libelle' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
}

class RubriqueRestService {
  // Lister les rubriques avec pagination
  async getRubriques(filter?: RubriqueRestFilter): Promise<ApiResponse<RubriqueRestDto[]>> {
    try {
      const params = new URLSearchParams({
        limit: String(filter?.size ?? 10),
        offset: String(filter?.page ?? 0),
        search: filter?.search || ''
      })
      
      const response = await api.get(`/parametrages/rubriques/list?${params}`)
      const data = response.data
      
      return {
        success: true,
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Rubriques retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching rubriques:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch rubriques'
      }
    }
  }

  // Alias pour compatibilité
  async getAll(filter?: RubriqueRestFilter): Promise<ApiResponse<RubriqueRestDto[]>> {
    return this.getRubriques(filter)
  }

  // Récupérer une rubrique par son ID
  async getRubriqueById(id: number): Promise<ApiResponse<RubriqueRestDto>> {
    try {
      const response = await api.post('/parametrages/rubriques/trouver', { idR: id })
      const data = response.data
      
      return {
        success: true,
        data: data as RubriqueRestDto,
        message: 'Rubrique retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching rubrique:', error)
      return {
        success: false,
        data: {} as RubriqueRestDto,
        message: 'Failed to fetch rubrique'
      }
    }
  }

  // Créer une nouvelle rubrique
  async createRubrique(rubrique: RubriqueRestDto): Promise<ApiResponse<RubriqueRestDto>> {
    try {
      const response = await api.post('/parametrages/rubriques/enregistrer', {
        // Pas d'ID pour la création
        code: rubrique.code,
        libelle: rubrique.libelle,
        type: rubrique.type || String(rubrique.etatImposition || 1),
        modeCalcul: rubrique.modeCalcul,
        valeurDef: rubrique.valeurDef,
        cotisable: rubrique.cotisable,
        active: rubrique.active,
        permanent: rubrique.permanent,
        speciale: rubrique.speciale,
        description: rubrique.description,
        categorie: rubrique.categorie,
        formule: rubrique.formule,
        montant: rubrique.montant,
        taux: rubrique.taux,
        mtExedent: rubrique.mtExedent,
        imposable: rubrique.imposable,
        afficherBulletin: rubrique.afficherBulletin
      })
      const data = response.data
      
      return {
        success: true,
        data: data as RubriqueRestDto,
        message: 'Rubrique created successfully'
      }
    } catch (error) {
      console.error('Error creating rubrique:', error)
      return {
        success: false,
        data: {} as RubriqueRestDto,
        message: 'Failed to create rubrique'
      }
    }
  }

  // Mettre à jour une rubrique
  async updateRubrique(id: number, rubrique: RubriqueRestDto): Promise<ApiResponse<RubriqueRestDto>> {
    try {
      const response = await api.put('/parametrages/rubriques/modifier', {
        idR: id, // ID obligatoire pour la modification
        code: rubrique.code,
        libelle: rubrique.libelle,
        type: rubrique.type || String(rubrique.etatImposition || 1),
        modeCalcul: rubrique.modeCalcul,
        valeurDef: rubrique.valeurDef,
        cotisable: rubrique.cotisable,
        active: rubrique.active,
        permanent: rubrique.permanent,
        speciale: rubrique.speciale,
        description: rubrique.description,
        categorie: rubrique.categorie,
        formule: rubrique.formule,
        montant: rubrique.montant,
        taux: rubrique.taux,
        mtExedent: rubrique.mtExedent,
        imposable: rubrique.imposable,
        afficherBulletin: rubrique.afficherBulletin
      })
      const data = response.data
      
      return {
        success: true,
        data: data as RubriqueRestDto,
        message: 'Rubrique updated successfully'
      }
    } catch (error) {
      console.error('Error updating rubrique:', error)
      return {
        success: false,
        data: {} as RubriqueRestDto,
        message: 'Failed to update rubrique'
      }
    }
  }

  // Supprimer une rubrique
  async deleteRubrique(id: number): Promise<ApiResponse<boolean>> {
    try {
      await api.post('/parametrages/rubriques/supprimer', { idR: id })
      
      return {
        success: true,
        data: true,
        message: 'Rubrique deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting rubrique:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete rubrique'
      }
    }
  }

  // Méthodes alias pour compatibilité
  async create(rubrique: RubriqueRestDto): Promise<ApiResponse<RubriqueRestDto>> {
    return this.createRubrique(rubrique)
  }

  async update(rubrique: RubriqueRestDto): Promise<ApiResponse<RubriqueRestDto>> {
    return this.updateRubrique(rubrique.idR || 0, rubrique)
  }

  async delete(id: number): Promise<ApiResponse<boolean>> {
    return this.deleteRubrique(id)
  }

  async getById(id: number): Promise<ApiResponse<RubriqueRestDto>> {
    return this.getRubriqueById(id)
  }
}

export const rubriqvariablerestService = new RubriqueRestService()
export default rubriqvariablerestService
