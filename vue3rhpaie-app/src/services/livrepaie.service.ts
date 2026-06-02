import { createAuthenticatedApi } from './api'
import type { ApiResponse } from '@/types/auth'

export interface LivrePaiePersonnel {
  id: number
  contratId?: number
  matricule: string
  nomComplet: string
  statut: string
  sexe: string
  compte: string
  fonction: string
  situationMatrimoniale: string
  nbEnfants: number
  salaireCategoriel: number
  netAPayer: number
  enSommeil: boolean
}

export interface LivrePaieFilter {
  page?: number
  size?: number
  search?: string
}

export interface BulletinFilter {
  idPeriode?: number
  page?: number
  size?: number
  search?: string
}

/**
 * Payload pour la modification rapide d'un personnel
 * (situation matrimoniale, nb enfants, statut sommeil/actif).
 *
 * Mapping backend des situations matrimoniales :
 *   1 = MARIE, 2 = CELIBATAIRE, 3 = DIVORCE, 4 = VEUF
 */
export interface EditerPersonnelPayload {
  idPersonnel: number
  situationMatrimoniale: number
  nombreEnfant: number
  statut: boolean // true = actif, false = en sommeil
}

/**
 * Payload pour l'enregistrement du temps de travail effectif d'un personnel
 * sur une période de paie.
 */
export interface TempEffectifPayload {
  idPers: number          // id du personnel
  idPeriodDep: number     // id de la période de paie
  jourtravail: number     // nombre de jours travaillés
  temptravail: number     // temps de travail (heures)
}

export interface PeriodePaieRef {
  id: number
  libelle?: string
  cloture?: boolean
  [k: string]: any
}

/** Convertit la valeur du select Vue ('marie', 'celibataire', ...) en code int backend */
export function situationMatriToCode(value: string | number | undefined | null): number {
  if (typeof value === 'number') return value
  const v = String(value || '').toLowerCase()
  if (v === 'marie' || v === 'marié' || v === 'mariee' || v === 'mariée') return 1
  if (v === 'celibataire' || v === 'célibataire') return 2
  if (v === 'divorce' || v === 'divorcé') return 3
  if (v === 'veuf' || v === 'veuve') return 4
  return 0
}

/**
 * Mappe une entrée backend (ContratPersonnel + Personnel imbriqué) vers la
 * forme attendue par l'écran Livre de Paie.
 */
function mapContratToPersonnel(row: any): LivrePaiePersonnel {
  const personnel = row?.personnel || {}
  const fonction = row?.fonction || {}
  const categorie = row?.categorie || {}

  const enSommeil =
    String(personnel.enSommeil || '').toUpperCase() === 'OUI'
    || personnel.enSommeil === true

  // Sexe → 'M' / 'F'
  const sexeRaw = String(personnel.sexe || '').toLowerCase()
  const sexe = sexeRaw.startsWith('f') ? 'F' : 'M'

  const nomComplet =
    personnel.nomComplet
    || `${personnel.prenom || ''} ${personnel.nom || ''}`.trim()
    || personnel.matricule
    || ''

  // Salaire catégoriel : priorité au salaire de la catégorie du contrat
  const salaireCategoriel = Number(
    categorie.salaireDeBase
    ?? row?.netAPayer
    ?? personnel.netapayer
    ?? 0
  )

  const netAPayer = Number(
    row?.netAPayer
    ?? personnel.netapayer
    ?? 0
  )

  return {
    id: personnel.id ?? row?.id,
    contratId: Number(row?.id ?? row?.idCtrat ?? row?.idContrat ?? 0) || undefined,
    matricule: personnel.matricule || '',
    nomComplet,
    statut: enSommeil ? 'sommeil' : 'actif',
    sexe,
    compte: personnel.numeroCompte || personnel.telephone || '',
    fonction: fonction.libelle || categorie.libelle || '',
    situationMatrimoniale: String(personnel.situationMatri || '').toLowerCase(),
    nbEnfants: Number(personnel.nombrEnfant ?? 0),
    salaireCategoriel,
    netAPayer,
    enSommeil
  }
}

class LivrePaieService {
  private api = createAuthenticatedApi()

  /**
   * Charge la liste des contrats actifs (personnel paie) avec pagination.
   * Endpoint backend: POST /api/personnels/listcontratpersonneljson
   */
  async loadPersonnel(filter?: LivrePaieFilter): Promise<ApiResponse<LivrePaiePersonnel[]>> {
    try {
      const limit = filter?.size ?? 20
      const page = filter?.page ?? 1
      const offset = (page - 1) * limit

      const body = {
        limit,
        offset,
        search: filter?.search || ''
      }

      const res = await this.api.post('/personnels/listcontratpersonneljson', body)
      const dto = res.data || {}
      const rows: any[] = Array.isArray(dto.rows) ? dto.rows : []

      return {
        success: true,
        data: rows.map(mapContratToPersonnel),
        total: dto.total ?? 0,
        message: dto.message || 'OK'
      }
    } catch (e: any) {
      console.error('Erreur chargement livre de paie:', e)
      return {
        success: false,
        data: [],
        total: 0,
        message: e?.response?.data?.message || e?.message || 'Erreur de chargement'
      }
    }
  }

  /**
   * Charge les bulletins du mois actif (période courante) en base,
   * avec pagination et recherche.
   * Endpoint : GET /api/paie/bulletin/bulletinperiodeactifjson
   */
  async loadBulletinsByPeriode(filter?: BulletinFilter): Promise<ApiResponse<any[]>> {
    try {
      const limit = filter?.size ?? 50
      const page = filter?.page ?? 1
      const offset = Math.max(page - 1, 0) * limit

      const res = await this.api.get('/paie/bulletin/bulletinperiodeactifjson', {
        params: {
          id: filter?.idPeriode,
          limit,
          offset,
          search: filter?.search || ''
        }
      })

      const dto = res.data || {}
      const rows: any[] = Array.isArray(dto.rows) ? dto.rows : []
      const ok = dto?.result !== 'echec' && dto?.result !== 'error'

      return {
        success: ok,
        data: rows,
        total: Number(dto?.total ?? rows.length),
        message: dto?.message || (ok ? 'OK' : 'Échec du chargement des bulletins')
      }
    } catch (e: any) {
      console.error('Erreur chargement bulletins période:', e)
      return {
        success: false,
        data: [],
        total: 0,
        message: e?.response?.data?.message || e?.message || 'Erreur de chargement des bulletins'
      }
    }
  }

  /**
   * Récupère la période de paie active (courante).
   * Endpoint : GET /parametrages/periodes/active
   */
  async getPeriodeActive(): Promise<ApiResponse<PeriodePaieRef | null>> {
    try {
      const res = await this.api.get('/parametrages/periodes/active')
      const dto = res.data || {}
      const row = dto.row || (Array.isArray(dto.rows) ? dto.rows[0] : null) || null
      return { success: !!row, data: row, message: dto?.result || 'OK' }
    } catch (e: any) {
      console.error('Erreur chargement période active:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de chargement de la période active'
      }
    }
  }

  /**
   * Génère et enregistre les bulletins de paie pour la période demandée.
   * Si idPeriode est omis, la période active est utilisée côté backend.
   * Endpoint : POST /api/paie/bulletin/saveBullPersonnel
   */
  async generateBulletins(idPeriode?: number, opts?: { limit?: number; pageIndex?: number }): Promise<ApiResponse<any>> {
    try {
      // Génération longue (calculs sur 1000+ personnels) → timeout étendu à 10 minutes
      // limit=0 → backend renvoie toutes les lignes sans pagination (mode scroll)
      const res = await this.api.post(
        '/paie/bulletin/saveBullPersonnel',
        {
          idPeriode: idPeriode ?? null,
          limit: opts?.limit ?? 0,
          offset: opts?.pageIndex ?? 0
        },
        { timeout: 600_000 }
      )
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error'
      return {
        success: ok,
        data: dto,
        total: dto?.total ?? (Array.isArray(dto?.rows) ? dto.rows.length : 0),
        message: ok
          ? (dto?.message || 'Bulletins générés avec succès')
          : (dto?.message || 'Échec de la génération des bulletins')
      }
    } catch (e: any) {
      console.error('Erreur génération bulletins:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de génération'
      }
    }
  }

  /**
   * Lance le calcul à l'envers (Net cible -> Sursalaire) sur tous les contrats actifs
   * de la période active. Met à jour le sursalaire de chaque contrat en base.
   * Endpoint : POST /api/paie/bulletin/calculalenvers-liste
   *
   * Opération longue (boucle sur N contrats x 20 itérations) -> timeout étendu.
   */
  async regularizeNetListe(idPeriode?: number): Promise<ApiResponse<any[]>> {
    try {
      const params: Record<string, any> = {}
      if (idPeriode != null) params.id = idPeriode

      const res = await this.api.post(
        '/paie/bulletin/calculalenvers-liste',
        null,
        { params, timeout: 600_000 }
      )
      const list: any[] = Array.isArray(res.data) ? res.data : []
      return {
        success: true,
        data: list,
        total: list.length,
        message: `Régularisation effectuée sur ${list.length} contrat(s)`
      }
    } catch (e: any) {
      console.error('Erreur calcul à l\'envers (liste):', e)
      return {
        success: false,
        data: [],
        message: e?.response?.data?.message || e?.message || 'Erreur de régularisation'
      }
    }
  }

  /**
   * Liste des périodes de paie ouvertes (>= période courante).
   * Endpoint : GET /api/paie/temp-effectif/periodes
   */
  async loadPeriodes(): Promise<ApiResponse<PeriodePaieRef[]>> {
    try {
      const res = await this.api.get('/paie/temp-effectif/periodes')
      const list: any[] = Array.isArray(res.data) ? res.data : []
      return { success: true, data: list, message: 'OK' }
    } catch (e: any) {
      console.error('Erreur chargement périodes:', e)
      return {
        success: false,
        data: [],
        message: e?.response?.data?.message || e?.message || 'Erreur de chargement des périodes'
      }
    }
  }

  /**
   * Recherche le temps effectif déjà saisi pour un personnel sur une période.
   * Endpoint : GET /api/paie/temp-effectif/chercher?idPersonnel=...&idPeriodDep=...
   * Renvoie null si rien n'est trouvé.
   */
  async findTempEffectif(idPersonnel: number, idPeriodDep: number): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.get('/paie/temp-effectif/chercher', {
        params: { idPersonnel, idPeriodDep }
      })
      return { success: true, data: res.data || null, message: 'OK' }
    } catch (e: any) {
      console.error('Erreur recherche temp effectif:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de recherche'
      }
    }
  }

  /**
   * Enregistre le temps de travail (jours + heures) d'un personnel pour une période.
   * Endpoint REST : POST /api/paie/temp-effectif/enregistrer
   */
  async saveTempEffectif(payload: TempEffectifPayload): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.post('/paie/temp-effectif/enregistrer', payload)
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error'
      return {
        success: ok,
        data: dto,
        message: ok
          ? (dto?.message || 'Temps de travail enregistré')
          : (dto?.message || 'Échec de l\'enregistrement')
      }
    } catch (e: any) {
      console.error('Erreur enregistrement temp effectif:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur d\'enregistrement'
      }
    }
  }

  /**
   * Enregistre le nouveau sursalaire issu du calcul à l'envers (Net → Sursalaire).
   * Met à jour le contrat actif du personnel.
   * Endpoint REST : POST /api/personnels/updatecontratpersonnelSursal
   * Body : { idPersonnel: Long, sursalaire: Double }
   */
  async updateContratSursalaire(idPersonnel: number, sursalaire: number): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.post('/personnels/updatecontratpersonnelSursal', {
        idPersonnel,
        sursalaire
      })
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error'
      return {
        success: ok,
        data: dto,
        message: ok
          ? (dto?.message || 'Sursalaire mis à jour')
          : (dto?.message || 'Échec de la mise à jour du sursalaire')
      }
    } catch (e: any) {
      console.error('Erreur update sursalaire:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de mise à jour du sursalaire'
      }
    }
  }

  /**
   * Confirme la génération du livre de paie (sauvegarde finale).
   * Endpoint : GET /api/paie/bulletin/saveBulletinlivrepaie
   */
  async confirmGeneration(): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.get('/paie/bulletin/saveBulletinlivrepaie')
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error'
      return {
        success: ok,
        data: dto.rows || [],
        total: dto.total || 0,
        message: dto.message || (ok ? 'Génération confirmée avec succès' : 'Échec de la confirmation')
      }
    } catch (e: any) {
      console.error('Erreur confirmation génération:', e)
      return {
        success: false,
        data: [],
        total: 0,
        message: e?.response?.data?.message || e?.message || 'Erreur lors de la confirmation'
      }
    }
  }

  /**
   * Exporte les bulletins de paie au format Julaya (Excel macro .xlsm) pour virements.
   * Endpoint : GET /api/paie/bulletin/export
   */
  async exportJulaya(): Promise<Blob | null> {
    try {
      const res = await this.api.get('/paie/bulletin/export', {
        responseType: 'blob'
      })
      return res.data as Blob
    } catch (e: any) {
      console.error('Erreur export Julaya:', e)
      return null
    }
  }

  /**
   * Télécharge le PDF d'un bulletin individuel.
   * Endpoint : GET /api/paie/bulletin/rapport/{id}/pdf
   */
  async printBulletinPdf(id: number): Promise<Blob | null> {
    try {
      const res = await this.api.get(`/paie/bulletin/rapport/${id}/pdf`, {
        responseType: 'blob'
      })
      return res.data as Blob
    } catch (e: any) {
      console.error('Erreur impression bulletin:', e)
      return null
    }
  }

  /**
   * Sauvegarde une copie locale du fichier Excel d'export bulletins sur le serveur.
   * Endpoint : POST /api/paie/bulletin/save-excel-export
   */
  async saveExcelExport(blob: Blob, filename: string): Promise<boolean> {
    try {
      const formData = new FormData()
      formData.append('file', blob, filename)
      await this.api.post('/paie/bulletin/save-excel-export', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      return true
    } catch (e: any) {
      console.error('Erreur sauvegarde export Excel:', e)
      return false
    }
  }

  /**
   * Modifie un personnel (situation matrimoniale, nombre d'enfants, statut actif/sommeil).
   * Endpoint REST : PUT /api/personnels/personnel/editerpersonnel
   */
  async updatePersonnel(payload: EditerPersonnelPayload): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.put('/personnels/personnel/editerpersonnel', payload)
      const dto = res.data || {}
      const ok = dto?.result !== 'echec'
      return {
        success: ok,
        data: dto,
        message: ok ? 'Personnel modifié avec succès' : (dto?.message || 'Échec de la modification')
      }
    } catch (e: any) {
      console.error('Erreur modification personnel:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de modification'
      }
    }
  }
}

export const livrePaieService = new LivrePaieService()
export const useLivreDePaieService = () => livrePaieService
export default livrePaieService

