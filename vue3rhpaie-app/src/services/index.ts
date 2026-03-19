// Exportation des services
export { default as api, apiUtils } from './api'
export { authService } from './auth.service'
export { categoriesService, type CategorieDto, type CategorieFilter } from './categories.service'

// Réexportation des types communs
export type { ApiResponse, ApiError } from '@/types/auth'
