import { useAuthStore } from '@/stores/auth'
import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router'

/**
 * Guard d'authentification - Protège les routes nécessitant une connexion
 */
export const authGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()
  
  console.log('Guard auth - Route:', to.path, 'Authentifié:', authStore.isAuthenticated, 'Utilisateur:', authStore.user)
  
  // Si l'utilisateur n'est pas connecté et la route nécessite une authentification
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    console.log('Redirection vers login depuis:', to.path)
    // Rediriger vers la page de login avec l'URL de retour
    next({
      name: 'login',
      query: { redirect: to.fullPath }
    })
    return
  }
  
  // Si l'utilisateur est connecté et essaie d'accéder à la page de login
  if (to.name === 'login' && authStore.isAuthenticated) {
    console.log('Utilisateur déjà connecté, redirection vers dashboard')
    next({ name: 'dashboard' })
    return
  }
  
  console.log('Navigation autorisée vers:', to.path)
  next()
}

/**
 * Guard de rôles - Protège les routes nécessitant des rôles spécifiques
 */
export const roleGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()
  
  // Vérifier si des rôles sont requis pour cette route
  if (to.meta.roles && Array.isArray(to.meta.roles)) {
    const requiredRoles = to.meta.roles as string[]
    
    // Vérifier si l'utilisateur a au moins un des rôles requis
    if (!authStore.hasAnyRole(requiredRoles as any[])) {
      // Rediriger vers une page d'accès refusé ou le dashboard
      next({ name: 'access-denied' })
      return
    }
  }
  
  next()
}

/**
 * Guard de permissions - Protège les routes nécessitant des permissions spécifiques
 */
export const permissionGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()
  
  // Vérifier si des permissions sont requises pour cette route
  if (to.meta.permissions && Array.isArray(to.meta.permissions)) {
    const requiredPermissions = to.meta.permissions as Array<{resource: string, action: string}>
    
    // Pour l'ADMIN, donner accès à tout
    if (authStore.isAdmin()) {
      next()
      return
    }
    
    // Vérifier si l'utilisateur a toutes les permissions requises
    if (!authStore.hasAllPermissions(requiredPermissions)) {
      // Rediriger vers une page d'accès refusé
      next({ name: 'access-denied' })
      return
    }
  }
  
  next()
}

/**
 * Guard combiné - Auth + Rôle + Permission
 */
export const combinedGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  // D'abord vérifier l'authentification
  const authResult = authGuard(to, from, next)
  if (authResult !== undefined) {
    next(authResult)
    return
  }
  
  // Ensuite vérifier les rôles
  const roleResult = roleGuard(to, from, next)
  if (roleResult !== undefined) {
    next(roleResult)
    return
  }
  
  // Enfin vérifier les permissions
  permissionGuard(to, from, next)
}

/**
 * Guard pour les routes publiques (accessibles sans connexion)
 */
export const publicGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()
  
  // Si l'utilisateur est déjà connecté et essaie d'accéder à une route publique
  if (to.meta.public && authStore.isAuthenticated) {
    // Rediriger vers le dashboard
    next({ name: 'dashboard' })
    return
  }
  
  next()
}
