import { api } from './api'

export interface PrintLs {
  s1?: string
  s2?: string
  i1?: number
  i2?: number
  i3?: number
  title1?: string
  title2?: string
  value1?: number
  value2?: number
}

export interface ReportingKpis {
  effectifTotal: number
  effectifHommes: number
  effectifFemmes: number
  masseSalariale: number
  contractuels: number
  stagiaires: number
  fonctionnaires: number
  consultants: number
  periodeActive: number | null
}

class ReportingService {
  private readonly baseUrl = '/reporting'

  async getKpis(): Promise<ReportingKpis> {
    const response = await api.get<ReportingKpis>(`${this.baseUrl}/kpis`)
    return response.data
  }

  async getEffectifAnnuel(exerciceId?: number): Promise<PrintLs[]> {
    const params = exerciceId ? { exerciceId } : {}
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/effectif-annuel`, { params })
    return response.data
  }

  async getEffectifParSite(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/effectif-par-site`)
    return response.data
  }

  async getMasseSalarialeParTypeContrat(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/masse-salariale-par-type-contrat`)
    return response.data
  }

  async getMasseSalarialeParSite(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/masse-salariale-par-site`)
    return response.data
  }

  async getEffectifMasseParSite(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/effectif-masse-par-site`)
    return response.data
  }

  async getCongeStat(exerciceId?: number): Promise<PrintLs[]> {
    const params = exerciceId ? { exerciceId } : {}
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/conge-stat`, { params })
    return response.data
  }

  async getRetraiteStat(exerciceId?: number): Promise<PrintLs[]> {
    const params = exerciceId ? { exerciceId } : {}
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/retraite-stat`, { params })
    return response.data
  }

  async getTypeContratStat(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/type-contrat-stat`)
    return response.data
  }

  async getPyramideAges(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/pyramide-ages`)
    return response.data
  }

  async getAnciennete(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/anciennete`)
    return response.data
  }

  async getEffectifMensuel(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/effectif-mensuel`)
    return response.data
  }

  async getNetPayeEvolution(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/net-paye-evolution`)
    return response.data
  }

  async getBrutNetCharges(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/brut-net-charges`)
    return response.data
  }

  async getRepartitionPrimes(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/repartition-primes`)
    return response.data
  }

  async getAnalyseRetenues(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/analyse-retenues`)
    return response.data
  }

  async getMasseSalarialeEvolution(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/masse-salariale-evolution`)
    return response.data
  }

  async getCoutsPersonnel(): Promise<PrintLs[]> {
    const response = await api.get<PrintLs[]>(`${this.baseUrl}/couts-personnel`)
    return response.data
  }
}

export const reportingService = new ReportingService()
