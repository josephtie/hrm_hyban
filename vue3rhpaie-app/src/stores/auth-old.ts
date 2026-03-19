import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { api } from '@/services/api'
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
      'RH_MANAGER': 3,
      'ADMIN': 4
    }
    
    return roleHierarchy[user.value.role] >= roleHierarchy[minimumRole]
  }

  // Vérification des permissions
  const hasPermission = (resource: string, action: string): boolean => {
    if (!user.value) return false
    
    return permissions.value.some(
      permission => permission.resource === resource && permission.action === action
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

  // Actions d'authentification
  const login = async (credentials: LoginCredentials): Promise<boolean> => {
    try {
      loading.value = true
      error.value = null
      
      const response = await api.post<AuthResponse>('/auth/login', credentials)
      const authData = response.data.data
      
      // Stockage des tokens
      token.value = authData.token
      refreshToken.value = authData.refreshToken
      
      // Stockage dans localStorage
      localStorage.setItem('access_token', authData.token)
      localStorage.setItem('refresh_token', authData.refreshToken)
      localStorage.setItem('user', JSON.stringify(authData.user))
      
      // Mise à jour de l'utilisateur
      user.value = authData.user
      
      ElMessage.success(`Bienvenue ${userName.value} !`)
      return true
      
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erreur de connexion'
      ElMessage.error(error.value)
      return false
    } finally {
      loading.value = false
    }
  }

  const logout = async (): Promise<void> => {
    try {
      // Appel API pour invalider le token (optionnel)
      if (token.value) {
        await api.post('/auth/logout')
      }
    } catch (err) {
      console.warn('Logout API call failed:', err)
    } finally {
      // Nettoyage local
      token.value = null
      refreshToken.value = null
      user.value = null
      error.value = null
      
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      localStorage.removeItem('user')
      
      ElMessage.success('Vous avez été déconnecté')
    }
  }

  // Initialisation depuis localStorage
  const initializeAuth = (): void => {
    const storedToken = localStorage.getItem('access_token')
    const storedRefreshToken = localStorage.getItem('refresh_token')
    const storedUser = localStorage.getItem('user')
    
    if (storedToken && storedUser) {
      try {
        token.value = storedToken
        refreshToken.value = storedRefreshToken
        user.value = JSON.parse(storedUser)
      } catch (err) {
        console.error('Failed to parse stored user data:', err)
        logout()
      }
    }
  }

  return {
    // État
    user,
    token,
    refreshToken,
    loading,
    error,
    
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
    
    // Méthodes de permission
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    
    // Actions
    login,
    logout,
    initializeAuth
  }
})
  
  const hasPermission = computed(() => (permission: PermissionEnum) => {
    return userPermissions.value.includes(permission)
  })
  
  const hasAnyPermission = computed(() => (permissions: PermissionEnum[]) => {
    return permissions.some(permission => userPermissions.value.includes(permission))
  })
  
  const hasRole = computed(() => (role: UserRole) => {
    return userRole.value === role
  })

  // Actions
  const login = async (credentials: LoginCredentials): Promise<boolean> => {
    isLoading.value = true
    error.value = null
    
    try {
      // Simulation d'un appel API
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Mock validation
      if (credentials.username === 'admin' && credentials.password === 'admin123') {
        user.value = mockUser
        token.value = 'mock-jwt-token'
        localStorage.setItem('token', token.value)
        localStorage.setItem('user', JSON.stringify(user.value))
        return true
      } else {
        error.value = 'Identifiants incorrects'
        return false
      }
    } catch (err) {
      error.value = 'Erreur lors de la connexion'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const checkAuth = () => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')
    
    // Temporairement forcer le logout pour tester
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    return
    
    if (storedToken && storedUser) {
      try {
        token.value = storedToken
        user.value = JSON.parse(storedUser)
      } catch {
        logout()
      }
    }
  }

  const refreshToken = async (): Promise<boolean> => {
    // Implémentation du refresh token
    return true
  }

  return {
    // State
    user,
    token,
    isLoading,
    error,
    
    // Getters
    isAuthenticated,
    userRole,
    userPermissions,
    hasPermission,
    hasAnyPermission,
    hasRole,
    
    // Actions
    login,
    logout,
    checkAuth,
    refreshToken
  }
})
