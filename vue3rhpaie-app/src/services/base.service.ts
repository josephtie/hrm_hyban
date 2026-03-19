import { api } from './api'

export class BaseService {
  protected baseUrl: string

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl
  }

  // Méthode GET générique
  async get<T = any>(endpoint = '', params = {}): Promise<T> {
    try {
      const response = await api.get<T>(`${this.baseUrl}${endpoint}`, { params })
      return response.data
    } catch (error) {
      console.error(`GET ${this.baseUrl}${endpoint} error:`, error)
      throw error
    }
  }

  // Méthode POST générique
  async post<T = any>(endpoint = '', data = {}, params = {}): Promise<T> {
    try {
      const response = await api.post<T>(`${this.baseUrl}${endpoint}`, data, { params })
      return response.data
    } catch (error) {
      console.error(`POST ${this.baseUrl}${endpoint} error:`, error)
      throw error
    }
  }

  // Méthode PUT générique
  async put<T = any>(endpoint = '', data = {}, params = {}): Promise<T> {
    try {
      const response = await api.put<T>(`${this.baseUrl}${endpoint}`, data, { params })
      return response.data
    } catch (error) {
      console.error(`PUT ${this.baseUrl}${endpoint} error:`, error)
      throw error
    }
  }

  // Méthode DELETE générique
  async delete<T = any>(endpoint = '', params = {}): Promise<T> {
    try {
      const response = await api.delete<T>(`${this.baseUrl}${endpoint}`, { params })
      return response.data
    } catch (error) {
      console.error(`DELETE ${this.baseUrl}${endpoint} error:`, error)
      throw error
    }
  }

  // Méthode pour uploader des fichiers
  async upload<T = any>(endpoint = '', formData: FormData): Promise<T> {
    try {
      const response = await api.post<T>(`${this.baseUrl}${endpoint}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      return response.data
    } catch (error) {
      console.error(`UPLOAD ${this.baseUrl}${endpoint} error:`, error)
      throw error
    }
  }

  // Méthode pour télécharger des fichiers
  async download(endpoint = '', params = {}): Promise<Blob> {
    try {
      const response = await api.get(`${this.baseUrl}${endpoint}`, { 
        params,
        responseType: 'blob'
      })
      return response.data
    } catch (error) {
      console.error(`DOWNLOAD ${this.baseUrl}${endpoint} error:`, error)
      throw error
    }
  }
}

// Service pour les utilisateurs
export class UserService extends BaseService {
  constructor() {
    super('/api/users')
  }

  async getAll() {
    return this.get<User[]>()
  }

  async getById(id: string) {
    return this.get<User>(`/${id}`)
  }

  async create(userData: Partial<User>) {
    return this.post<User>('', userData)
  }

  async update(id: string, userData: Partial<User>) {
    return this.put<User>(`/${id}`, userData)
  }

  async delete(id: string) {
    return this.delete(`/${id}`)
  }
}

// Service pour l'authentification
export class AuthService extends BaseService {
  constructor() {
    super('/auth')
  }

  async login(credentials: { username: string; password: string }) {
    return this.post('/login', credentials)
  }

  async refreshToken(refreshToken: string) {
    return this.post('/refresh', { refresh_token: refreshToken })
  }

  async logout() {
    return this.post('/logout')
  }

  async getCurrentUser() {
    return this.get<User>('/me')
  }
}
