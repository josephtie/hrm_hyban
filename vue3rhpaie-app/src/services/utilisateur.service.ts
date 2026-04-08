import axios from 'axios'

const API_BASE_URL = 'http://192.168.1.8:7200/api/users'

export interface UtilisateurDto {
  id: number
  username: string
  password?: string
  email: string
  nomComplet: string
  enabled: boolean
  accountNonExpired: boolean
  credentialsNonExpired: boolean
  accountNonLocked: boolean
  authorities?: { authority: string }[]
}

export interface UtilisateurVueRequest {
  id?: number
  username: string
  email: string
  nomComplet: string
  password?: string
  dateNaissance?: string
  telephone?: string
  adresse?: string
  idRole?: number
  actif?: boolean
}

export interface UtilisateurBackendResponse {
  id: number
  utilisateur: {
    id: number
    username: string
    password?: string
    email: string
    nomComplet: string
    enabled: boolean
    accountNonExpired: boolean
    credentialsNonExpired: boolean
    accountNonLocked: boolean
  }
  role: {
    id: number
    name: string
    authority: string
  }
}

export interface UtilisateurVueResponse<T> {
  rows: UtilisateurBackendResponse[]
  total: number
  result: string
  message?: string
}

export interface PaginationRequest {
  offset?: number
  limit?: number
  search?: string
  username?: string
  role?: string
}

class UtilisateurService {
  private api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Récupérer la liste paginée des utilisateurs
  async getAll(request: PaginationRequest = {}): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/list', request)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération des utilisateurs:', error)
      throw error
    }
  }

  // Récupérer un utilisateur par son ID
  async getById(id: number): Promise<UtilisateurDto> {
    try {
      const response = await this.api.get(`/${id}`)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de l\'utilisateur:', error)
      throw error
    }
  }

  // Créer un nouvel utilisateur
  async create(utilisateur: UtilisateurVueRequest): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/save', utilisateur)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la création de l\'utilisateur:', error)
      throw error
    }
  }

  // Mettre à jour un utilisateur
  async update(utilisateur: UtilisateurVueRequest): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/update', utilisateur)
      return response.data
    } catch (error) {
      console.error('Erreur lors de la mise à jour de l\'utilisateur:', error)
      throw error
    }
  }

  // Supprimer un utilisateur
  async delete(id: number): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/delete', { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la suppression de l\'utilisateur:', error)
      throw error
    }
  }

  // Changer le statut d'un utilisateur
  async toggleStatus(id: number): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/toggle-status', { id })
      return response.data
    } catch (error) {
      console.error('Erreur lors du changement de statut:', error)
      throw error
    }
  }

  // Réinitialiser le mot de passe
  async resetPassword(email: string): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/reset-password', { email })
      return response.data
    } catch (error) {
      console.error('Erreur lors de la réinitialisation du mot de passe:', error)
      throw error
    }
  }

  // Changer le mot de passe
  async changePassword(id: number, oldPassword: string, newPassword: string): Promise<UtilisateurVueResponse<UtilisateurDto>> {
    try {
      const response = await this.api.post('/change-password', { id, ancienMotDePasse: oldPassword, nouveauMotDePasse: newPassword })
      return response.data
    } catch (error) {
      console.error('Erreur lors du changement de mot de passe:', error)
      throw error
    }
  }

  // Récupérer tous les utilisateurs (sans pagination)
  async getAllUtilisateurs(): Promise<UtilisateurDto[]> {
    try {
      const response = await this.api.get('/list/all')
      return response.data
    } catch (error) {
      console.error('Erreur lors de la récupération de tous les utilisateurs:', error)
      throw error
    }
  }
}

export default new UtilisateurService()
