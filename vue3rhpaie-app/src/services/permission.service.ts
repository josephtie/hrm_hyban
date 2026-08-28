import { api } from '@/services/api'

export interface Permission {
  id: number
  code: string
  description: string
  active: boolean
}

export interface RolePermissions {
  role: string
  permissions: string[]
}

export interface RoleWithPermissions {
  id: number
  name: string
  permissions: string[]
}

class PermissionService {
  private baseUrl = '/permissions'
  private cachedPermissions: string[] | null = null

  async getAllPermissions(): Promise<Permission[]> {
    const response = await api.get(`${this.baseUrl}`)
    return response.data
  }

  async getRolesWithPermissions(): Promise<RoleWithPermissions[]> {
    const response = await api.get(`${this.baseUrl}/roles`)
    return response.data
  }

  async getPermissionsByRole(roleName: string): Promise<RolePermissions> {
    const response = await api.get(`${this.baseUrl}/role/${roleName}`)
    return response.data
  }

  async updateRolePermissions(roleName: string, permissions: string[]): Promise<RolePermissions> {
    const response = await api.put(`${this.baseUrl}/role/${roleName}`, { permissions })
    return response.data
  }

  setCachedPermissions(permissions: string[]) {
    this.cachedPermissions = permissions
  }

  getCachedPermissions(): string[] | null {
    return this.cachedPermissions
  }

  hasPermission(code: string): boolean {
    if (!this.cachedPermissions) return false
    return this.cachedPermissions.includes(code)
  }

  hasAnyPermission(codes: string[]): boolean {
    if (!this.cachedPermissions) return false
    return codes.some(code => this.cachedPermissions!.includes(code))
  }

  hasAllPermissions(codes: string[]): boolean {
    if (!this.cachedPermissions) return false
    return codes.every(code => this.cachedPermissions!.includes(code))
  }
}

export const permissionService = new PermissionService()
export default permissionService
