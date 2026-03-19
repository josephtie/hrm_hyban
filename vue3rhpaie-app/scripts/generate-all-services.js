#!/usr/bin/env node

/**
 * Script pour générer automatiquement tous les services à partir de l'API OpenAPI générée
 * Usage: node scripts/generate-all-services.js
 */

const fs = require('fs')
const path = require('path')

// Lire le fichier api.ts généré
const apiFilePath = path.join(__dirname, '../src/generated/api.ts')
const apiContent = fs.readFileSync(apiFilePath, 'utf8')

// Extraire tous les noms de contrôleurs
const controllerRegex = /export\s+class\s+(\w+ControllerApi)/g
const controllers = []
let match

while ((match = controllerRegex.exec(apiContent)) !== null) {
  controllers.push(match[1])
}

// Filtrer les contrôleurs (exclure ceux de base)
const filteredControllers = controllers.filter(controller => 
  !controller.includes('BaseAPI') && 
  !controller.includes('Api') && 
  controller.endsWith('Api')
)

console.log(`🔍 ${filteredControllers.length} contrôleurs trouvés:`)
filteredControllers.forEach((controller, index) => {
  console.log(`${index + 1}. ${controller}`)
})

// Template pour générer un service
function generateServiceTemplate(controllerName) {
  const entityName = controllerName.replace('ControllerApi', '')
  const serviceName = entityName.toLowerCase()
  const entityNameLower = entityName.toLowerCase()
  
  return `import { ${controllerName} } from '@/generated/api'
import type { ApiResponse } from '@/types/auth'
import type * as GeneratedTypes from '@/generated/api'

// Interface pour compatibilité avec le code existant
export interface ${entityName}Dto {
  // TODO: Adapter selon les champs réels de l'entité
  id?: number
  // Ajouter les autres champs selon la documentation
}

export interface ${entityName}Filter {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

class ${entityName}Service {
  private generatedApi: ${controllerName}

  constructor() {
    this.generatedApi = new ${controllerName}()
  }

  // TODO: Implémenter les méthodes selon les endpoints disponibles
  // Consultez src/generated/docs/${controllerName}.md pour la documentation complète
  
  async getAll(filter?: ${entityName}Filter): Promise<ApiResponse<${entityName}Dto[]>> {
    try {
      // Adapter au endpoint réel (exemple avec pagination)
      const paginationRequest: GeneratedTypes.PaginationRequest = {
        limit: filter?.size ?? 10,
        offset: filter?.page ?? 0,
        search: filter?.search
      }
      
      // Adapter à la méthode réelle du contrôleur
      // const response = await this.generatedApi.get${entityName}List(paginationRequest)
      
      // Template à adapter:
      return {
        success: true,
        data: [],
        total: 0,
        message: '${entityName} retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: [],
        message: error.response?.data?.message || 'Failed to retrieve ${entityNameLower}'
      }
    }
  }

  async getById(id: number): Promise<ApiResponse<${entityName}Dto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.get${entityName}ById({ id })
      
      return {
        success: true,
        data: {} as ${entityName}Dto,
        message: '${entityName} retrieved successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as ${entityName}Dto,
        message: error.response?.data?.message || 'Failed to retrieve ${entityNameLower}'
      }
    }
  }

  async create(data: any): Promise<ApiResponse<${entityName}Dto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.save${entityName}(data)
      
      return {
        success: true,
        data: {} as ${entityName}Dto,
        message: '${entityName} created successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as ${entityName}Dto,
        message: error.response?.data?.message || 'Failed to create ${entityNameLower}'
      }
    }
  }

  async update(id: number, data: any): Promise<ApiResponse<${entityName}Dto>> {
    try {
      // Adapter au endpoint réel
      // const response = await this.generatedApi.update${entityName}({ id, ...data })
      
      return {
        success: true,
        data: {} as ${entityName}Dto,
        message: '${entityName} updated successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: {} as ${entityName}Dto,
        message: error.response?.data?.message || 'Failed to update ${entityNameLower}'
      }
    }
  }

  async delete(id: number): Promise<ApiResponse<void>> {
    try {
      // Adapter au endpoint réel
      // await this.generatedApi.delete${entityName}({ id })
      
      return {
        success: true,
        data: undefined,
        message: '${entityName} deleted successfully'
      }
    } catch (error: any) {
      return {
        success: false,
        data: undefined,
        message: error.response?.data?.message || 'Failed to delete ${entityNameLower}'
      }
    }
  }
}

export const ${serviceName}Service = new ${entityName}Service()
export default ${serviceName}Service
`
}

// Générer tous les services
const servicesDir = path.join(__dirname, '../src/services')

console.log('\n🚀 Génération des services...')

filteredControllers.forEach((controller, index) => {
  const entityName = controller.replace('ControllerApi', '')
  const serviceName = entityName.toLowerCase()
  const fileName = `${serviceName}.service.ts`
  const filePath = path.join(servicesDir, fileName)
  
  // Vérifier si le service existe déjà
  if (fs.existsSync(filePath)) {
    console.log(`⚠️  ${index + 1}. ${fileName} existe déjà - ignoré`)
    return
  }
  
  try {
    const template = generateServiceTemplate(controller)
    fs.writeFileSync(filePath, template)
    console.log(`✅ ${index + 1}. ${fileName} généré`)
  } catch (error) {
    console.error(`❌ Erreur lors de la génération de ${fileName}: ${error.message}`)
  }
})

console.log('\n📋 Services générés avec succès!')
console.log('\n🔧 Étapes suivantes:')
console.log('1. Consultez la documentation dans src/generated/docs/')
console.log('2. Adaptez les méthodes selon les endpoints réels')
console.log('3. Définissez les interfaces selon les types réels')
console.log('4. Testez chaque service avec les vues correspondantes')

// Générer un index des services
const indexContent = `// Services générés automatiquement
// Généré le ${new Date().toISOString()}

${filteredControllers.map((controller, index) => {
  const entityName = controller.replace('ControllerApi', '')
  const serviceName = entityName.toLowerCase()
  return `export { ${serviceName}Service } from './${serviceName}.service'`
}).join('\n')}
`

const indexPath = path.join(servicesDir, 'generated-services.ts')
fs.writeFileSync(indexPath, indexContent)
console.log(`\n📄 Index des services généré: src/services/generated-services.ts`)
