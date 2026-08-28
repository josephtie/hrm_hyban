import axios from 'axios'
import type { AxiosResponse } from 'axios'
import type { ApiResponse } from '@/types/auth'
import { API_URLS, API_CONFIG } from '@/config/api'
import router from '@/router'

export interface UserInfo {
  sub?: string
  email: string
  name?: string
  preferred_username?: string
  given_name?: string
  family_name?: string
  roles?: string[]
  username: string
  nom: string
  prenom: string
  role: string
}

export interface LoginResponse {
  success: boolean
  user?: UserInfo
  error?: string
}

// Configuration Keycloak centralisée
const keycloakConfig = {
  tokenUrl: API_URLS.KEYCLOAK_TOKEN,
  clientId: API_CONFIG.KEYCLOAK.CLIENT_ID,
  grantType: API_CONFIG.KEYCLOAK.GRANT_TYPE,
  scope: API_CONFIG.KEYCLOAK.SCOPE
}

export const keycloakAuthService = {
  // Connexion directe avec username/password (Resource Owner Password Credentials)
  async login(username: string, password: string): Promise<LoginResponse> {
    try {
      console.log('Tentative de connexion Keycloak vers:', keycloakConfig.tokenUrl)
      
      const response = await axios.post(
        keycloakConfig.tokenUrl,
        new URLSearchParams({
          client_id: keycloakConfig.clientId,
          grant_type: keycloakConfig.grantType,
          username: username,
          password: password,
          scope: keycloakConfig.scope
        }),
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }
      )

      const tokenData = response.data
      
      if (tokenData.access_token) {
        // Parser le JWT pour extraire les infos utilisateur
        const userInfo = this.parseJWT(tokenData.access_token)
        
        // Stocker les tokens
        localStorage.setItem('access_token', tokenData.access_token)
        localStorage.setItem('refresh_token', tokenData.refresh_token)
        localStorage.setItem('user', JSON.stringify(userInfo))
        
        console.log('Connexion réussie - Utilisateur:', userInfo)
        
        return {
          success: true,
          user: userInfo
        }
      } else {
        return {
          success: false,
          error: 'Token non reçu'
        }
      }
    } catch (error: any) {
      console.error('Erreur connexion Keycloak:', error)
      return {
        success: false,
        error: error.response?.data?.error_description || error.message || 'Erreur de connexion'
      }
    }
  },

  // Parser le JWT pour extraire les infos utilisateur
  parseJWT(token: string): UserInfo {
    try {
      // Décoder le payload du JWT (base64)
      const base64Url = token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      }).join(''))
      
      const payload = JSON.parse(jsonPayload)
      
      console.log('🔍 JWT Payload complet:', payload)
      console.log('🔍 realm_access.roles:', payload.realm_access?.roles)
      console.log('🔍 resource_access.hrm_frontend.roles:', payload.resource_access?.['hrm_frontend']?.roles)
      
      const extractedRole = this.extractRoleFromToken(payload)
      console.log('🔍 Rôle extrait:', extractedRole)
      
      return {
        username: payload.preferred_username || payload.sub || 'Unknown',
        email: payload.email || '',
        nom: payload.family_name || payload.given_name || '',
        prenom: payload.given_name || '',
        role: extractedRole
      }
    } catch (error) {
      console.error('Erreur parsing JWT:', error)
      return {
        username: 'Unknown',
        email: '',
        nom: '',
        prenom: '',
        role: 'GUEST'
      }
    }
  },

  // Normaliser les rôles (aligné avec le backend JwtAuthConverter.ROLE_ALIASES)
  normalizeRole(role: string): string {
    const upper = role ? role.trim().toUpperCase() : ''
    const ALIASES: Record<string, string> = {
      'PAIE': 'DAF',
      'POINTAGE': 'PTGE'
    }
    return ALIASES[upper] || upper
  },

  // Extraire les rôles depuis le token
  extractRoleFromToken(payload: any): string {
    const PRIORITY: string[] = ['ADMIN', 'DAF', 'RH', 'PTGE', 'USER']

    // Priorité aux rôles client
    if (payload.resource_access && payload.resource_access['hrm_frontend']) {
      const roles: string[] = payload.resource_access['hrm_frontend'].roles || []
      const normalized = roles.map(r => this.normalizeRole(r))
      const found = PRIORITY.find(r => normalized.includes(r))
      if (found) return found
    }

    // Sinon, utiliser les rôles realm
    const realmRoles: string[] = payload.realm_access?.roles || []
    const normalizedRealm = realmRoles.map(r => this.normalizeRole(r))
    const found = PRIORITY.find(r => normalizedRealm.includes(r))
    return found || 'GUEST'
  },

  // Déconnexion
  async logout(): Promise<void> {
    try {
      // Optionnel : révoquer le token auprès de Keycloak
      const refreshToken = localStorage.getItem('refresh_token')
      if (refreshToken) {
        await axios.post(
          keycloakConfig.tokenUrl,
          new URLSearchParams({
            client_id: keycloakConfig.clientId,
            grant_type: 'refresh_token',
            refresh_token: refreshToken
          }),
          {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          }
        )
      }
    } catch (error) {
      console.warn('Logout API call failed:', error)
    } finally {
      // Nettoyer les données locales
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      localStorage.removeItem('user')
      
      // Rediriger vers login
      router.push('/login')
    }
  },

  // Obtenir le token actuel
  getToken(): string | null {
    return localStorage.getItem('access_token')
  },

  // Vérifier si l'utilisateur est authentifié
  isAuthenticated(): boolean {
    const token = this.getToken()
    if (!token) return false
    
    try {
      // Vérifier si le token n'est pas expiré
      const payload = JSON.parse(atob(token.split('.')[1]))
      const currentTime = Date.now() / 1000
      return payload.exp > currentTime
    } catch {
      return false
    }
  },

  // Obtenir les infos utilisateur
  getCurrentUser(): UserInfo | null {
    const userStr = localStorage.getItem('user')
    if (userStr) {
      try {
        return JSON.parse(userStr)
      } catch {
        return null
      }
    }
    return null
  },

  // Rafraîchir le token
  async refreshToken(): Promise<string | null> {
    try {
      const refreshToken = localStorage.getItem('refresh_token')
      if (!refreshToken) {
        throw new Error('Pas de refresh token')
      }

      const response = await axios.post(
        keycloakConfig.tokenUrl,
        new URLSearchParams({
          client_id: keycloakConfig.clientId,
          grant_type: 'refresh_token',
          refresh_token: refreshToken
        }),
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }
      )

      const tokenData = response.data
      
      if (tokenData.access_token) {
        localStorage.setItem('access_token', tokenData.access_token)
        if (tokenData.refresh_token) {
          localStorage.setItem('refresh_token', tokenData.refresh_token)
        }
        
        console.log('Token rafraîchi avec succès')
        return tokenData.access_token
      }
      
      return null
    } catch (error) {
      console.error('Erreur refresh token:', error)
      await this.logout()
      return null
    }
  },

  // Vérifier les rôles
  hasRole(role: string): boolean {
    const user = this.getCurrentUser()
    if (!user) return false
    return user.role === role || user.role === 'ADMIN'
  },

  // Vérifier si admin
  isAdmin(): boolean {
    return this.hasRole('ADMIN')
  },

  // Nettoyer les données locales
  clearLocalData(): void {
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    localStorage.removeItem('user')
  }
}

export default keycloakAuthService
