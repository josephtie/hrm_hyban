import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// Import du système de design personnalisé
import '@/styles/design-system.scss'

// Import de l'initialisation Keycloak
import initializeKeycloak from '@/plugins/keycloak'

// Import du diagnostic de configuration
// import '@/utils/debug-config'
// Import du test d'authentification
// import '@/utils/test-auth'

import App from './App.vue'
import router from './router'

const app = createApp(App)

// Enregistrer tous les icônes Element Plus
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// Initialiser Keycloak avant de monter l'app
initializeKeycloak().then(() => {
  app.mount('#app')
}).catch(error => {
  console.error('Erreur critique lors de l\'initialisation de Keycloak:', error)
  // Monter l'app même si Keycloak échoue
  app.mount('#app')
})
