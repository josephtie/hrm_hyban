import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface basée sur UserWithRolesDto du backend
export interface UserWithRolesDto {
  userdto: UserRepresentationDTO
  roles: string[]
}

export interface UserRepresentationDTO {
  id: string
  username: string
  email: string
  enabled: boolean
  lastLogin?: string
}

// Interface exacte basée sur CreateUserRequest du backend
export interface CreateUserRequest {
  username: string
  email: string
  firstName: string
  lastName: string
  password?: string
  roles: string[]
}

export interface UtilisateurRestDto {
  id?: string
  username: string
  email: string
  firstName: string
  lastName: string
  password?: string
  roles?: string[]
  enabled?: boolean
  accountNonExpired?: boolean
  credentialsNonExpired?: boolean
  accountNonLocked?: boolean
  authorities?: { authority: string }[]
  lastLogin?: string
}

export interface UtilisateurRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'username' | 'email' | 'lastLogin'
  sortOrder?: 'asc' | 'desc'
}

class UtilisateurRestService {
  async getUtilisateurs(filter?: UtilisateurRestFilter): Promise<ApiResponse<UtilisateurRestDto[]>> {
    try {
      // Si pas de recherche, utiliser /allUsers (plus rapide)
      if (!filter?.search || filter.search.trim() === '') {
        const response = await api.get('/users/allUsers')
        const users: UserWithRolesDto[] = response.data || []
        
        // Transformer UserWithRolesDto en UtilisateurRestDto
        const transformedUsers = users.map((userWithRoles: UserWithRolesDto) => ({
          id: userWithRoles.userdto.id,
          username: userWithRoles.userdto.username,
          email: userWithRoles.userdto.email,
          firstName: '', // À extraire si disponible
          lastName: '',  // À extraire si disponible
          roles: userWithRoles.roles,
          enabled: userWithRoles.userdto.enabled,
          lastLogin: userWithRoles.userdto.lastLogin
        }))
        
        return {
          success: true,
          data: transformedUsers,
          total: transformedUsers.length,
          message: 'Utilisateurs retrieved successfully'
        }
      }
      
      // Si recherche, utiliser /search/allUsers avec timeout augmenté
      const params = new URLSearchParams({
        limit: String(filter?.size ?? 10),
        offset: String(filter?.page ?? 0),
        search: filter?.search || ''
      })
      
      const response = await api.get(`/users/search/allUsers?${params}`, {
        timeout: 60000 // Timeout de 60 secondes
      })
      const users: UserWithRolesDto[] = response.data || []
      
      // Transformer UserWithRolesDto en UtilisateurRestDto
      const transformedUsers = users.map((userWithRoles: UserWithRolesDto) => ({
        id: userWithRoles.userdto.id,
        username: userWithRoles.userdto.username,
        email: userWithRoles.userdto.email,
        firstName: '', // À extraire si disponible
        lastName: '',  // À extraire si disponible
        roles: userWithRoles.roles,
        enabled: userWithRoles.userdto.enabled,
        lastLogin: userWithRoles.userdto.lastLogin
      }))
      
      return {
        success: true,
        data: transformedUsers,
        total: transformedUsers.length,
        message: 'Utilisateurs retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching utilisateurs:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch utilisateurs'
      }
    }
  }

  async getAll(filter?: UtilisateurRestFilter): Promise<ApiResponse<UtilisateurRestDto[]>> {
    return this.getUtilisateurs(filter)
  }

  async getUtilisateurById(id: string): Promise<ApiResponse<UtilisateurRestDto>> {
    try {
      const response = await api.get(`/users/${id}`)
      const data = response.data
      
      return {
        success: true,
        data: data as UtilisateurRestDto,
        message: 'Utilisateur retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching utilisateur:', error)
      return {
        success: false,
        data: {} as UtilisateurRestDto,
        message: 'Failed to fetch utilisateur'
      }
    }
  }

  async createUtilisateur(utilisateur: UtilisateurRestDto): Promise<ApiResponse<UtilisateurRestDto>> {
    try {
      const response = await api.post('/users', {
        username: utilisateur.username,
        email: utilisateur.email,
        firstName: utilisateur.firstName,
        lastName: utilisateur.lastName,
        password: utilisateur.password,
        roles: utilisateur.roles || []
      })
      const data = response.data
      
      return {
        success: true,
        data: data as UtilisateurRestDto,
        message: 'Utilisateur created successfully'
      }
    } catch (error) {
      console.error('Error creating utilisateur:', error)
      return {
        success: false,
        data: {} as UtilisateurRestDto,
        message: 'Failed to create utilisateur'
      }
    }
  }

  async updateUtilisateur(id: string, utilisateur: UtilisateurRestDto): Promise<ApiResponse<UtilisateurRestDto>> {
    try {
      const response = await api.put(`/users/${id}`, {
        username: utilisateur.username,
        email: utilisateur.email,
        firstName: utilisateur.firstName,
        lastName: utilisateur.lastName,
        password: utilisateur.password,
        roles: utilisateur.roles || []
      })
      const data = response.data
      
      return {
        success: true,
        data: data as UtilisateurRestDto,
        message: 'Utilisateur updated successfully'
      }
    } catch (error) {
      console.error('Error updating utilisateur:', error)
      return {
        success: false,
        data: {} as UtilisateurRestDto,
        message: 'Failed to update utilisateur'
      }
    }
  }

  async deleteUtilisateur(id: string): Promise<ApiResponse<boolean>> {
    try {
      await api.delete(`/users/${id}`)
      
      return {
        success: true,
        data: true,
        message: 'Utilisateur deleted successfully'
      }
    } catch (error) {
      console.error('Error deleting utilisateur:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete utilisateur'
      }
    }
  }

  async toggleUserStatus(id: string, active: boolean): Promise<ApiResponse<UtilisateurRestDto>> {
    try {
      const response = await api.put(`/users/${id}/status`, null, {
        params: { active }
      })
      const data = response.data
      
      return {
        success: true,
        data: data as UtilisateurRestDto,
        message: `Utilisateur ${active ? 'activé' : 'désactivé'} successfully`
      }
    } catch (error) {
      console.error('Error toggling user status:', error)
      return {
        success: false,
        data: {} as UtilisateurRestDto,
        message: 'Failed to toggle user status'
      }
    }
  }

  async resetPassword(id: string, newPassword: string): Promise<ApiResponse<boolean>> {
    try {
      await api.put(`/users/${id}/reset-password`, { newPassword })
      
      return {
        success: true,
        data: true,
        message: 'Password reset successfully'
      }
    } catch (error) {
      console.error('Error resetting password:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to reset password'
      }
    }
  }

  // Alias pour compatibilité
  async create(utilisateur: UtilisateurRestDto): Promise<ApiResponse<UtilisateurRestDto>> {
    return this.createUtilisateur(utilisateur)
  }

  async update(utilisateur: UtilisateurRestDto): Promise<ApiResponse<UtilisateurRestDto>> {
    if (!utilisateur.id) {
      throw new Error('ID is required for update operation')
    }
    return this.updateUtilisateur(utilisateur.id, utilisateur)
  }

  async delete(id: string): Promise<ApiResponse<boolean>> {
    return this.deleteUtilisateur(id)
  }

  async getById(id: string): Promise<ApiResponse<UtilisateurRestDto>> {
    return this.getUtilisateurById(id)
  }
}

export const utilisateurrestService = new UtilisateurRestService()
export default utilisateurrestService
