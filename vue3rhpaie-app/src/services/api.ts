import axios from 'axios'
import type { AxiosInstance, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import type { ApiResponse, ApiError } from '@/types/auth'
import { API_CONFIG, API_URLS } from '@/config/api'

// Configuration centralisée
const API_BASE_URL = API_URLS.BASE
const REQUEST_TIMEOUT = API_CONFIG.TIMEOUT

// Création de l'instance Axios
export const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: REQUEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// ============================================
// INTERCEPTOR DE REQUÊTE (Unique)
// ============================================
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 1. Ajouter le token d'authentification s'il existe
    const token = localStorage.getItem('access_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('🔑 Token ajouté au header Authorization')
    } else {
      console.log('⚠️ Aucun token trouvé dans localStorage')
    }

    // 2. Gérer le Content-Type pour FormData
    if (config.data instanceof FormData) {
      // Laisser Axios définir le Content-Type automatiquement avec le boundary
      delete config.headers['Content-Type']
      console.log('📤 FormData détecté - Content-Type laissé à Axios')
    }

    // 3. Tracker le temps de la requête pour les logs
    ;(config as any).metadata = { startTime: new Date() }

    // 4. Logs en développement
    if (import.meta.env.DEV) {
      console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`, {
        params: config.params,
        hasData: !!config.data,
        hasToken: !!token,
        headers: {
          'Content-Type': config.headers['Content-Type'],
          'Authorization': config.headers.Authorization ? 'Bearer ***' : 'none'
        }
      })
    }

    return config
  },
  (error: AxiosError) => {
    console.error('❌ Erreur dans la requête:', error)
    return Promise.reject(error)
  }
)

// ============================================
// INTERCEPTOR DE RÉPONSE (Unique)
// ============================================
api.interceptors.response.use(
  (response: AxiosResponse) => {
    // Logs en développement
    if (import.meta.env.DEV) {
      const startTime = (response.config as any).metadata?.startTime?.getTime()
      const duration = startTime ? new Date().getTime() - startTime : 0
      console.log(`✅ API Response: ${response.config.method?.toUpperCase()} ${response.config.url}`, {
        status: response.status,
        duration: `${duration}ms`
      })
    }
    return response
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

    // ============================================
    // GESTION DES ERREURS
    // ============================================

    // 1. Erreur 401 - Token expiré ou invalide
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      console.log('🔄 Token expiré - Tentative de rafraîchissement...')

      try {
        // Charger dynamiquement pour éviter les imports circulaires
        const { keycloakAuthService } = await import('./keycloak-auth.service')
        const newToken = await keycloakAuthService.refreshToken()

        if (newToken) {
          console.log('✅ Token rafraîchi avec succès')
          // Mettre à jour le token en localStorage
          localStorage.setItem('access_token', newToken)
          // Réessayer la requête originale avec le nouveau token
          originalRequest.headers.Authorization = `Bearer ${newToken}`
          return api(originalRequest)
        }
      } catch (refreshError) {
        console.error('❌ Erreur lors du rafraîchissement du token:', refreshError)
      }

      // Si rafraîchissement échoue, déconnecter l'utilisateur
      console.log('🚪 Déconnexion - Redirection vers login')
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      localStorage.removeItem('user')

      ElMessageBox.alert(
        'Votre session a expiré. Veuillez vous reconnecter.',
        'Session Expirée',
        {
          confirmButtonText: 'OK',
          type: 'warning',
          callback: () => {
            router.push('/login')
          }
        }
      )
      return Promise.reject(error)
    }

    // 2. Erreur 403 - Accès refusé
    if (error.response?.status === 403) {
      ElMessage.error('Accès refusé. Permissions insuffisantes.')
      return Promise.reject(error)
    }

    // 3. Erreur 404 - Ressource non trouvée
    if (error.response?.status === 404) {
      console.warn('Resource not found:', originalRequest.url)
      return Promise.reject(error)
    }

    // 4. Erreur 422 - Validation échouée
    if (error.response?.status === 422) {
      const errorData = error.response.data as any
      if (errorData?.errors && Array.isArray(errorData.errors)) {
        errorData.errors.forEach((err: string) => {
          ElMessage.error(err)
        })
      } else {
        ElMessage.error(errorData?.message || 'Erreur de validation')
      }
      return Promise.reject(error)
    }

    // 5. Erreur 429 - Trop de requêtes
    if (error.response?.status === 429) {
      ElMessage.warning('Trop de requêtes. Veuillez réessayer plus tard.')
      return Promise.reject(error)
    }

    // 6. Erreur 500 - Erreur serveur
    if (error.response?.status === 500) {
      ElMessage.error('Erreur serveur. Veuillez contacter l\'administrateur.')
      return Promise.reject(error)
    }

    // 7. Autres erreurs
    if (import.meta.env.DEV) {
      console.error(`❌ API Error: ${originalRequest?.method?.toUpperCase()} ${originalRequest?.url}`, {
        status: error.response?.status,
        message: error.message,
        data: error.response?.data
      })
    }

    return Promise.reject(error)
  }
)

// ============================================
// UTILITAIRES
// ============================================

/**
 * Vérifier l'état du token
 */
export const checkToken = () => {
  const token = localStorage.getItem('access_token')
  const refreshToken = localStorage.getItem('refresh_token')
  const user = localStorage.getItem('user')

  const tokenInfo = {
    hasAccessToken: !!token,
    hasRefreshToken: !!refreshToken,
    hasUser: !!user,
    accessTokenLength: token?.length || 0,
    accessTokenPreview: token ? `${token.substring(0, 20)}...${token.substring(token.length - 10)}` : 'none',
    user: user ? JSON.parse(user) : null
  }

  console.log('🔑 Token Status:', tokenInfo)
  return tokenInfo
}

/**
 * Déconnecter l'utilisateur
 */
export const logout = async () => {
  localStorage.removeItem('access_token')
  localStorage.removeItem('refresh_token')
  localStorage.removeItem('user')
  console.log('🚪 Utilisateur déconnecté')
  await router.push('/login')
}

/**
 * Compatibilité: retourne l'instance api authentifiée partagée.
 */
export const createAuthenticatedApi = (): AxiosInstance => api

export const apiUtils = {
  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('access_token')
  },

  ensureAuth: (): void => {
    if (!localStorage.getItem('access_token')) {
      throw new Error('Utilisateur non authentifié')
    }
  },

  refreshToken: async (): Promise<boolean> => {
    try {
      const { keycloakAuthService } = await import('./keycloak-auth.service')
      const newToken = await keycloakAuthService.refreshToken()
      if (newToken) {
        localStorage.setItem('access_token', newToken)
        return true
      }
      return false
    } catch (error) {
      console.error('❌ Erreur lors du rafraîchissement du token:', error)
      return false
    }
  }
}

export default api
