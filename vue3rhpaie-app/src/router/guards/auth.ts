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

  // ADMIN a accès à toutes les routes
  if (authStore.isAdmin()) {
    next()
    return
  }

  // Parcourir toute la hiérarchie de routes (parent + enfants)
  for (const record of to.matched) {
    if (record.meta.roles && Array.isArray(record.meta.roles)) {
      const requiredRoles = record.meta.roles as string[]
      if (!authStore.hasAnyRole(requiredRoles as any[])) {
        next({ name: 'access-denied' })
        return
      }
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
  
  // Vérifier les permission codes (nouveau système RBAC)
  if (to.meta.permissionCodes && Array.isArray(to.meta.permissionCodes)) {
    const requiredCodes = to.meta.permissionCodes as string[]
    
    if (!authStore.hasAnyPermissionCode(requiredCodes)) {
      next({ name: 'access-denied' })
      return
    }
  }
  
  // Vérifier l'ancien système de permissions (resource/action) — conservé pour compatibilité
  if (to.meta.permissions && Array.isArray(to.meta.permissions)) {
    const requiredPermissions = to.meta.permissions as Array<{resource: string, action: string}>
    
    if (!authStore.hasAllPermissions(requiredPermissions)) {
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
