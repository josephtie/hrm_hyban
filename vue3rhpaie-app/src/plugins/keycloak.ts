import { useAuthStore } from '@/stores/auth'
import { nextTick } from 'vue'

// Initialiser l'authentification au démarrage de l'application
export const initializeKeycloak = async () => {
  const authStore = useAuthStore()
  
  try {
    // Attendre que le DOM soit prêt
    await nextTick()
    
    // Initialiser l'authentification depuis localStorage
    await authStore.initializeAuth()
    
    console.log('Authentification initialisée avec succès')
    return true
  } catch (error) {
    console.error('Erreur lors de l\'initialisation de l\'authentification:', error)
    return false
  }
}

export default initializeKeycloak
