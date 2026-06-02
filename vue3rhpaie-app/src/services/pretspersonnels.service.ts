import axios from 'axios'
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
    timeout: 30000, // Augmenté à 30s pour les gros volumes de données
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
        // Token expiré ou invalide, rediriger vers la page de connexion
        localStorage.removeItem('access_token')
        localStorage.removeItem('refresh_token')
        localStorage.removeItem('user')
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )

  return api
}

export interface PretPersonnelRequest {
  id?: number
  montant: number
  echelonage: number
  pret: number
  idpers: number
  dEmprunt: string
  periodepaie: number
}

export interface PretPersonnelResponse {
  id: number
  montant: number
  echelonage: number
  pret: { id: number; libelle: string; createdAt?: string; updatedAt?: string }
  idpers: number
  dEmprunt: string
  dateEmprunt?: string
  periodepaie?: number
  periode: { id: number; affiche: string; datedeb: string; ddeb: string; datefin: string; dfin: string }
  personnel: {
    id: number
    nom: string
    prenom: string
    nomComplet: string
    matricule: string
    createdAt: string
    createdBy: string
    updatedAt: string
    updatedBy: string
    [key: string]: any
  }
  employee?: any
  libelle?: string
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
  total?: number
  result?: string
  message?: string
  resultBoolean?: boolean
}

export interface PretPersonnelListResponse {
  rows: PretPersonnelResponse[]
  total: number
  result: string
  message?: string
}

export interface Personnel {
  id: number
  matricule: string
  nomComplet: string
  prenom?: string
  nom?: string
  dateNaissance?: string
  fonction?: string
  categorie?: string
}

export interface PeriodePaie {
  id: number
  periode: string
  libelle: string
  dateDebut: string
  dateFin: string
  statut: string
}

class PretsPersonnelsService {
  private api = createAuthenticatedApi()

  // Récupérer la liste des prêts personnels
  async getPretsPersonnels(limit = 10, offset = 0, search?: string): Promise<PretPersonnelListResponse> {
    try {
      const params = new URLSearchParams({
        limit: limit.toString(),
        offset: offset.toString()
      })
      
      if (search) {
        params.append('search', search)
      }

      const response = await this.api.get(`/paie/pret-personnel/list?${params}`)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des prêts personnels:', error)
      throw error
    }
  }

  // Enregistrer un nouveau prêt personnel
  async savePretPersonnel(pret: PretPersonnelRequest): Promise<any> {
    try {
      const response = await this.api.post('/paie/pret-personnel/enregistrer', pret)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création du prêt:', error)
      throw error
    }
  }

  async savePretEmployee(pret: PretPersonnelRequest): Promise<any> {
    try {
      const params = new URLSearchParams()
      params.append('montant1', String(pret.montant))
      params.append('echelonage1', String(pret.echelonage))
      params.append('pret1', String(pret.pret))
      params.append('idpers1', String(pret.idpers))
      params.append('dEmprunt1', pret.dEmprunt)
      params.append('periodepaie1', String(pret.periodepaie))

      const response = await this.api.post('/paie/pret-personnel/savepretEmployee', params, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création du prêt agent spécifique:', error)
      throw error
    }
  }

  // Modifier un prêt personnel
  async updatePretPersonnel(id: number, pret: PretPersonnelRequest): Promise<any> {
    try {
      const response = await this.api.post(`/paie/pret-personnel/modifier`, pret)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la modification du prêt:', error)
      throw error
    }
  }

  // Modifier un prêt personnel existant
  async updatePretPersonnelOld(
    idpret: number,
    montant: number,
    echelonage: number,
    pret: number,
    idpers: number,
    dEmprunt: string,
    periodepaie: number
  ): Promise<PretPersonnelResponse> {
    try {
      const params = new URLSearchParams({
        idpret: idpret.toString(),
        montant1: montant.toString(),
        echelonage1: echelonage.toString(),
        pret1: pret.toString(),
        idpers1: idpers.toString(),
        dEmprunt1: dEmprunt,
        periodepaie1: periodepaie.toString()
      })

      const response = await this.api.get(`/paie/pret-personnel/modifier?${params}`)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la modification du prêt personnel:', error)
      throw error
    }
  }

  // Supprimer un prêt personnel
  async deletePretPersonnel(id: number): Promise<void> {
    try {
      await this.api.delete(`/paie/pret-personnel/individuel/${id}`)
    } catch (error) {
      console.error('Erreur lors de la suppression du prêt personnel:', error)
      throw error
    }
  }

  // Récupérer les périodes de paie disponibles pour les prêts
  async getPeriodesPaie(): Promise<PeriodePaie[]> {
    try {
      const response = await this.api.get('/paie/pret-personnel/periodes')
      const data = response.data
      return data.rows || (Array.isArray(data) ? data : data.row ? [data.row] : [])
    } catch (error) {
      console.error('Erreur lors de la récupération des périodes de paie:', error)
      throw error
    }
  }

  // Récupérer la liste des personnels (pour le select)
  async getPersonnels(): Promise<Personnel[]> {
    try {
      const response = await this.api.get('/personnels/personnel/list/all')
      return response.data || []
    } catch (error) {
      console.error('Erreur lors de la récupération des personnels:', error)
      throw error
    }
  }

  // Calculer la simulation d'un prêt
  async calculerSimulation(
    montant: number,
    duree: number,
    tauxInteret: number
  ): Promise<{ mensualite: number; totalInteret: number; totalPaiement: number }> {
    try {
      // Calcul de la mensualité avec la formule d'amortissement
      const principal = montant
      const monthlyRate = tauxInteret / 100 / 12
      const months = duree

      let mensualite: number
      if (monthlyRate === 0) {
        mensualite = principal / months
      } else {
        mensualite = principal * (monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1)
      }

      const totalPaiement = mensualite * months
      const totalInteret = totalPaiement - principal

      return {
        mensualite: Math.round(mensualite),
        totalInteret: Math.round(totalInteret),
        totalPaiement: Math.round(totalPaiement)
      }
    } catch (error) {
      console.error('Erreur lors du calcul de simulation:', error)
      throw error
    }
  }
}

export const pretspersonnelsService = new PretsPersonnelsService()
export type { PretPersonnelRequest, PretPersonnelResponse, PretPersonnelListResponse, Personnel, PeriodePaie }
