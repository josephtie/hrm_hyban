import axios from 'axios'
import type { ApiResponse } from '@/types/auth'
import { API_CONFIG } from '@/config/api'

const API_BASE_URL = API_CONFIG.API_BASE_URL

// Fonction pour obtenir le token d'authentification
const getAuthToken = (): string | null => {
  return localStorage.getItem('access_token')
}

// Créer une instance axios avec intercepteur pour l'authentification
const createAuthenticatedApi = () => {
  const api = axios.create({
    baseURL: `${API_BASE_URL}/api`,
    timeout: 30000,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Intercepteur pour ajouter le token à chaque requête
  api.interceptors.request.use(
    (config) => {
      const token = getAuthToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    },
    (error) => {
      return Promise.reject(error)
    }
  )

  // Intercepteur pour gérer les erreurs 401
  api.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('access_token')
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )

  return api
}

// Interfaces basées sur la structure backend
export interface Echelonnement {
  id: number
  idechel: number
  periodPaie: string
  montantEcheance: number
  montantPaye: number
  resteAPayer: number
  statut: string
  dateEcheance: string
  datePaiement?: string
  pretPersonnel?: {
    id: number
    montant: number
    type: string
    personnel: {
      id: number
      nomComplet: string
      matricule: string
    }
  }
}

export interface EchelonnementDTO {
  rows: Echelonnement[]
  total: number
  result: string
  message?: string
}

export interface EchelonnementRequest {
  idechel: number
  periodPaie: string
}

export interface EchelonnementFilter {
  limit?: number
  offset?: number
  search?: string
  matricule?: string
  nom?: string
  prenom?: string
  statut?: boolean
  periodePaieId?: number
}

export interface PeriodePaie {
  id: number
  affiche: string
  libelle?: string
  annee?: {
    annee: number
  }
  mois?: {
    mois: number
  }
}

class EchelonnementRestService {
  private api = createAuthenticatedApi()

  // Récupérer la liste des échéanciers
  async getEchelonnements(filter?: EchelonnementFilter): Promise<ApiResponse<EchelonnementDTO>> {
    try {
      const params: any = {
        limit: filter?.limit || 20,
        offset: filter?.offset || 0
      }

      // Ajouter les paramètres de recherche seulement s'ils sont définis
      if (filter?.search && filter.search.trim() !== '') {
        params.search = filter.search
      }
      if (filter?.matricule && filter.matricule.trim() !== '') {
        params.matricule = filter.matricule
      }
      if (filter?.nom && filter.nom.trim() !== '') {
        params.nom = filter.nom
      }
      if (filter?.prenom && filter.prenom.trim() !== '') {
        params.prenom = filter.prenom
      }
      if (filter?.statut !== undefined) {
        params.statut = filter.statut
      }
      if (filter?.periodePaieId) {
        params.periodePaieId = filter.periodePaieId
      }

      const response = await this.api.get('/paie/echelonnement/list', { params })
      
      return {
        success: true,
        data: response.data,
        total: response.data.total || 0,
        message: 'Échéanciers récupérés avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: { rows: [], total: 0, result: 'error' },
        message: error.response?.data?.message || 'Erreur lors de la récupération des échéanciers'
      }
    }
  }

  // Récupérer les échéanciers d'un prêt spécifique
  async getEchelonnementsByPret(idPret: number): Promise<ApiResponse<EchelonnementDTO>> {
    try {
      const response = await this.api.get(`/paie/echelonnement/pret/${idPret}`)
      
      return {
        success: true,
        data: response.data,
        total: response.data.total || 0,
        message: 'Échéanciers du prêt récupérés avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: { rows: [], total: 0, result: 'error' },
        message: error.response?.data?.message || 'Erreur lors de la récupération des échéanciers du prêt'
      }
    }
  }

  // Mettre à jour un échéancier (marquer comme payé)
  async updateEchelonnement(request: EchelonnementRequest): Promise<ApiResponse<Echelonnement>> {
    try {
      const response = await this.api.post('/paie/echelonnement/update', request)
      
      return {
        success: true,
        data: response.data.row,
        message: 'Échéancier mis à jour avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as Echelonnement,
        message: error.response?.data?.message || 'Erreur lors de la mise à jour de l\'échéancier'
      }
    }
  }

  // Récupérer les périodes de paie
  async getPeriodesPaie(): Promise<ApiResponse<PeriodePaie[]>> {
    try {
      const response = await this.api.get('/paie/echelonnement/periodes')
      
      return {
        success: true,
        data: response.data,
        total: response.data.length,
        message: 'Périodes récupérées avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Erreur lors de la récupération des périodes'
      }
    }
  }

  // Marquer plusieurs échéanciers comme payés
  async markMultipleAsPaid(echelonnementIds: number[]): Promise<ApiResponse<Echelonnement[]>> {
    try {
      const results = await Promise.all(
        echelonnementIds.map(id => 
          this.updateEchelonnement({
            idechel: id,
            periodPaie: new Date().toISOString().split('T')[0]
          })
        )
      )
      
      return {
        success: true,
        data: results.map(r => r.data).filter(Boolean),
        total: results.length,
        message: 'Échéanciers marqués comme payés avec succès'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Erreur lors du marquage des échéanciers'
      }
    }
  }

  // Exporter les échéanciers en CSV
  async exportEcheanciers(filter?: EchelonnementFilter): Promise<ApiResponse<Blob>> {
    try {
      const params: any = {}

      // Ajouter les paramètres de recherche seulement s'ils sont définis
      if (filter?.search && filter.search.trim() !== '') {
        params.search = filter.search
      }
      if (filter?.matricule && filter.matricule.trim() !== '') {
        params.matricule = filter.matricule
      }
      if (filter?.nom && filter.nom.trim() !== '') {
        params.nom = filter.nom
      }
      if (filter?.prenom && filter.prenom.trim() !== '') {
        params.prenom = filter.prenom
      }
      if (filter?.statut !== undefined) {
        params.statut = filter.statut
      }
      if (filter?.periodePaieId) {
        params.periodePaieId = filter.periodePaieId
      }
      
      const response = await this.api.get('/paie/echelonnement/export', { 
        params,
        responseType: 'blob' // Important pour télécharger des fichiers
      })
      
      return {
        success: true,
        data: response.data,
        message: 'Export des échéanciers réussi'
      }
    } catch (error: any) {
      return {
        success: false,
        data: new Blob(),
        message: error.response?.data?.message || 'Erreur lors de l\'export des échéanciers'
      }
    }
  }
}

export const echelonnementrestService = new EchelonnementRestService()
export default echelonnementrestService
