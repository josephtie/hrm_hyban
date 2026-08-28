import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authGuard, roleGuard, permissionGuard } from '@/router/guards/auth'
import type { UserRole } from '@/types/auth'

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
      path: '/access-denied',
      name: 'access-denied',
      component: () => import('@/views/AccessDeniedView.vue'),
      meta: { requiresAuth: true }
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
          meta: { title: 'Tableau de bord', affix: true, permissionCodes: ['DASHBOARD_READ'] }
        },
        // Module Paramétrage RH
        {
          path: 'parametrage',
          name: 'parametrage',
          meta: { title: 'Paramétrage RH', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[] },
          children: [
            {
              path: 'organisation',
              name: 'parametrage-organisation',
              component: () => import('@/views/parametrage/OrganisationView.vue'),
              meta: { title: 'Organisation', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'exercices',
              name: 'parametrage-exercices',
              component: () => import('@/views/parametrage/ExerciceView.vue'),
              meta: { 
                title: 'Exercices',
                roles: ['ADMIN'] as UserRole[],
                permissionCodes: ['PARAMETER_READ']
              }
            },
            {
              path: 'banques',
              name: 'parametrage-banques',
              component: () => import('@/views/parametrage/BanqueView.vue'),
              meta: { 
                title: 'Banques',
                roles: ['ADMIN', 'DAF'] as UserRole[],
                permissionCodes: ['PARAMETER_READ']
              }
            },
            {
              path: 'types-sanctions',
              name: 'parametrage-types-sanctions',
              component: () => import('@/views/parametrage/TypeSanctionView.vue'),
              meta: { title: 'Types de Sanctions', roles: ['ADMIN', 'RH'] as UserRole[], permissionCodes: ['SANCTION_READ'] }
            },
            {
              path: 'sanctions',
              name: 'parametrage-sanctions',
              component: () => import('@/views/parametrage/SanctionView.vue'),
              meta: { title: 'Sanctions', roles: ['ADMIN', 'RH'] as UserRole[], permissionCodes: ['SANCTION_READ'] }
            },
            {
              path: 'utilisateurs',
              name: 'parametrage-utilisateurs',
              component: () => import('@/views/parametrage/UtilisateurView.vue'),
              meta: { title: 'Utilisateurs', roles: ['ADMIN'] as UserRole[], permissionCodes: ['USER_READ'] }
            },
            {
              path: 'societe',
              name: 'parametrage-societe',
              component: () => import('@/views/parametrage/SocieteView.vue'),
              meta: { title: 'Société', roles: ['ADMIN'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'comptes-virement',
              name: 'parametrage-comptes-virement',
              component: () => import('@/views/parametrage/ComptesVirementView.vue'),
              meta: { title: 'Comptes Virement', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'periodes',
              name: 'parametrage-periodes',
              component: () => import('@/views/parametrage/PeriodesPaieView.vue'),
              meta: { 
                title: 'Périodes de Paie',
                roles: ['ADMIN', 'DAF'] as UserRole[],
                permissionCodes: ['PARAMETER_READ']
              }
            },
            {
              path: 'referentiels',
              name: 'parametrage-referentiels',
              component: () => import('@/views/parametrage/ReferentielsView.vue'),
              meta: { title: 'Référentiels RH', roles: ['ADMIN', 'RH', 'DAF', 'PTGE'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'roles-permissions',
              name: 'parametrage-roles-permissions',
              component: () => import('@/views/parametrage/RolesPermissionsView.vue'),
              meta: { title: 'Rôles & Permissions', roles: ['ADMIN'] as UserRole[], permissionCodes: ['ROLE_READ'] }
            },
            {
              path: 'audit',
              name: 'parametrage-audit',
              component: () => import('@/views/parametrage/AuditLogView.vue'),
              meta: { title: 'Journal d\'Audit', roles: ['ADMIN'] as UserRole[], permissionCodes: ['ROLE_READ'] }
            }
          ]
        },
        // Module Personnel
        {
          path: '/personnel',
          name: 'personnel',
          component: () => import('@/views/personnel/PersonnelView.vue'),
          meta: { title: 'Personnel', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['EMPLOYEE_READ'] }
        },
        {
          path: '/personnel',
          name: 'personnel-layout',
          component: () => import('@/views/personnel/PersonnelLayoutView.vue'),
          meta: { title: 'Gestion RH', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['EMPLOYEE_READ'] },
          children: [
            {
              path: 'categories',
              name: 'personnel-categories',
              component: () => import('@/views/parametrage/CategoriesView.vue'),
              meta: { title: 'Catégories Professionnelles', roles: ['ADMIN', 'RH', 'DAF', 'PTGE'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'referentiels',
              name: 'personnel-referentiels',
              component: () => import('@/views/parametrage/ReferentielsView.vue'),
              meta: { title: 'Référentiels RH', roles: ['ADMIN', 'RH', 'DAF', 'PTGE'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'fonctions',
              name: 'personnel-fonctions',
              component: () => import('@/views/parametrage/FonctionsView.vue'),
              meta: { title: 'Emplois/Fonctions', roles: ['ADMIN', 'RH'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'sanctions',
              name: 'personnel-sanctions',
              component: () => import('@/views/parametrage/SanctionsView.vue'),
              meta: { title: 'Sanctions Disciplinaires', roles: ['ADMIN', 'RH'] as UserRole[], permissionCodes: ['SANCTION_READ'] }
            },
            {
              path: 'temps-absences',
              name: 'personnel-temps-absences',
              component: () => import('@/views/parametrage/TempsAbsencesView.vue'),
              meta: { title: 'Temps & Absences', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['ABSENCE_READ'] }
            },
            {
              path: 'paie',
              name: 'personnel-paie',
              component: () => import('@/views/parametrage/PaieView.vue'),
              meta: { title: 'Paramétrage Paie', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'contrats',
              name: 'personnel-contrats',
              component: () => import('@/views/personnel/ContratsView.vue'),
              meta: { title: 'Contrats de travail', roles: ['ADMIN', 'RH'] as UserRole[], permissionCodes: ['CONTRACT_READ'] }
            },
            {
              path: 'agents-specifiques',
              name: 'personnel-agents-specifiques',
              component: () => import('@/views/personnel/AgentsSpecifiquesView.vue'),
              meta: { title: 'Agents spécifiques', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['EMPLOYEE_READ'] }
            },
            {
              path: 'wizard',
              name: 'personnel-wizard',
              component: () => import('@/views/personnel/PersonnelWizardView.vue'),
              meta: { title: 'Ajouter un Personnel', roles: ['ADMIN', 'RH'] as UserRole[], permissionCodes: ['EMPLOYEE_CREATE'] }
            },
            {
              path: 'view/:id',
              name: 'personnel-view',
              component: () => import('@/views/personnel/PersonnelDetailView.vue'),
              meta: { title: 'Détails Personnel', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['EMPLOYEE_READ'] }
            }
          ]
        },
        // Module Paie
        {
          path: 'paie',
          name: 'paie',
          meta: { title: 'Module Paie', roles: ['ADMIN', 'DAF', 'RH'] as UserRole[], permissionCodes: ['PAYROLL_READ'] },
          children: [
            {
              path: 'saisie-elements',
              name: 'paie-saisie-elements',
              component: () => import('@/views/paie/SaisieEltPaieView.vue'),
              meta: { title: 'Saisie des éléments', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_CALCULATE'] }
            },
            {
              path: 'livre-paie',
              name: 'paie-livre-paie',
              component: () => import('@/views/paie/LivrepaieView.vue'),
              meta: { title: 'Livre de paie', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'livre-paie-special',
              name: 'paie-livre-paie-special',
              component: () => import('@/views/paie/LivrepaieSpecialeView.vue'),
              meta: { title: 'Livre de paie Special', specialAgents: true, roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'historique-bulletins',
              name: 'paie-historique-bulletins',
              component: () => import('@/views/paie/HistoriqueBulletinsView.vue'),
              meta: { title: 'Historique Bulletins', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'depart-cdd',
              name: 'paie-depart-cdd',
              component: () => import('@/views/paie/DepartCddView.vue'),
              meta: { title: 'Départ CDD', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'rubriques',
              name: 'paie-rubriques',
              component: () => import('@/views/paie/RubriquesView.vue'),
              meta: { title: 'Rubriques', roles: ['ADMIN', 'DAF', 'RH'] as UserRole[], permissionCodes: ['PARAMETER_READ'] }
            },
            {
              path: 'bulletins',
              name: 'paie-bulletins',
              component: () => import('@/views/paie/BulletinsView.vue'),
              meta: { title: 'Bulletins de paie', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'etats',
              name: 'paie-etats',
              component: () => import('@/views/paie/EtatsView.vue'),
              meta: { title: 'États de paie', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_EXPORT'] }
            },
            {
              path: 'prets',
              name: 'paie-prets',
              component: () => import('@/views/paie/PretsView.vue'),
              meta: { title: 'Prêts', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'echeanciers',
              name: 'paie-echeanciers',
              component: () => import('@/views/paie/EcheanciersView.vue'),
              meta: { title: 'Echeanciers', roles: ['ADMIN', 'DAF'] as UserRole[], permissionCodes: ['PAYROLL_READ'] }
            },
            {
              path: 'heures-supplementaires',
              name: 'paie-heures-supplementaires',
              component: () => import('@/views/paie/HeuresSupplementairesView.vue'),
              meta: { title: 'Heures Supplémentaires', roles: ['ADMIN', 'DAF', 'RH'] as UserRole[], permissionCodes: ['HS_READ'] }
            }
          ]
        },
        // Module Reporting
        {
          path: 'reporting',
          name: 'reporting',
          component: () => import('@/views/reporting/ReportingView.vue'),
          meta: { title: 'Reporting & Tableaux de bord', roles: ['ADMIN', 'RH', 'DAF'] as UserRole[], permissionCodes: ['REPORT_READ'] }
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
router.beforeEach(roleGuard)
router.beforeEach(permissionGuard)

export default router
