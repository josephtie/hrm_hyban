import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import type { ApiResponse, ApiError } from '@/types/auth'

// Configuration de base
const API_BASE_URL = 'http://192.168.1.5:7200/api' // Connexion directe au backend prod
const REQUEST_TIMEOUT = 30000

// Création de l'instance Axios
export const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: REQUEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// Intercepteur pour les requêtes
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // Ajouter le token d'authentification si disponible
    const token = localStorage.getItem('access_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('🔑 Token envoyé:', token.substring(0, 50) + '...')
    } else {
      console.log('❌ Aucun token trouvé')
    }
    
    // Log pour le débogage
    console.log('🚀 Requête API:', config.method?.toUpperCase(), config.url)
    console.log('🚀 API Request:', config.method?.toUpperCase(), config.url, {
      params: config.params,
      data: config.data,
      headers: config.headers
    })
    
    return config
  },
  (error: AxiosError) => {
    console.error('❌ Request Error:', error)
    return Promise.reject(error)
  }
)

// Intercepteur pour les réponses
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

    // Si erreur 401 et qu'on n'a pas déjà essayé de rafraîchir le token
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        // Importer keycloakAuthService dynamiquement pour éviter les dépendances circulaires
        const keycloakAuthService = await import('./keycloak-auth.service')
        const newToken = await keycloakAuthService.default.refreshToken()
        
        if (newToken) {
          // Mettre à jour le header de la requête originale
          originalRequest.headers.Authorization = `Bearer ${newToken}`
          // Relancer la requête avec le nouveau token
          return api(originalRequest)
        }
      } catch (refreshError) {
        console.error('Erreur refresh token:', refreshError)
        // Rediriger vers login si le refresh échoue
        localStorage.removeItem('authToken')
        localStorage.removeItem('refreshToken')
        router.push('/login')
      }
    }
    
    // Gérer les autres erreurs
    if (error.response?.status === 401) {
      // Rediriger vers la page de connexion
      localStorage.removeItem('authToken')
      localStorage.removeItem('refreshToken')
      router.push('/login')
    }
    
    // Log de l'erreur pour le débogage
    console.error('❌ API Error:', error.config?.method?.toUpperCase(), error.config?.url, {
      status: error.response?.status,
      message: error.message,
      data: error.response?.data
    })
    
    return Promise.reject(error)
  }
)

// Token check utility
export const checkToken = () => {
  const token = localStorage.getItem('authToken')
  const tokenInfo = {
    token: token ? 'EXISTS' : 'MISSING',
    tokenLength: token?.length || 0,
    localStorage: {
      authToken: !!localStorage.getItem('authToken'),
      refreshToken: !!localStorage.getItem('refreshToken'),
      user: !!localStorage.getItem('user')
    }
  }
  console.log('🔑 Token check:', tokenInfo)
  return tokenInfo
}

export default api

// Extension de l'interface pour les métadonnées
declare module 'axios' {
  interface InternalAxiosRequestConfig {
    metadata?: {
      startTime?: Date
    }
  }
}

// Interceptor de requête
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // Ajout du token d'authentification
    const token = localStorage.getItem('access_token')
    
    // Debug: Vérifier le token
    if (import.meta.env.DEV) {
      console.log('🔑 Token check:', {
        token: token ? 'EXISTS' : 'MISSING',
        tokenLength: token?.length || 0,
        localStorage: {
          access_token: !!localStorage.getItem('access_token'),
          refresh_token: !!localStorage.getItem('refresh_token'),
          user: !!localStorage.getItem('user')
        }
      })
    }
    
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
      
      // Debug: Confirmer l'ajout du header
      if (import.meta.env.DEV) {
        console.log('🔐 Authorization header added:', config.headers.Authorization)
      }
    } else {
      // Debug: Token manquant
      if (import.meta.env.DEV) {
        console.warn('⚠️ No token available, request will be unauthenticated')
      }
    }

    // Ajout de l'ID de requête pour le tracking
    config.metadata = { startTime: new Date() }

    // Log en développement
    if (import.meta.env.DEV) {
      console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`, {
        params: config.params,
        data: config.data,
        headers: config.headers
      })
    }

    return config
  },
  (error: AxiosError) => {
    console.error('❌ Request Error:', error)
    return Promise.reject(error)
  }
)

// Interceptor de réponse
api.interceptors.response.use(
  (response: AxiosResponse) => {
    // Calcul du temps de réponse
    const endTime = new Date()
    const startTime = response.config.metadata?.startTime?.getTime()
    const duration = startTime ? endTime.getTime() - startTime : 0

    // Log en développement
    if (import.meta.env.DEV) {
      console.log(`✅ API Response: ${response.config.method?.toUpperCase()} ${response.config.url}`, {
        status: response.status,
        duration: `${duration}ms`,
        data: response.data
      })
    }

    return response
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

    // Log d'erreur
    if (import.meta.env.DEV) {
      console.error(`❌ API Error: ${originalRequest?.method?.toUpperCase()} ${originalRequest?.url}`, {
        status: error.response?.status,
        message: error.message,
        data: error.response?.data
      })
    }

    // Gestion des erreurs HTTP
    if (error.response) {
      const status = error.response.status
      const errorData = error.response.data as any

      switch (status) {
        case 401:
          // Token expiré ou invalide
          if (!originalRequest._retry) {
            originalRequest._retry = true
            
            try {
              // Tentative de rafraîchissement du token
              const refreshToken = localStorage.getItem('refresh_token')
              if (refreshToken) {
                const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
                  refreshToken
                })
                
                const { token, refreshToken: newRefreshToken } = response.data
                localStorage.setItem('access_token', token)
                localStorage.setItem('refresh_token', newRefreshToken)
                
                // Relancer la requête originale
                return api(originalRequest)
              }
            } catch (refreshError) {
              console.error('Token refresh failed:', refreshError)
            }
          }
          
          // Déconnexion et redirection vers login
          localStorage.removeItem('access_token')
          localStorage.removeItem('refresh_token')
          localStorage.removeItem('user')
          
          ElMessageBox.alert(
            'Votre session a expiré. Veuillez vous reconnecter.',
            'Session Expirée',
            {
              confirmButtonText: 'OK',
              type: 'warning'
            }
          ).then(() => {
            router.push('/login')
          })
          break

        case 403:
          ElMessage.error('Accès refusé. Permissions insuffisantes.')
          break

        case 404:
          ElMessage.error('Ressource non trouvée.')
          break

        case 422:
          // Erreurs de validation
          if (errorData?.errors && Array.isArray(errorData.errors)) {
            errorData.errors.forEach((err: string) => {
              ElMessage.error(err)
            })
          } else {
            ElMessage.error(errorData?.message || 'Erreur de validation')
          }
          break

        case 429:
          ElMessage.warning('Trop de requêtes. Veuillez réessayer plus tard.')
          break

        case 500:
          ElMessage.error('Erreur serveur. Veuillez contacter l\'administrateur.')
          break

        default:
          ElMessage.error(errorData?.message || 'Une erreur est survenue.')
      }
    } else if (error.request) {
      // Erreur réseau
      ElMessage.error('Erreur de connexion. Vérifiez votre réseau.')
    } else {
      // Erreur inattendue
      ElMessage.error('Une erreur inattendue est survenue.')
    }

    return Promise.reject(error)
  }
)

// Méthodes utilitaires
export const apiUtils = {
  // Vérifier si l'utilisateur est authentifié
  isAuthenticated: (): boolean => {
    const token = localStorage.getItem('access_token')
    const user = localStorage.getItem('user')
    return !!(token && user)
  },

  // Forcer le rechargement de la page si token manquant
  ensureAuth: (): void => {
    const token = localStorage.getItem('access_token')
    if (!token) {
      console.warn('⚠️ No token found, redirecting to login...')
      router.push('/login')
      return
    }
  },

  // Rafraîchir manuellement le token
  refreshToken: async (): Promise<boolean> => {
    try {
      const refreshToken = localStorage.getItem('refresh_token')
      if (!refreshToken) {
        console.warn('⚠️ No refresh token available')
        return false
      }

      const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
        refreshToken
      })
      
      const { token, refreshToken: newRefreshToken } = response.data
      localStorage.setItem('access_token', token)
      localStorage.setItem('refresh_token', newRefreshToken)
      
      console.log('✅ Token refreshed successfully')
      return true
    } catch (error) {
      console.error('❌ Token refresh failed:', error)
      return false
    }
  },

  // Construction de l'URL avec paramètres
  buildUrl: (endpoint: string, params?: Record<string, any>): string => {
    if (!params) return endpoint
    
    const url = new URL(endpoint, API_BASE_URL)
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        url.searchParams.append(key, String(value))
      }
    })
    return url.pathname + url.search
  },

  // Gestion du téléchargement de fichiers
  downloadFile: async (endpoint: string, filename?: string) => {
    try {
      const response = await api.get(endpoint, {
        responseType: 'blob'
      })
      
      const blob = new Blob([response.data])
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename || 'download'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      
      ElMessage.success('Téléchargement réussi')
    } catch (error) {
      ElMessage.error('Erreur lors du téléchargement')
      throw error
    }
  },

  // Upload de fichiers
  uploadFile: async (endpoint: string, file: File, onProgress?: (progress: number) => void) => {
    const formData = new FormData()
    formData.append('file', file)
    
    return api.post(endpoint, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress && progressEvent.total) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          onProgress(progress)
        }
      }
    })
  }
}
