import { api } from '@/services/api'
import type { ApiResponse as AuthApiResponse } from '@/types/auth'

// ==================== INTERFACES ====================
export interface ApiResponse<T> {
  success: boolean
  data: T
  message: string
}
export interface Banque {
  id: number
  code: string
  libelle: string
  actif?: boolean
}

export interface Fonction {
  id: number
  code?: string
  libelle: string
  actif?: boolean
}

export interface TypeContrat {
  id: number
  code?: string
  libelle: string
  actif?: boolean
}

export interface Categorie {
  id: number
  code?: string
  libelle: string
  actif?: boolean
}

export interface Nationnalite {
  id: number
  code?: string
  libelle: string
  actif?: boolean
}

export interface TypeService {
  id: number
  libelle: string
  actif?: boolean
}

export interface Service {
  id: number
  libelle: string
  typeServiceId?: number
  serviceParentId?: number
  actif?: boolean
}

export interface ApiResponse<T> {
  success: boolean
  data: T
  message: string
}

// ==================== SERVICES ====================
export class ReferencesService {
  
  // ==================== BANQUES ====================
  
  async getAllBanques(): Promise<ApiResponse<Banque[]>> {
  try {
    const response = await api.post('/parametrages/banques/lister')
    const data = response.data
    
    return {
      success: true,
      data: data.rows || [],
      message: 'Banques récupérées avec succès'
    }
  } catch (error) {
    console.error('Error fetching banques:', error)
    return {
      success: false,
      data: [],
      message: 'Failed to fetch banques'
    }
  }
}

  getBanqueIdByCode(banques: Banque[], code: string): number {
    const found = banques.find(b => b.libelle === code || b.code === code)
    if (found) {
      console.log('Banque trouvée:', found)
      return found.id
    }
    console.log('Banque non trouvée pour code:', code, 'parmi:', banques)
    return 1 // Valeur par défaut
  }

  // ==================== FONCTIONS/EMPLOIS ====================
  
  async getAllFonctions(): Promise<ApiResponse<Fonction[]>> {
    try {
      const response = await api.get('/personnels/fonctions/listfonction')
      const data = response.data
      
      return {
        success: true,
        data: data || [],
        message: 'Fonctions récupérées avec succès'
      }
    } catch (error) {
      console.error('Error fetching fonctions:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch fonctions'
      }
    }
  }

  getFonctionIdByCode(fonctions: Fonction[], code: string): number {
    const found = fonctions.find(f => f.libelle === code || f.code === code)
    if (found) {
      console.log('Fonction trouvée:', found)
      return found.id
    }
    console.log('Fonction non trouvée pour code:', code, 'parmi:', fonctions)
    return 1 // Valeur par défaut
  }

  // ==================== TYPES DE CONTRATS ====================
  
  async getAllTypeContrats(): Promise<ApiResponse<TypeContrat[]>> {
    try {
      const response = await api.get('/parametrages/types-contrats/list/all')
      const data = response.data
      
      return {
        success: true,
        data: data || [],
        message: 'Types de contrats récupérés avec succès'
      }
    } catch (error) {
      console.error('Error fetching type contrats:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch type contrats'
      }
    }
  }

  getTypeContratIdByCode(typeContrats: TypeContrat[], code: string): number {
    const found = typeContrats.find(tc => tc.libelle === code || tc.code === code)
    if (found) {
      console.log('Type contrat trouvé:', found)
      return found.id
    }
    console.log('Type contrat non trouvé pour code:', code, 'parmi:', typeContrats)
    return 1 // Valeur par défaut
  }

  // ==================== CATÉGORIES ====================
  
  async getAllCategories(): Promise<ApiResponse<Categorie[]>> {
    try {
      const response = await api.get('/categories/listcategorie')
      const data = response.data
      
      return {
        success: true,
        data: data || [],
        message: 'Catégories récupérées avec succès'
      }
    } catch (error) {
      console.error('Error fetching categories:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch categories'
      }
    }
  }

  getCategorieIdByCode(categories: Categorie[], code: string): number {
    const found = categories.find(c => c.libelle === code || c.code === code)
    if (found) {
      console.log('Catégorie trouvée:', found)
      return found.id
    }
    console.log('Catégorie non trouvée pour code:', code, 'parmi:', categories)
    return 1 // Valeur par défaut
  }

  // ==================== NATIONALITÉS ====================
  
  async getAllNationalites(): Promise<ApiResponse<Nationnalite[]>> {
    try {
      const response = await api.get('/nationalites')
      const data = response.data
      
      return {
        success: true,
        data: data.data || [],
        message: 'Nationalités récupérées avec succès'
      }
    } catch (error) {
      console.error('Error fetching nationalities:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch nationalities'
      }
    }
  }

  getNationaliteIdByCode(nationalites: Nationnalite[], code: string): number {
    const found = nationalites.find(n => n.libelle === code || n.code === code)
    if (found) {
      console.log('Nationalité trouvée:', found)
      return found.id
    }
    console.log('Nationalité non trouvée pour code:', code, 'parmi:', nationalites)
    return 1 // Valeur par défaut
  }

  // ==================== TYPES DE SERVICES ====================
  
  async getAllTypeServices(): Promise<ApiResponse<TypeService[]>> {
    try {
      const response = await api.get('/parametrages/types-services/list/all')
      const data = response.data
      
      return {
        success: true,
        data: data || [],
        message: 'Types de services récupérés avec succès'
      }
    } catch (error) {
      console.error('Error fetching type services:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch type services'
      }
    }
  }

  getTypeServiceIdByCode(typeServices: TypeService[], code: string): number {
    const found = typeServices.find(ts => ts.libelle === code || ts.id.toString() === code)
    if (found) {
      console.log('Type service trouvé:', found)
      return found.id
    }
    console.log('Type service non trouvé pour code:', code, 'parmi:', typeServices)
    return 1 // Valeur par défaut
  }

  // ==================== SERVICES ====================
  
  async getServicesByType(typeServiceId: number): Promise<ApiResponse<Service[]>> {
  try {
    const response = await api.post('/personnels/listservicepartype', { id: typeServiceId })
    const data = response.data
    
    return {
      success: true,
      data: data || [],
      message: 'Services récupérés avec succès'
    }
  } catch (error) {
    console.error('Error fetching services:', error)
    return {
      success: false,
      data: [],
      message: 'Failed to fetch services'
    }
  }
}

  getServiceIdByCode(services: Service[], code: string): number {
    const found = services.find(s => s.libelle === code || s.id.toString() === code)
    if (found) {
      console.log('Service trouvé:', found)
      return found.id
    }
    console.log('Service non trouvé pour code:', code, 'parmi:', services)
    return 1 // Valeur par défaut
  }

  // ==================== CHARGEMENT TOUTES LES RÉFÉRENCES ====================
  
  async loadAllReferences() {
    try {
      const [banques, fonctions, typeContrats, categories, typeServices] = await Promise.all([
        this.getAllBanques(),
        this.getAllFonctions(),
        this.getAllTypeContrats(),
        this.getAllCategories(),
        this.getAllTypeServices()
      ])

      return {
        banques: banques.success ? banques.data : [],
        fonctions: fonctions.success ? fonctions.data : [],
        typeContrats: typeContrats.success ? typeContrats.data : [],
        categories: categories.success ? categories.data : [],
        typeServices: typeServices.success ? typeServices.data : []
      }
    } catch (error) {
      console.error('Error loading references:', error)
      return {
        banques: [],
        fonctions: [],
        typeContrats: [],
        categories: [],
        typeServices: []
      }
    }
  }
}

export const referencesService = new ReferencesService()
