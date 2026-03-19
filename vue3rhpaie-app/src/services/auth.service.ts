import axios from 'axios'
import router from '@/router'

export interface UserInfo {
  username: string
  email: string
  nom: string
  prenom: string
  role: string
}

export interface LoginResponse {
  success: boolean
  user?: UserInfo
  error?: string
}

export const keycloakAuthService = {
  // URL de Keycloak via proxy Vite
  keycloakUrl: '/keycloak/realms/hyban/protocol/openid-connect/token',
  clientId: 'hrm_frontend',

  // Connexion directe à Keycloak
  async login(username: string, password: string): Promise<LoginResponse> {
    try {
      console.log('Tentative de connexion Keycloak vers:', this.keycloakUrl)
      
      const response = await axios.post(
        this.keycloakUrl,
        new URLSearchParams({
          client_id: this.clientId,
          grant_type: "password",
          username: username,
          password: password
        }),
        {
          headers: { "Content-Type": "application/x-www-form-urlencoded" }
        }
      )

      console.log('Response Keycloak status:', response.status)
      console.log('Response Keycloak data:', response.data)

      if (response.data.access_token) {
        // Stocker le token
        localStorage.setItem('access_token', response.data.access_token)
        if (response.data.refresh_token) {
          localStorage.setItem('refresh_token', response.data.refresh_token)
        }
        
        // Récupérer les infos utilisateur depuis le token JWT
        const userInfo = this.parseJWT(response.data.access_token)
        console.log('UserInfo parsed:', userInfo)
        localStorage.setItem('user', JSON.stringify(userInfo))
        
        return { success: true, user: userInfo }
      } else {
        throw new Error('Token non trouvé dans la réponse Keycloak')
      }
    } catch (error) {
      console.error('Erreur de connexion Keycloak:', error)
      return { success: false, error: (error as Error).message }
    }
  },

  // Parser le token JWT pour extraire les infos utilisateur
  parseJWT(token: string): UserInfo {
    try {
      const base64Url = token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      }).join(''))
      
      const payload = JSON.parse(jsonPayload)
      return {
        username: payload.preferred_username || payload.sub || 'Unknown',
        email: payload.email || '',
        nom: payload.family_name || payload.given_name || '',
        prenom: payload.given_name || '',
        role: this.extractRoleFromToken(payload)
      }
    } catch (error) {
      console.error('Erreur parsing JWT:', error)
      return {
        username: 'Unknown',
        email: '',
        nom: '',
        prenom: '',
        role: 'USER'
      }
    }
  },

  // Extraire le rôle depuis le token
  extractRoleFromToken(payload: any): string {
    console.log('Payload JWT:', payload)
    
    if (payload.resource_access && payload.resource_access['backend_hrm']) {
      const roles = payload.resource_access['backend_hrm'].roles
      console.log('Roles from resource_access:', roles)
      const role = roles.includes('ADMIN') ? 'ADMIN' : 
                   roles.includes('RH_MANAGER') ? 'RH_MANAGER' :
                   roles.includes('USER') ? 'USER' : 'GUEST'
      console.log('Role determined:', role)
      return role
    }
    
    const realmRoles = payload.realm_access?.roles || []
    console.log('Roles from realm_access:', realmRoles)
    
    // Chercher les rôles métier dans l'ordre de priorité
    const role = realmRoles.includes('ADMIN') ? 'ADMIN' : 
                 realmRoles.includes('RH_MANAGER') ? 'RH_MANAGER' :
                 realmRoles.includes('USER') ? 'USER' : 'GUEST'
    
    console.log('Role determined from realm:', role)
    return role
  },

  // Déconnexion
  logout(): void {
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    localStorage.removeItem('user')
    router.push('/login')
  },

  // Vérifier si l'utilisateur est connecté
  isAuthenticated(): boolean {
    const token = localStorage.getItem('access_token')
    if (!token) return false
    
    // Vérifier si le token n'est pas expiré
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      const now = Date.now() / 1000
      return payload.exp > now
    } catch {
      return false
    }
  },

  // Obtenir le token actuel
  getToken(): string | null {
    return localStorage.getItem('access_token')
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
      if (!refreshToken) return null
      
      const response = await axios.post(
        this.keycloakUrl,
        new URLSearchParams({
          client_id: this.clientId,
          grant_type: "refresh_token",
          refresh_token: refreshToken
        }),
        {
          headers: { "Content-Type": "application/x-www-form-urlencoded" }
        }
      )
      
      if (response.data.access_token) {
        localStorage.setItem('access_token', response.data.access_token)
        if (response.data.refresh_token) {
          localStorage.setItem('refresh_token', response.data.refresh_token)
        }
        return response.data.access_token
      }
      
      return null
    } catch (error) {
      console.error('Erreur refresh token:', error)
      this.logout()
      return null
    }
  },

  // Vérifier le rôle de l'utilisateur
  hasRole(role: string): boolean {
    const user = this.getCurrentUser()
    return user?.role === role
  },

  // Vérifier si l'utilisateur est admin
  isAdmin(): boolean {
    return this.hasRole('ADMIN')
  }
}

export default keycloakAuthService
