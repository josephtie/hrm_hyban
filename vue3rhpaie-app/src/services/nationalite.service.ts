import { api } from '@/services/api'

export interface Nationnalite {
  id: number
  libelle: string
}

export interface ApiResponse<T> {
  success: boolean
  data: T
  message: string
}

class NationnaliteService {
  // Récupérer toutes les nationalités
  async getAllNationalites(): Promise<ApiResponse<Nationnalite[]>> {
    try {
      const response = await api.get('/nationalites')
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data || [],
        message: data.message || 'Nationalités récupérées avec succès'
      }
    } catch (error) {
      console.error('Error fetching nationalities:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch nationalities'
      }
    }
  }

  // Récupérer une nationalité par ID
  async getNationaliteById(id: number): Promise<ApiResponse<Nationnalite>> {
    try {
      const response = await api.get(`/nationalites/${id}`)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Nationalité récupérée avec succès'
      }
    } catch (error) {
      console.error('Error fetching nationality:', error)
      return {
        success: false,
        data: {} as Nationnalite,
        message: 'Failed to fetch nationality'
      }
    }
  }

  // Créer une nouvelle nationalité
  async createNationalite(nationalite: Partial<Nationnalite>): Promise<ApiResponse<Nationnalite>> {
    try {
      const response = await api.post('/nationalites', nationalite)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Nationalité créée avec succès'
      }
    } catch (error) {
      console.error('Error creating nationality:', error)
      return {
        success: false,
        data: {} as Nationnalite,
        message: 'Failed to create nationality'
      }
    }
  }

  // Mettre à jour une nationalité
  async updateNationalite(id: number, nationalite: Partial<Nationnalite>): Promise<ApiResponse<Nationnalite>> {
    try {
      const response = await api.put(`/nationalites/${id}`, nationalite)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Nationalité mise à jour avec succès'
      }
    } catch (error) {
      console.error('Error updating nationality:', error)
      return {
        success: false,
        data: {} as Nationnalite,
        message: 'Failed to update nationality'
      }
    }
  }

  // Supprimer une nationalité
  async deleteNationalite(id: number): Promise<ApiResponse<boolean>> {
    try {
      const response = await api.delete(`/nationalites/${id}`)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Nationalité supprimée avec succès'
      }
    } catch (error) {
      console.error('Error deleting nationality:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete nationality'
      }
    }
  }

  // Fonction utilitaire pour convertir le code en ID
  getNationaliteIdByCode(nationalites: Nationnalite[], code: string): number {
    const found = nationalites.find(n => n.libelle === code)
    return found ? found.id : 1 // Valeur par défaut
  }
}

export const nationnaliteService = new NationnaliteService()
