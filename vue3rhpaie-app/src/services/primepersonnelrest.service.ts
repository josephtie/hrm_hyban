
import { createAuthenticatedApi } from './api'
import type { ApiResponse } from '@/types/auth'

export interface PrimePersonnelRestDto {
  id?: number
  montantop?: number
  valeurop?: number
  idPeriode?: number
  periodePaie?: {
    id: number
    libelle?: string
  }
  pretPersonnel?: {
    personnel?: {
      nomComplet: string
      matricule: string
    }
    pret?: {
      libelle: string
    }
  }
  message?: string
}

export interface PrimePersonnelRestFilter {
  page?: number
  size?: number
  search?: string
}

export interface PrimeIndividuelRequest {
  id?: number
  montantop?: number
  valeurop?: number
  idPeriode?: number
  idCtrat?: number
  ctrat?: number
  idAbsence?: number
  idPrime?: number
  idPersonnel?: number
}

export interface PrimeCollectiveRequest {
  montant?: number
  idPeriode?: number
  typeElementId?: number
  categorieId?: number
  personnelIds?: number[]
  commentaire?: string
}

export interface PrimeCollectiveBatchRequest {
  idPrime: number
  montantop: number
  valeurop?: number
  idPeriode?: number
  idPersonnels: number[]
  commentaire?: string
}

export interface RubriquePrimeMontantDto {
  rubriqueId?: number
  primePersonnelId?: number
  code?: string
  libelle?: string
  etatImposition?: number
  montant?: number
  valeur?: number
  mtExedent?: number
}

export interface RubriquesContratPeriodeDto {
  idPeriode?: number
  idCtrat?: number
  listePrimesImp?: RubriquePrimeMontantDto[]
  listePrimesNonImpos?: RubriquePrimeMontantDto[]
  listePrimesImposetNon?: RubriquePrimeMontantDto[]
  listePrimesMutuelle?: RubriquePrimeMontantDto[]
  listePrimesSociale?: RubriquePrimeMontantDto[]
  listePrimesGains?: RubriquePrimeMontantDto[]
}

class PrimePersonnelRestService {
  private api = createAuthenticatedApi()

  // ✅ LIST - retourne les rows mappés au format ElementPaie attendu par la vue
  async getAll(filter?: PrimePersonnelRestFilter): Promise<ApiResponse<any[]>> {
    try {
      const params = {
        limit: filter?.size || 10,
        offset: ((filter?.page || 1) - 1) * (filter?.size || 10),
        search: filter?.search || ''
      }

      const res = await this.api.get('/paie/prime-personnel/list', { params })
      const rawRows: any[] = res.data.rows || []

      const mapped = rawRows.map((row: any) => {
        const prime = row.prime || {}
        const contrat = row.contratPersonnel || {}
        const personnel = contrat.personnel || {}
        const periode = row.periode || row.periodePaie || {}

        const nomComplet = personnel.nomComplet
          || `${personnel.prenom || ''} ${personnel.nom || ''}`.trim()
          || personnel.matricule
          || ''

        // Déterminer le type Gain/Retenue
        const rawType = (prime.typeRubrique || prime.categorie || '').toString().toLowerCase()
        const type = rawType.includes('reten') || rawType.includes('charge')
          ? 'Retenue'
          : 'Gain'

        const montant = typeof row.montant === 'string'
          ? parseFloat(row.montant)
          : (row.montant || 0)

        return {
          id: row.id,
          personnelId: personnel.id,
          personnelNom: nomComplet,
          matricule: personnel.matricule || '',
          typeElementId: prime.id,
          libelleElement: prime.libelle || '',
          codeElement: prime.code || '',
          type,
          quantite: row.valeur || 0,
          quantiteAffichee: row.valeur != null ? String(row.valeur) : '1',
          montant: type === 'Retenue' ? -Math.abs(montant) : montant,
          periode: periode.affiche || periode.libelle || (periode.id != null ? String(periode.id) : ''),
          periodeId: periode.id,
          statut: 'actif',
          commentaire: ''
        }
      })

      return {
        success: true,
        data: mapped,
        total: res.data.total || 0,
        message: 'OK'
      }
    } catch (e: any) {
      return {
        success: false,
        data: [],
        total: 0,
        message: e.response?.data?.message || 'Erreur chargement'
      }
    }
  }

  // ✅ SAVE INDIVIDUEL
  async savePrime(req: PrimeIndividuelRequest): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.post('/paie/prime-personnel/enregistrer', req)

      return { success: true, data: res.data, message: 'OK' }
    } catch (e: any) {
      return { success: false, data: null, message: e.response?.data?.message }
    }
  }

  // ✅ SAVE COLLECTIF
  async savePrimeCollective(req: PrimeCollectiveRequest): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.post('/paie/prime-personnel/enregistrer-collective', req)

      return { success: true, data: res.data, message: 'OK' }
    } catch (e: any) {
      return { success: false, data: null, message: e.response?.data?.message }
    }
  }

  // ✅ SAVE COLLECTIF EN LOT (1 seul appel, 1 seule transaction côté backend)
  async savePrimeCollectiveBatch(req: PrimeCollectiveBatchRequest): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.post('/paie/prime-personnel/enregistrer-collective-batch', req)
      const dto = res.data || {}
      const success = dto.result === true || dto.status === true
      return {
        success,
        data: dto,
        total: dto.total || 0,
        message: dto.message || (success ? 'OK' : 'Erreur')
      }
    } catch (e: any) {
      return { success: false, data: null, message: e.response?.data?.message || e.message }
    }
  }

  // ✅ DELETE
  async deletePrime(id: number): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.delete(`/paie/prime-personnel/supprimer/${id}`)

      return { success: true, data: res.data, message: 'OK' }
    } catch (e: any) {
      return { success: false, data: null, message: e.response?.data?.message }
    }
  }

  // ✅ TYPES ELEMENTS
  async getTypesElements(): Promise<ApiResponse<any[]>> {
    try {
      const res = await this.api.get('/paie/prime-personnel/types-elements')

      return { success: true, data: res.data || [], message: 'OK' }
    } catch (e: any) {
      return { success: false, data: [], message: e.response?.data?.message }
    }
  }

  // ✅ CATEGORIES
  async getCategories(): Promise<ApiResponse<any[]>> {
    try {
      const res = await this.api.get('/paie/prime-personnel/categories')

      return { success: true, data: res.data || [], message: 'OK' }
    } catch (e: any) {
      return { success: false, data: [], message: e.response?.data?.message }
    }
  }

  async getRubriquesContratPeriode(idPeriode: number, idCtrat: number): Promise<ApiResponse<RubriquesContratPeriodeDto | null>> {
    try {
      const res = await this.api.get('/paie/prime-personnel/rubriques-contrat-periode', {
        params: { idPeriode, idCtrat }
      })

      return {
        success: true,
        data: (res.data || null) as RubriquesContratPeriodeDto | null,
        message: 'OK'
      }
    } catch (e: any) {
      return {
        success: false,
        data: null,
        message: e.response?.data?.message || e.message || 'Erreur chargement rubriques contrat/période'
      }
    }
  }
}

// ✅ UN SEUL EXPORT
export const primepersonnelrestService = new PrimePersonnelRestService()
export default primepersonnelrestService
