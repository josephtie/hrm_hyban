import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authGuard, roleGuard, permissionGuard } from '@/router/guards/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      name: 'layout',
      component: () => import('@/views/layout/LayoutViewNew.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('@/views/DashboardView.vue'),
          meta: { title: 'Tableau de bord', affix: true }
        },
        // Module Paramétrage RH
        {
          path: 'parametrage',
          name: 'parametrage',
          meta: { title: 'Paramétrage RH' },
          children: [
            {
              path: 'organisation',
              name: 'parametrage-organisation',
              component: () => import('@/views/parametrage/OrganisationView.vue'),
              meta: { title: 'Organisation' }
            },
            {
              path: 'exercices',
              name: 'parametrage-exercices',
              component: () => import('@/views/parametrage/ExerciceView.vue'),
              meta: { 
                title: 'Exercices',
                permissions: [
                  { resource: 'organisation', action: 'write' }
                ]
              }
            },
            {
              path: 'banques',
              name: 'parametrage-banques',
              component: () => import('@/views/parametrage/BanqueView.vue'),
              meta: { 
                title: 'Banques',
                permissions: [
                  { resource: 'organisation', action: 'write' }
                ]
              }
            },
            {
              path: 'types-sanctions',
              name: 'parametrage-types-sanctions',
              component: () => import('@/views/parametrage/TypeSanctionView.vue'),
              meta: { title: 'Types de Sanctions' }
            },
            {
              path: 'sanctions',
              name: 'parametrage-sanctions',
              component: () => import('@/views/parametrage/SanctionView.vue'),
              meta: { title: 'Sanctions' }
            },
            {
              path: 'utilisateurs',
              name: 'parametrage-utilisateurs',
              component: () => import('@/views/parametrage/UtilisateurView.vue'),
              meta: { title: 'Utilisateurs' }
            },
            {
              path: 'societe',
              name: 'parametrage-societe',
              component: () => import('@/views/parametrage/SocieteView.vue'),
              meta: { title: 'Société' }
            },
            {
              path: 'comptes-virement',
              name: 'parametrage-comptes-virement',
              component: () => import('@/views/parametrage/ComptesVirementView.vue'),
              meta: { title: 'Comptes Virement' }
            },
            {
              path: 'periodes',
              name: 'parametrage-periodes',
              component: () => import('@/views/parametrage/PeriodesPaieView.vue'),
              meta: { 
                title: 'Périodes de Paie',
                permissions: [
                  { resource: 'organisation', action: 'write' }
                ]
              }
            },
            {
              path: 'referentiels',
              name: 'parametrage-referentiels',
              component: () => import('@/views/parametrage/ReferentielsView.vue'),
              meta: { title: 'Référentiels RH' }
            }
          ]
        },
        // Module Personnel
        {
          path: '/personnel',
          name: 'personnel',
          component: () => import('@/views/personnel/PersonnelView.vue'),
          meta: { title: 'Personnel' }
        },
        {
          path: '/personnel',
          name: 'personnel-layout',
          component: () => import('@/views/personnel/PersonnelLayoutView.vue'),
          meta: { title: 'Gestion RH' },
          children: [
            {
              path: 'categories',
              name: 'personnel-categories',
              component: () => import('@/views/parametrage/CategoriesView.vue'),
              meta: { title: 'Catégories Professionnelles' }
            },
            {
              path: 'referentiels',
              name: 'personnel-referentiels',
              component: () => import('@/views/parametrage/ReferentielsView.vue'),
              meta: { title: 'Référentiels RH' }
            },
            {
              path: 'fonctions',
              name: 'personnel-fonctions',
              component: () => import('@/views/parametrage/FonctionsView.vue'),
              meta: { title: 'Emplois/Fonctions' }
            },
            {
              path: 'sanctions',
              name: 'personnel-sanctions',
              component: () => import('@/views/parametrage/SanctionsView.vue'),
              meta: { title: 'Sanctions Disciplinaires' }
            },
            {
              path: 'temps-absences',
              name: 'personnel-temps-absences',
              component: () => import('@/views/parametrage/TempsAbsencesView.vue'),
              meta: { title: 'Temps & Absences' }
            },
            {
              path: 'paie',
              name: 'personnel-paie',
              component: () => import('@/views/parametrage/PaieView.vue'),
              meta: { title: 'Paramétrage Paie' }
            },
            {
              path: 'contrats',
              name: 'personnel-contrats',
              component: () => import('@/views/personnel/ContratsView.vue'),
              meta: { title: 'Contrats de travail' }
            },
            {
              path: 'wizard',
              name: 'personnel-wizard',
              component: () => import('@/views/personnel/PersonnelWizardView.vue'),
              meta: { title: 'Ajouter un Personnel' }
            },
            {
              path: 'view/:id',
              name: 'personnel-view',
              component: () => import('@/views/personnel/PersonnelDetailView.vue'),
              meta: { title: 'Détails Personnel' }
            }
          ]
        },
        // Module Paie
        {
          path: 'paie',
          name: 'paie',
          meta: { title: 'Module Paie' },
          children: [
            {
              path: 'saisie-elements',
              name: 'paie-saisie-elements',
              component: () => import('@/views/paie/SaisieEltPaieView.vue'),
              meta: { title: 'Saisie des éléments' }
            },
            {
              path: 'livre-paie',
              name: 'paie-livre-paie',
              component: () => import('@/views/paie/LivrepaieView.vue'),
              meta: { title: 'Livre de paie' }
            },
            {
              path: 'historique-bulletins',
              name: 'paie-historique-bulletins',
              component: () => import('@/views/paie/HistoriqueBulletinsView.vue'),
              meta: { title: 'Historique Bulletins' }
            },
            {
              path: 'depart-cdd',
              name: 'paie-depart-cdd',
              component: () => import('@/views/paie/DepartCddView.vue'),
              meta: { title: 'Départ CDD' }
            },
            {
              path: 'rubriques',
              name: 'paie-rubriques',
              component: () => import('@/views/paie/RubriquesView.vue'),
              meta: { title: 'Rubriques' }
            },
            {
              path: 'bulletins',
              name: 'paie-bulletins',
              component: () => import('@/views/paie/BulletinsView.vue'),
              meta: { title: 'Bulletins de paie' }
            },
            {
              path: 'etats',
              name: 'paie-etats',
              component: () => import('@/views/paie/EtatsView.vue'),
              meta: { title: 'États de paie' }
            },
            {
              path: 'prets',
              name: 'paie-prets',
              component: () => import('@/views/paie/PretsView.vue'),
              meta: { title: 'Prêts' }
            },
            {
              path: 'echeanciers',
              name: 'paie-echeanciers',
              component: () => import('@/views/paie/EcheanciersView.vue'),
              meta: { title: 'Echeanciers' }
            }
          ]
        },
        // Module Reporting
        {
          path: 'reporting',
          name: 'reporting',
          component: () => import('@/views/reporting/ReportingView.vue'),
          meta: { title: 'Reporting & Tableaux de bord' }
        }
      ]
    },
    // Page 404
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFoundView.vue')
    }
  ]
})

// Guards de navigation
router.beforeEach(authGuard)
router.beforeEach(permissionGuard)

export default router
