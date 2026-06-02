/**
 * Test direct d'authentification Keycloak
 * Utiliser dans la console: testKeycloakAuth()
 */

import { API_CONFIG } from '@/config/api'

export const testKeycloakAuth = async () => {
  console.group('🔐 Test Authentification Keycloak')
  
  try {
    console.log('📝 Tentative de connexion avec admin/admin123')
    
    const tokenUrl = `${API_CONFIG.KEYCLOAK_URL}/realms/${API_CONFIG.KEYCLOAK.REALM}/protocol/openid-connect/token`
    
    const formData = new URLSearchParams({
      grant_type: API_CONFIG.KEYCLOAK.GRANT_TYPE,
      client_id: API_CONFIG.KEYCLOAK.CLIENT_ID,
      username: 'admin',
      password: 'admin123',
      scope: API_CONFIG.KEYCLOAK.SCOPE
    })
    
    console.log('🌐 URL:', tokenUrl)
    console.log('📦 Données:', Object.fromEntries(formData))
    
    const response = await fetch(tokenUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: formData
    })
    
    console.log('📊 Status:', response.status)
    
    if (response.ok) {
      const data = await response.json()
      console.log('✅ Connexion réussie!')
      console.log('🔑 Access Token:', data.access_token ? data.access_token.substring(0, 50) + '...' : 'Non reçu')
      console.log('🔄 Refresh Token:', data.refresh_token ? 'Présent' : 'Absent')
      console.log('⏱️ Expires In:', data.expires_in)
      console.log('👤 Token Type:', data.token_type)
      
      // Tester l'appel API avec le token
      await testApiWithToken(data.access_token)
      
      return data
    } else {
      const errorText = await response.text()
      console.log('❌ Erreur de connexion:', response.status, response.statusText)
      console.log('📄 Détails erreur:', errorText)
      return null
    }
  } catch (error) {
    console.log('❌ Erreur réseau:', error.message)
    return null
  } finally {
    console.groupEnd()
  }
}

export const testApiWithToken = async (token: string) => {
  console.group('🌐 Test API avec Token')
  
  try {
    const response = await fetch(`${API_CONFIG.API_BASE_URL}/api`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      }
    })
    
    console.log('📊 Status:', response.status)
    
    if (response.ok) {
      console.log('✅ API accessible avec token!')
      const data = await response.text()
      console.log('📄 Réponse:', data.substring(0, 200) + '...')
    } else {
      console.log('❌ API non accessible même avec token:', response.status)
      const errorText = await response.text()
      console.log('📄 Erreur:', errorText)
    }
  } catch (error) {
    console.log('❌ Erreur API:', error.message)
  } finally {
    console.groupEnd()
  }
}

// Export pour utilisation dans la console
if (typeof window !== 'undefined') {
  (window as any).testKeycloakAuth = testKeycloakAuth
  (window as any).testApiWithToken = testApiWithToken
}

export default {
  testKeycloakAuth,
  testApiWithToken
}
