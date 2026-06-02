/**
 * Configuration centralisée des API et services
 * Permet une gestion facile des environnements (dev/test/prod)
 */

// Interface pour la configuration API
export interface ApiConfig {
  // URLs principales
  API_BASE_URL: string
  KEYCLOAK_URL: string
  
  // Ports
  API_PORT: number
  KEYCLOAK_PORT: number
  
  // Endpoints spécifiques
  ENDPOINTS: {
    PAIE: string
    PARAMETRAGES: string
    RH: string
    USERS: string
    KEYCLOAK_TOKEN: string
    KEYCLOAK_REALM: string
  }
  
  // Configuration timeout
  TIMEOUT: number
  
  // Configuration Keycloak
  KEYCLOAK: {
    REALM: string
    CLIENT_ID: string
    GRANT_TYPE: string
    SCOPE: string
  }
}

// Déterminer si on est en développement
const isDevelopment = import.meta.env.MODE === 'development'

// Helper pour obtenir les variables d'environnement avec fallback
const getEnvVar = (key: string, fallback: string): string => {
  return import.meta.env[key as keyof ImportMetaEnv] || fallback
}

// Configuration principale
export const API_CONFIG: ApiConfig = {
  // URLs de base - utilise des URLs relatives en développement (via proxy)
  API_BASE_URL: isDevelopment ? '' : getEnvVar('VITE_API_BASE_URL', 'http://192.168.1.6:7200'),
  KEYCLOAK_URL: isDevelopment ? '' : getEnvVar('VITE_KEYCLOAK_URL', 'http://192.168.1.6:8080'),
  
  // Ports
  API_PORT: parseInt(getEnvVar('VITE_API_PORT', '7200')),
  KEYCLOAK_PORT: parseInt(getEnvVar('VITE_KEYCLOAK_PORT', '8080')),
  
  // Configuration Keycloak
  KEYCLOAK: {
    REALM: getEnvVar('VITE_KEYCLOAK_REALM', 'hyban'),
    CLIENT_ID: getEnvVar('VITE_KEYCLOAK_CLIENT_ID', 'hrm_frontend'),
    GRANT_TYPE: getEnvVar('VITE_KEYCLOAK_GRANT_TYPE', 'password'),
    SCOPE: getEnvVar('VITE_KEYCLOAK_SCOPE', 'openid profile email')
  },
  
  // Timeout et autres configurations
  TIMEOUT: parseInt(getEnvVar('VITE_API_TIMEOUT', '30000')),
  
  // Endpoints spécifiques
  ENDPOINTS: {
    PAIE: '/api/paie',
    PARAMETRAGES: '/api/parametrages',
    RH: '/api/rh',
    USERS: '/api/users',
    KEYCLOAK_TOKEN: '/protocol/openid-connect/token',
    KEYCLOAK_REALM: '/realms'
  }
}

// Helper pour construire l'URL API complète
const buildApiUrl = (base: string, path: string = ''): string => {
  return isDevelopment ? `${base}${path}` : `${API_CONFIG.API_BASE_URL}${base}${path}`
}

// Helper pour construire l'URL Keycloak complète
const buildKeycloakUrl = (path: string): string => {
  return isDevelopment ? `/realms/${API_CONFIG.KEYCLOAK.REALM}${path}` : `${API_CONFIG.KEYCLOAK_URL}/realms/${API_CONFIG.KEYCLOAK.REALM}${path}`
}

// URLs pré-construites pour faciliter l'utilisation
export const API_URLS = {
  // URLs de base - utilise des URLs relatives en développement (via proxy)
  BASE: isDevelopment ? '/api' : `${API_CONFIG.API_BASE_URL}/api`,
  PAIE_BASE: isDevelopment ? '/api/paie' : `${API_CONFIG.API_BASE_URL}/api/paie`,
  RH_BASE: isDevelopment ? '/api/rh' : `${API_CONFIG.API_BASE_URL}/api/rh`,
  PARAMETRAGES_BASE: isDevelopment ? '/api/parametrages' : `${API_CONFIG.API_BASE_URL}/api/parametrages`,
  
  // Services de paie
  ECHEANCE: buildApiUrl(API_CONFIG.ENDPOINTS.PAIE, '/echelonnement'),
  PRETS: buildApiUrl(API_CONFIG.ENDPOINTS.PAIE, '/pretspersonnels'),
  PERIODES: buildApiUrl(API_CONFIG.ENDPOINTS.PARAMETRAGES, '/periodes'),
  PRIMES: buildApiUrl(API_CONFIG.ENDPOINTS.PAIE, '/prime-personnel'),
  
  // Services de paramétrage
  SOCIETES: buildApiUrl(API_CONFIG.ENDPOINTS.PARAMETRAGES, '/societes'),
  RUBRIQUES: buildApiUrl(API_CONFIG.ENDPOINTS.PARAMETRAGES, '/rubriques'),
  BANQUES: buildApiUrl(API_CONFIG.ENDPOINTS.PARAMETRAGES, '/banques'),
  
  // Services RH
  SANCTIONS: buildApiUrl(API_CONFIG.ENDPOINTS.RH, '/carriere/sanctions'),
  TYPE_SANCTIONS: buildApiUrl(API_CONFIG.ENDPOINTS.RH, '/carriere/types-sanctions'),
  
  // Services utilisateurs
  USERS: buildApiUrl(API_CONFIG.ENDPOINTS.USERS),
  
  // Keycloak
  KEYCLOAK_TOKEN: buildKeycloakUrl(API_CONFIG.ENDPOINTS.KEYCLOAK_TOKEN),
  KEYCLOAK_USERINFO: buildKeycloakUrl('/protocol/openid-connect/userinfo'),
  KEYCLOAK_LOGOUT: buildKeycloakUrl('/protocol/openid-connect/logout')
}

// Export par défaut
export default API_CONFIG
