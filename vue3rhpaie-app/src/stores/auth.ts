import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import keycloakAuthService from '@/services/keycloak-auth.service'
import type { 
  User, 
  UserRole, 
  LoginCredentials, 
  RegisterData, 
  AuthResponse,
  Permission 
} from '@/types/auth'

export const useAuthStore = defineStore('auth', () => {
  // État
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const refreshToken = ref<string | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)
  const isInitialized = ref(false)

  // Getters computed
  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const userRole = computed(() => user.value?.role || 'GUEST')
  const permissions = computed(() => user.value?.permissions || [])
  const userName = computed(() => user.value ? `${user.value.firstName} ${user.value.lastName}` : '')
  const userInitials = computed(() => {
    if (!user.value) return 'G'
    return `${user.value.firstName[0]}${user.value.lastName[0]}`.toUpperCase()
  })

  // Vérification des rôles
  const hasRole = (role: UserRole): boolean => {
    if (!user.value) return false
    return user.value.role === role
  }

  const hasAnyRole = (roles: UserRole[]): boolean => {
    if (!user.value) return false
    return roles.includes(user.value.role)
  }

  const hasMinimumRole = (minimumRole: UserRole): boolean => {
    if (!user.value) return false
    
    const roleHierarchy: Record<UserRole, number> = {
      'GUEST': 0,
      'VIEWER': 1,
      'USER': 2,
      'RH': 3,
      'RH_MANAGER': 4,
      'ADMIN': 5
    }
    
    return roleHierarchy[user.value.role] >= roleHierarchy[minimumRole]
  }

  // Vérification des permissions
  const hasPermission = (resource: string, action: string): boolean => {
    if (!user.value) return false
    
    return permissions.value.some(
      perm => perm.resource === resource && perm.action === action
    )
  }

  const hasAnyPermission = (perms: Array<{resource: string, action: string}>): boolean => {
    if (!user.value) return false
    
    return perms.some(perm => hasPermission(perm.resource, perm.action))
  }

  const hasAllPermissions = (perms: Array<{resource: string, action: string}>): boolean => {
    if (!user.value) return false
    
    return perms.every(perm => hasPermission(perm.resource, perm.action))
  }

  // Vérification des rôles (pour Keycloak)
  const hasRoleKeycloak = (role: string): boolean => {
    if (!user.value) return false
    return user.value.role === role || user.value.role === 'ADMIN'
  }

  const isAdmin = (): boolean => {
    return hasRoleKeycloak('ADMIN')
  }

  const hasAnyRoleKeycloak = (roles: string[]): boolean => {
    if (!user.value) return false
    return roles.includes(user.value.role) || user.value.role === 'ADMIN'
  }

  // Permission simplifiée basée sur les rôles Keycloak
  const hasPermissionByRole = (resource: string, action: string): boolean => {
    if (!user.value) return false
    
    const role = user.value.role
    
    // ADMIN a tous les droits
    if (role === 'ADMIN') return true
    
    // Permissions spécifiques par rôle
    const rolePermissions: Record<string, string[]> = {
      'ADMIN': ['*'], // ADMIN a tous les droits
      'RH_MANAGER': [
        'personnel:read', 'temps_absences:read', 'bulletins:read', 'etats:read'
      ],
      'USER': [
        'personnel:read', 'bulletins:read'
      ]
    }
    
    const requiredPermission = `${resource}:${action}`
    return rolePermissions[role]?.includes(requiredPermission) || false
  }

  // Actions d'authentification
  const login = async (credentials: LoginCredentials): Promise<boolean> => {
    try {
      loading.value = true
      error.value = null
      
      // Connexion directe avec username/password
      const response = await keycloakAuthService.login(credentials.username, credentials.password)
      
      console.log('Réponse auth Keycloak:', response) // Debug
      
      if (response.success && response.user) {
        // Mettre à jour le store avec les infos de Keycloak
        token.value = keycloakAuthService.getToken()
        refreshToken.value = localStorage.getItem('refresh_token')
        user.value = response.user as any // Cast temporaire pour compatibilité
        
        console.log('Utilisateur connecté:', user.value) // Debug
        
        ElMessage.success(`Bienvenue ${response.user.username} !`)
        console.log('Login réussi, retour true') // Debug
        return true
      } else {
        error.value = response.error || 'Erreur de connexion'
        ElMessage.error(error.value || 'Erreur de connexion')
        return false
      }
      
    } catch (err: any) {
      error.value = err.message || 'Erreur de connexion'
      ElMessage.error(error.value || 'Erreur de connexion')
      return false
    } finally {
      loading.value = false
    }
  }

  const logout = async (): Promise<void> => {
    try {
      // Appel keycloakAuthService pour la déconnexion
      await keycloakAuthService.logout()
    } catch (err) {
      console.warn('Logout API call failed:', err)
    } finally {
      // Nettoyage local du store
      user.value = null
      token.value = null
      refreshToken.value = null
      error.value = null
    }
  }

  // Initialisation Keycloak
  const initializeAuth = async (): Promise<void> => {
    try {
      // Initialiser depuis localStorage
      const storedToken = localStorage.getItem('access_token')
      const storedUser = localStorage.getItem('user')
      
      if (storedToken && storedUser) {
        token.value = storedToken
        refreshToken.value = localStorage.getItem('refresh_token')
        user.value = JSON.parse(storedUser)
        
        // Vérifier si le token est encore valide
        if (keycloakAuthService.isAuthenticated()) {
          console.log('Auth initialisée avec succès')
        } else {
          console.log('Token expiré, déconnexion')
          await logout()
        }
      } else {
        console.log('Utilisateur non authentifié')
      }
      
      isInitialized.value = true
    } catch (error) {
      console.error('Erreur initialisation auth:', error)
      isInitialized.value = true
    }
  }

  return {
    // État
    user,
    token,
    refreshToken,
    loading,
    error,
    isInitialized,
    
    // Getters
    isAuthenticated,
    userRole,
    permissions,
    userName,
    userInitials,
    
    // Méthodes de rôle
    hasRole,
    hasAnyRole,
    hasMinimumRole,
    
    // Méthodes de rôle Keycloak
    hasRoleKeycloak,
    hasAnyRoleKeycloak,
    isAdmin,
    
    // Méthodes de permission
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    hasPermissionByRole,
    
    // Actions
    login,
    logout,
    initializeAuth
  }
})
