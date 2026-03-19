import { api } from './api'
import type { ApiResponse } from '@/types/auth'

// Interface pour compatibilité avec le code existant
export interface PersonnelRestDto {
  id?: number
  matricule?: string
  nom?: string
  prenom?: string
  dateNaissance?: string
  lieuNaissance?: string
  sexe?: string
  nationalite?: number      // Backend: Long
  service?: number          // Backend: Long
  categorie?: number        // Backend: Long
  fonction?: number         // Backend: Long
  typeContrat?: number      // Backend: Long
  departement?: number      // Backend: Long
  situationMatrimoniale?: number  // Backend: Integer (pas string)
  nombreEnfant?: number          // Backend: Integer (pas nombreEnfants)
  telephone?: string
  email?: string
  residence?: string
  dateArrivee?: string
  dateRetourcg?: string          // Backend: dateRetourcg (pas dateRetourConge)
  numeroCNPS?: string
  adresse?: string
  dateDebut?: string
  dateFin?: string
  modePaiement?: string
  idBanque?: number              // Backend: idBanque (pas banque)
  numeroCompte?: string
  numeroGuichet?: string
  rib?: string
  salaireNet?: number
  indemnitelogement?: number
  ancienneteInitial?: number
  carec?: boolean
  typeEmp?: string
  situationMedaille?: number
  situationEmploie?: number
  sursalaire?: number
  indemniteTransport?: number
  indemniteRespons?: number
  indemniteRepresent?: number
}

export interface PersonnelRestFilter {
  page?: number
  size?: number
  search?: string
  sortBy?: 'nom' | 'prenom' | 'matricule' | 'createdAt'
  sortOrder?: 'asc' | 'desc'
  service?: string
  statut?: string
  modePaiement?: string
  fonction?: string
}

class PersonnelRestService {
  private readonly endpoint = '/personnels'

  // Récupérer la liste des personnels avec pagination
  async getPersonnels(filter?: PersonnelRestFilter): Promise<ApiResponse<PersonnelRestDto[]>> {
    try {
      // Utiliser PersonnelRestController endpoint avec tous les filtres
      const paginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search,
        service: filter?.service,
        statut: filter?.statut,
        modePaiement: filter?.modePaiement,
        fonction: filter?.fonction
      }
      
      const response = await api.post('/personnels/personnel/list', paginationRequest)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.rows || [],
        total: data.total || 0,
        message: data.message || 'Personnel retrieved successfully'
      }
    } catch (error) {
      console.error('Erreur getPersonnels:', error)
      return {
        success: false,
        data: [],
        total: 0,
        message: 'Failed to fetch personnel'
      }
    }
  }

  // Récupérer un personnel par son ID
  // Vérifier si un matricule existe déjà
  async checkMatriculeExists(matricule: string): Promise<boolean> {
    try {
      const response = await api.get(`/personnels/personnel/recherche/${matricule}`)
      return response.data && response.data.length > 0
    } catch (error: any) {
      // Si erreur 404, le matricule n'existe pas
      if (error.response?.status === 404) {
        return false
      }
      console.error('Error checking matricule:', error)
      return false
    }
  }

  // Récupérer un personnel par son ID
  async getPersonnelById(id: number): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const response = await api.get(`/personnels/personnel/${id}`)
      const data = response.data
      
      return {
        success: true,
        data: data, // PersonnelRestController retourne directement l'entité Personnel
        message: 'Personnel retrieved successfully'
      }
    } catch (error) {
      console.error('Error fetching personnel:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to fetch personnel'
      }
    }
  }

  // Créer un nouveau personnel avec contrat
  async createPersonnel(personnel: Partial<PersonnelRestDto>): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      console.log('Données envoyées au backend (personnel.service.ts):', personnel)
      const response = await api.post('/personnels/personnel/enregistrerpersonnel', personnel)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Personnel créé avec succès'
      }
    } catch (error) {
      console.error('Error creating personnel:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to create personnel'
      }
    }
  }

  // Mettre à jour un personnel existant
  async updatePersonnel(personnel: Partial<PersonnelRestDto>): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const response = await api.post('/personnels/personnel/modifierpersonnel', personnel)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Personnel mis à jour avec succès'
      }
    } catch (error) {
      console.error('Error updating personnel:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to update personnel'
      }
    }
  }

  // Supprimer un personnel
  async deletePersonnel(id: number): Promise<ApiResponse<boolean>> {
    try {
      const response = await api.post('/personnels/personnel/supprimerpersonnel', { id })
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Personnel supprimé avec succès'
      }
    } catch (error) {
      console.error('Error deleting personnel:', error)
      return {
        success: false,
        data: false,
        message: 'Failed to delete personnel'
      }
    }
  }

  // Marquer le départ d'un personnel
  async departPersonnel(id: number): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const response = await api.post('/personnels/personnel/departpersonnel', { id })
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Départ enregistré avec succès'
      }
    } catch (error) {
      console.error('Error marking departure:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to mark departure'
      }
    }
  }

  // Activer un personnel
  async activatePersonnel(id: number): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const response = await api.post('/personnels/personnel/activer', { id })
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Personnel activé avec succès'
      }
    } catch (error) {
      console.error('Error activating personnel:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to activate personnel'
      }
    }
  }

  // Désactiver un personnel
  async deactivatePersonnel(id: number): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const response = await api.post('/personnels/personnel/desactiver', { id })
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Personnel désactivé avec succès'
      }
    } catch (error) {
      console.error('Error deactivating personnel:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to deactivate personnel'
      }
    }
  }

  // Rechercher par matricule
  async searchByMatricule(matricule: string): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const response = await api.get(`/personnels/personnel/recherche/${matricule}`)
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Personnel trouvé'
      }
    } catch (error) {
      console.error('Error searching by matricule:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to search personnel'
      }
    }
  }

  // Créer ou mettre à jour un personnel (méthode legacy pour compatibilité)
  async savePersonnel(personnel: Partial<PersonnelRestDto>): Promise<ApiResponse<PersonnelRestDto>> {
    if (personnel.id) {
      return this.updatePersonnel(personnel)
    } else {
      return this.createPersonnel(personnel)
    }
  }

  // ==================== GESTION DES PHOTOS ====================
  
  // Uploader la photo d'un personnel
  async uploadPhoto(personnelId: number, photoFile: File): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      const formData = new FormData()
      formData.append('photo', photoFile)
      formData.append('id', personnelId.toString())
      
      const response = await api.post('/personnels/personnel/upload/photo', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      const data = response.data
      
      return {
        success: data.result === 'success',
        data: data.data,
        message: data.message || 'Photo uploadée avec succès'
      }
    } catch (error) {
      console.error('Error uploading photo:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to upload photo'
      }
    }
  }

  // Récupérer la photo d'un personnel
  async getPhoto(personnelId: number): Promise<string> {
    try {
      const response = await api.get(`/personnels/personnel/photo/${personnelId}`, {
        responseType: 'blob'
      })
      
      // Convertir blob en URL
      const blob = new Blob([response.data])
      const url = URL.createObjectURL(blob)
      return url
    } catch (error: any) {
      // Si c'est une erreur 404, c'est normal (pas de photo)
      if (error.response?.status === 404) {
        // Pas d'erreur pour les photos manquantes, c'est un cas normal
        return ''
      }
      // Pour les autres erreurs, on log mais on retourne quand même une chaîne vide
      console.warn('Photo fetch error (non-404):', error.message || 'Unknown error')
      return ''
    }
  }

  // Uploader une photo depuis une base64 (pour le wizard)
  async uploadPhotoFromBase64(personnelId: number, base64Data: string): Promise<ApiResponse<PersonnelRestDto>> {
    try {
      // Convertir base64 en File
      const base64Response = await fetch(base64Data)
      const blob = await base64Response.blob()
      const filename = `personnel_${personnelId}_${Date.now()}.jpg`
      const file = new File([blob], filename, { type: 'image/jpeg' })
      
      return this.uploadPhoto(personnelId, file)
    } catch (error) {
      console.error('Error uploading photo from base64:', error)
      return {
        success: false,
        data: {} as PersonnelRestDto,
        message: 'Failed to upload photo from base64'
      }
    }
  }

  // Obtenir tous les personnels (sans pagination)
  async getAllPersonnel(): Promise<ApiResponse<PersonnelRestDto[]>> {
    try {
      const response = await api.get('/personnels/personnel/list/all')
      const data = response.data
      
      return {
        success: true,
        data: data || [],
        message: 'All personnel retrieved successfully'
      }
    } catch (error) {
      console.error('Error getting all personnel:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to get all personnel'
      }
    }
  }

  // Récupérer les contrats d'un personnel
  async getContratsByPersonnel(personnelId: number): Promise<ApiResponse<any[]>> {
    try {
      const response = await api.post('/personnels/listcontratparpersonneljson', {
        id: personnelId
      })
      const data = response.data
      
      // Vérifier si les données existent même si status est false
      const hasData = data.rows && data.rows.length > 0
      
      return {
        success: (data.status !== false) || hasData, // Succès si status true OU si données existent
        data: data.rows || data || [],
        message: data.message || (hasData ? 'Contrats retrieved successfully' : 'No contracts found')
      }
    } catch (error) {
      console.error('Error fetching contracts:', error)
      return {
        success: false,
        data: [],
        message: 'Failed to fetch contracts'
      }
    }
  }
}

export const personnelRestService = new PersonnelRestService()
