/**
 * Utilitaire de diagnostic pour vérifier la configuration
 * Utiliser dans la console du navigateur: debugConfig()
 */

import { API_CONFIG, API_URLS } from '@/config/api'

export const debugConfig = () => {
  console.group('🔍 Debug Configuration')
  
  // 1. Variables d'environnement
  console.log('📦 Variables d\'environnement:')
  console.log('  - VITE_API_BASE_URL:', import.meta.env.VITE_API_BASE_URL)
  console.log('  - VITE_KEYCLOAK_URL:', import.meta.env.VITE_KEYCLOAK_URL)
  console.log('  - VITE_API_PORT:', import.meta.env.VITE_API_PORT)
  console.log('  - VITE_KEYCLOAK_PORT:', import.meta.env.VITE_KEYCLOAK_PORT)
  console.log('  - VITE_KEYCLOAK_REALM:', import.meta.env.VITE_KEYCLOAK_REALM)
  console.log('  - VITE_KEYCLOAK_CLIENT_ID:', import.meta.env.VITE_KEYCLOAK_CLIENT_ID)
  console.log('  - NODE_ENV:', import.meta.env.NODE_ENV)
  console.log('  - MODE:', import.meta.env.MODE)
  
  // 2. Configuration centralisée
  console.log('\n⚙️ Configuration API_CONFIG:')
  console.log(API_CONFIG)
  
  // 3. URLs pré-construites
  console.log('\n🔗 URLs pré-construites:')
  console.log(API_URLS)
  
  // 4. Test de connexion API
  console.log('\n🌐 Test de connexion API:')
  testApiConnection()
  
  // 5. Test de connexion Keycloak
  console.log('\n🔐 Test de connexion Keycloak:')
  testKeycloakConnection()
  
  console.groupEnd()
}

export const testApiConnection = async () => {
  try {
    console.log(`  Test: ${API_CONFIG.API_BASE_URL}`)
    const response = await fetch(`${API_CONFIG.API_BASE_URL}/api`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    })
    
    if (response.ok) {
      console.log('  ✅ API accessible')
      console.log(`  Status: ${response.status}`)
    } else {
      console.log(`  ❌ API non accessible (${response.status})`)
    }
  } catch (error) {
    console.log('  ❌ Erreur de connexion API:', error.message)
  }
}

export const testKeycloakConnection = async () => {
  try {
    const keycloakUrl = `${API_CONFIG.KEYCLOAK_URL}/realms/${API_CONFIG.KEYCLOAK.REALM}/.well-known/openid-configuration`
    console.log(`  Test: ${keycloakUrl}`)
    
    const response = await fetch(keycloakUrl)
    
    if (response.ok) {
      console.log('  ✅ Keycloak accessible')
      console.log(`  Status: ${response.status}`)
      const config = await response.json()
      console.log('  Token Endpoint:', config.token_endpoint)
    } else {
      console.log(`  ❌ Keycloak non accessible (${response.status})`)
    }
  } catch (error) {
    console.log('  ❌ Erreur de connexion Keycloak:', error.message)
  }
}

export const debugLocalStorage = () => {
  console.group('🔍 Debug LocalStorage')
  
  const token = localStorage.getItem('access_token')
  const refreshToken = localStorage.getItem('refresh_token')
  const user = localStorage.getItem('user')
  
  console.log('🔑 Tokens:')
  console.log('  - access_token:', token ? '✅ Présent' : '❌ Absent')
  console.log('  - refresh_token:', refreshToken ? '✅ Présent' : '❌ Absent')
  console.log('  - user:', user ? '✅ Présent' : '❌ Absent')
  
  if (token) {
    console.log('  - Token length:', token.length)
    console.log('  - Token preview:', token.substring(0, 50) + '...')
  }
  
  console.groupEnd()
}

// Export pour utilisation dans la console
if (typeof window !== 'undefined') {
  (window as any).debugConfig = debugConfig
  (window as any).debugLocalStorage = debugLocalStorage
  (window as any).testApiConnection = testApiConnection
  (window as any).testKeycloakConnection = testKeycloakConnection
}

export default {
  debugConfig,
  debugLocalStorage,
  testApiConnection,
  testKeycloakConnection
}
