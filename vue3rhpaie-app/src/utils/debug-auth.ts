/**
 * Script de débogage pour l'authentification
 * À exécuter dans la console du navigateur pour diagnostiquer les problèmes de token
 */

// ============================================
// 1. VÉRIFIER L'ÉTAT DU TOKEN
// ============================================
export const debugToken = () => {
  console.group('🔍 Debug Token Status')
  
  const accessToken = localStorage.getItem('access_token')
  const refreshToken = localStorage.getItem('refresh_token')
  const user = localStorage.getItem('user')
  
  console.log('✅ localStorage Keys:')
  console.log('  - access_token:', accessToken ? `${accessToken.substring(0, 30)}...` : 'MISSING')
  console.log('  - refresh_token:', refreshToken ? `${refreshToken.substring(0, 30)}...` : 'MISSING')
  console.log('  - user:', user ? JSON.parse(user) : 'MISSING')
  
  // Décoder le token
  if (accessToken) {
    try {
      const parts = accessToken.split('.')
      if (parts.length === 3) {
        const payload = JSON.parse(atob(parts[1]))
        console.log('\n📋 JWT Payload:')
        console.log('  - iss:', payload.iss)
        console.log('  - sub:', payload.sub)
        console.log('  - preferred_username:', payload.preferred_username)
        console.log('  - email:', payload.email)
        console.log('  - exp:', new Date(payload.exp * 1000).toLocaleString())
        console.log('  - iat:', new Date(payload.iat * 1000).toLocaleString())
        
        const now = Date.now() / 1000
        if (payload.exp > now) {
          console.log(`  ✅ Token VALIDE (expire dans ${Math.round((payload.exp - now) / 60)} minutes)`)
        } else {
          console.log(`  ❌ Token EXPIRÉ depuis ${Math.round((now - payload.exp) / 60)} minutes`)
        }
        
        console.log('\n🔐 Roles:')
        if (payload.resource_access?.hrm_frontend?.roles) {
          console.log('  - Client Roles:', payload.resource_access.hrm_frontend.roles)
        }
        if (payload.realm_access?.roles) {
          console.log('  - Realm Roles:', payload.realm_access.roles)
        }
      }
    } catch (error) {
      console.error('❌ Error decoding token:', error)
    }
  }
  
  console.groupEnd()
}

// ============================================
// 2. VÉRIFIER LES HEADERS DE REQUÊTE
// ============================================
export const debugRequestHeaders = () => {
  console.group('🔍 Debug Request Headers')
  
  const token = localStorage.getItem('access_token')
  console.log('Authorization header would be:', token ? `Bearer ${token.substring(0, 30)}...` : 'NONE')
  
  // Simuler une requête pour voir les headers
  fetch('/api/personnels/organisation/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    },
    body: JSON.stringify({})
  })
    .then(response => {
      console.log('Response Status:', response.status)
      console.log('Response Headers:')
      response.headers.forEach((value, name) => {
        console.log(`  - ${name}: ${value}`)
      })
      return response.json()
    })
    .catch(error => console.error('Request error:', error))
  
  console.groupEnd()
}

// ============================================
// 3. VÉRIFIER LE STOCKAGE LOCAL
// ============================================
export const debugLocalStorage = () => {
  console.group('🔍 Debug LocalStorage')
  
  console.log('Total items:', localStorage.length)
  console.log('\nAll keys:')
  
  for (let i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i)
    const value = localStorage.getItem(key)
    const valuePreview = value && value.length > 50 ? `${value.substring(0, 50)}...` : value
    console.log(`  - ${key}: ${valuePreview}`)
  }
  
  console.groupEnd()
}

// ============================================
// 4. VÉRIFIER LA CONNEXION À KEYCLOAK
// ============================================
export const debugKeycloakConnection = async () => {
  console.group('🔍 Debug Keycloak Connection')
  
  import { API_CONFIG } from '@/config/api'
  const keycloakUrl = API_CONFIG.KEYCLOAK_URL
  const realm = API_CONFIG.KEYCLOAK.REALM
  
  try {
    console.log(`Vérification de la connexion à ${keycloakUrl}...`)
    
    // Test 1: Vérifier si Keycloak répond
    const configResponse = await fetch(`${keycloakUrl}/realms/${realm}/.well-known/openid-configuration`)
    if (configResponse.ok) {
      const config = await configResponse.json()
      console.log('✅ Keycloak responsive')
      console.log('  - Token Endpoint:', config.token_endpoint)
      console.log('  - Userinfo Endpoint:', config.userinfo_endpoint)
      console.log('  - Issuer:', config.issuer)
    } else {
      console.error('❌ Keycloak not accessible (HTTP ' + configResponse.status + ')')
    }
    
    // Test 2: Vérifier les tokens actuels
    const token = localStorage.getItem('access_token')
    if (token) {
      console.log('\n🔐 Testing current token with userinfo endpoint...')
      const userResponse = await fetch(`${keycloakUrl}/realms/${realm}/protocol/openid-connect/userinfo`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      
      if (userResponse.ok) {
        const userInfo = await userResponse.json()
        console.log('✅ Token VALID')
        console.log('  - Username:', userInfo.preferred_username)
        console.log('  - Email:', userInfo.email)
      } else {
        console.error('❌ Token INVALID (HTTP ' + userResponse.status + ')')
      }
    }
    
  } catch (error) {
    console.error('❌ Connection error:', error)
  }
  
  console.groupEnd()
}

// ============================================
// 5. SIMULER UNE REQUÊTE API
// ============================================
export const debugApiCall = async (endpoint = '/api/personnels/organisation/list') => {
  console.group(`🔍 Debug API Call: ${endpoint}`)
  
  const token = localStorage.getItem('access_token')
  
  console.log('Request details:')
  console.log(`  - URL: ${endpoint}`)
  console.log(`  - Method: POST`)
  console.log(`  - Authorization: ${token ? 'Bearer ***' : 'NONE'}`)
  
  try {
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      body: JSON.stringify({})
    })
    
    console.log(`\nResponse:`)
    console.log(`  - Status: ${response.status} ${response.statusText}`)
    console.log(`  - Headers:`)
    response.headers.forEach((value, name) => {
      console.log(`    - ${name}: ${value}`)
    })
    
    const data = await response.json()
    console.log(`  - Data:`, data)
    
  } catch (error) {
    console.error('Request error:', error)
  }
  
  console.groupEnd()
}

// ============================================
// 6. EXÉCUTER TOUS LES DIAGNOSTICS
// ============================================
export const runAllDebugChecks = async () => {
  console.clear()
  console.log('🚀 RUNNING ALL DEBUG CHECKS...\n')
  
  debugToken()
  console.log('\n')
  
  debugLocalStorage()
  console.log('\n')
  
  await debugKeycloakConnection()
  console.log('\n')
  
  await debugApiCall()
}

// Export pour utilisation globale
if (typeof window !== 'undefined') {
  (window as any).__DEBUG = {
    token: debugToken,
    headers: debugRequestHeaders,
    localStorage: debugLocalStorage,
    keycloak: debugKeycloakConnection,
    api: debugApiCall,
    all: runAllDebugChecks
  }
  
  console.log('🔍 Debug utilities loaded. Type __DEBUG.all() to run all checks')
}
