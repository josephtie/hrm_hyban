export interface User {
  id: number
  username: string
  email: string
  firstName: string
  lastName: string
  matricule?: string
  role: UserRole
  permissions: Permission[]
  createdAt: string
  lastLogin?: string
  isActive: boolean
}

export interface Permission {
  id: string
  name: string
  resource: string
  action: string
  description?: string
}

export type UserRole = 'ADMIN' | 'RH' | 'RH_MANAGER' | 'USER' | 'VIEWER' | 'GUEST'

export interface LoginCredentials {
  username: string
  password: string
  rememberMe?: boolean
}

export interface RegisterData {
  username: string
  email: string
  firstName: string
  lastName: string
  password: string
  confirmPassword: string
  matricule?: string
}

export interface AuthResponse {
  token: string
  accessToken?: string
  refreshToken: string
  user: User
  expiresIn: number
  tokenType?: string
}

export interface TabItem {
  name: string
  title: string
  component?: string
  path?: string
  icon?: string
  closable?: boolean
  params?: Record<string, any>
}

export interface MenuItem {
  id: string
  title: string
  icon?: string
  path?: string
  badge?: string | number
  children?: MenuItem[]
  roles?: UserRole[]
  permissions?: string[]
  external?: boolean
}

export interface ApiResponse<T = any> {
  success: boolean
  data: T
  message?: string
  total?: number // Pour la pagination
  errors?: string[]
  pagination?: {
    page: number
    size: number
    total: number
    totalPages: number
  }
}

export interface ApiError {
  message: string
  code?: string
  details?: any
  timestamp: string
}
