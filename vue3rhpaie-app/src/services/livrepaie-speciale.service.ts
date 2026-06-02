import { createAuthenticatedApi } from './api'
import type { ApiResponse } from '@/types/auth'
import type { BulletinFilter, EditerPersonnelPayload, LivrePaieFilter, LivrePaiePersonnel, PeriodePaieRef, TempEffectifPayload } from './livrepaie.service'
export { situationMatriToCode, type BulletinFilter, type EditerPersonnelPayload, type LivrePaieFilter, type LivrePaiePersonnel, type PeriodePaieRef, type TempEffectifPayload } from './livrepaie.service'

function mapSpecialContractToPersonnel(row: any): LivrePaiePersonnel {
  const employee = row?.employee || row?.employe || row?.personnel || row || {}
  const fonction = row?.fonction || employee?.fonction || {}
  const site = row?.site || employee?.site || {}
  const actif = row?.actif !== false && employee?.actif !== false
  const nomComplet = employee?.nomComplet || `${employee?.prenom || ''} ${employee?.nom || ''}`.trim() || employee?.matricule || ''

  return {
    id: Number(employee?.id ?? row?.employeeId ?? row?.idEmpl ?? row?.id ?? 0),
    contratId: Number(row?.id ?? row?.contratId ?? 0) || undefined,
    matricule: employee?.matricule || row?.matricule || '',
    nomComplet,
    statut: actif ? 'actif' : 'sommeil',
    sexe: String(employee?.sexe || row?.sexe || '').toLowerCase().startsWith('f') ? 'F' : 'M',
    compte: row?.paiementNumber || employee?.paiementNumber || employee?.phoneNumber || '',
    fonction: fonction?.libelle || fonction?.description || site?.libelle || '',
    situationMatrimoniale: String(employee?.situationMatrimoniale ?? employee?.situationMatri ?? ''),
    nbEnfants: Number(employee?.nombreEnfant ?? employee?.nombrEnfant ?? 0),
    salaireCategoriel: Number(row?.remunerationForfaitaire ?? employee?.netapayer ?? row?.netpayer ?? 0),
    netAPayer: Number(row?.remunerationForfaitaire ?? employee?.netapayer ?? row?.netpayer ?? 0),
    enSommeil: !actif
  }
}

function successResponse<T>(data: T, message = 'OK', total?: number): ApiResponse<T> {
  return { success: true, data, total, message }
}

function failureResponse<T>(data: T, message: string, total = 0): ApiResponse<T> {
  return { success: false, data, total, message }
}

class LivrePaieSpecialeService {
  private api = createAuthenticatedApi()

  async loadPersonnel(filter?: LivrePaieFilter): Promise<ApiResponse<LivrePaiePersonnel[]>> {
    try {
      const limit = filter?.size ?? 20
      const page = filter?.page ?? 1
      const offset = (page - 1) * limit
      const res = await this.api.get('/personnels/specifique/special-contracts/listcontratspecjson', {
        params: { limit, offset, search: filter?.search || '' }
      })
      const dto = res.data || {}
      const rows: any[] = Array.isArray(dto.rows) ? dto.rows : []
      return successResponse(rows.map(mapSpecialContractToPersonnel), dto.message || 'OK', dto.total ?? rows.length)
    } catch (e: any) {
      console.error('Erreur chargement livre de paie spéciale:', e)
      return failureResponse([], e?.response?.data?.message || e?.message || 'Erreur de chargement')
    }
  }

  async loadBulletinsByPeriode(filter?: BulletinFilter): Promise<ApiResponse<any[]>> {
    try {
      const limit = filter?.size ?? 50
      const page = filter?.page ?? 1
      const offset = (page - 1) * limit

      if (!filter?.idPeriode) {
        return failureResponse([], 'Aucune période sélectionnée')
      }

      const res = await this.api.get('/paie/bulletin/bulletinSpecialeperiodeactifjson', {
        params: {
          id: filter.idPeriode,
          limit,
          offset,
          search: filter?.search || ''
        }
      })
      const dto = res.data || {}
      const rows: any[] = Array.isArray(dto.rows) ? dto.rows : []
      return successResponse(rows, dto.message || 'OK', dto.total ?? rows.length)
    } catch (e: any) {
      console.error('Erreur chargement bulletins spéciaux:', e)
      return failureResponse([], e?.response?.data?.message || e?.message || 'Erreur de chargement des bulletins spéciaux')
    }
  }

  async getPeriodeActive(): Promise<ApiResponse<PeriodePaieRef | null>> {
    try {
      const res = await this.api.get('/parametrages/periodes/active')
      const dto = res.data || {}
      const row = dto.row || (Array.isArray(dto.rows) ? dto.rows[0] : null) || null
      return { success: !!row, data: row, message: dto?.result || 'OK' }
    } catch (e: any) {
      console.error('Erreur chargement période active spéciale:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de chargement de la période active'
      }
    }
  }

  async loadPeriodes(): Promise<ApiResponse<PeriodePaieRef[]>> {
    try {
      const res = await this.api.get('/paie/temp-effectif/periodes')
      const list: any[] = Array.isArray(res.data) ? res.data : []
      return { success: true, data: list, message: 'OK' }
    } catch (e: any) {
      console.error('Erreur chargement périodes spéciales:', e)
      return {
        success: false,
        data: [],
        message: e?.response?.data?.message || e?.message || 'Erreur de chargement des périodes'
      }
    }
  }

  async generateBulletins(idPeriode?: number, opts?: { limit?: number; pageIndex?: number }): Promise<ApiResponse<any>> {
    try {
      const limit = opts?.limit ?? 100
      const pageIndex = opts?.pageIndex ?? 0
      const offset = pageIndex * limit
      const active = idPeriode ? null : await this.getPeriodeActive()
      const id = idPeriode ?? active?.data?.id

      if (!id) {
        return failureResponse(null, 'Aucune période de paie courante trouvée')
      }

      const res = await this.api.get('/paie/bulletin/savebullEmployee', {
        params: { id, limit, offset },
        timeout: 600_000
      })
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error' && dto?.result !== 'failed'
      return {
        success: ok,
        data: dto,
        total: dto?.total ?? (Array.isArray(dto?.rows) ? dto.rows.length : 0),
        message: ok
          ? (dto?.message || 'Livre de paie spéciale généré')
          : (dto?.message || 'Échec de la génération du livre de paie spéciale')
      }
    } catch (e: any) {
      console.error('Erreur génération livre de paie spéciale:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de génération'
      }
    }
  }

  async regularizeNetListe(): Promise<ApiResponse<any[]>> {
    return failureResponse([], 'Régularisation spéciale non disponible côté API')
  }

  async findTempEffectif(): Promise<ApiResponse<any>> {
    return successResponse(null, 'Temps effectif non applicable')
  }

  async saveTempEffectif(payload: TempEffectifPayload): Promise<ApiResponse<any>> {
    try {
      const params = new URLSearchParams()
      params.append('temptravail', String(payload.temptravail))
      params.append('jourtravail', String(payload.jourtravail))
      params.append('idPers', String(payload.idPers))
      params.append('idPeriodDep', String(payload.idPeriodDep))

      const res = await this.api.post('/paie/temp-effectif/savetempeffectifEmp', params, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      })
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error' && dto?.result !== 'failed'
      return {
        success: ok,
        data: dto,
        message: ok
          ? (dto?.message || 'Temps de travail enregistré')
          : (dto?.message || 'Échec de l\'enregistrement')
      }
    } catch (e: any) {
      console.error('Erreur enregistrement temp effectif spécial:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur d\'enregistrement'
      }
    }
  }

  async updateContratSursalaire(_idPersonnel: number, _sursalaire: number): Promise<ApiResponse<any>> {
    return failureResponse(null, 'Mise à jour du sursalaire spécial non disponible côté API')
  }

  async confirmGeneration(): Promise<ApiResponse<any>> {
    try {
      const res = await this.api.post('/paie/bulletin/saveBulletinEmplivrepaie')
      const dto = res.data || {}
      const ok = dto?.result !== 'echec' && dto?.result !== 'error' && dto?.result !== 'failed'
      return {
        success: ok,
        data: dto,
        total: dto?.total ?? (Array.isArray(dto?.rows) ? dto.rows.length : 0),
        message: ok
          ? (dto?.message || 'Bulletins spéciaux enregistrés')
          : (dto?.message || 'Échec de l’enregistrement des bulletins spéciaux')
      }
    } catch (e: any) {
      console.error('Erreur confirmation génération spéciale:', e)
      return {
        success: false,
        data: null,
        message: e?.response?.data?.message || e?.message || 'Erreur de confirmation'
      }
    }
  }

  async exportJulaya(): Promise<Blob | null> {
    try {
      const res = await this.api.get('/paie/bulletin/exportSeciale', {
        responseType: 'blob',
        timeout: 600_000
      })
      return res.data instanceof Blob ? res.data : new Blob([res.data])
    } catch (e) {
      console.error('Erreur export Julaya spécial:', e)
      return null
    }
  }

  async printBulletinPdf(_id: number): Promise<Blob | null> {
    return null
  }

  async saveExcelExport(blob: Blob, filename: string): Promise<boolean> {
    try {
      const formData = new FormData()
      formData.append('file', blob, filename)
      formData.append('filename', filename)

      await this.api.post('/paie/bulletin/save-excel-export', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
        timeout: 600_000
      })
      return true
    } catch (e) {
      console.error('Erreur sauvegarde export Excel spécial:', e)
      return false
    }
  }

  async updatePersonnel(_payload: EditerPersonnelPayload): Promise<ApiResponse<any>> {
    return failureResponse(null, 'Modification agent spécial non disponible depuis ce traitement')
  }
}

export const livrePaieSpecialeService = new LivrePaieSpecialeService()
export const useLivreDePaieSpecialeService = () => livrePaieSpecialeService
export default livrePaieSpecialeService
