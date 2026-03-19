import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface TagView {
  path: string
  name: string
  title: string
  query?: Record<string, any>
  affix?: boolean
}

export const useTagsViewStore = defineStore('tagsView', () => {
  const visitedViews = ref<TagView[]>([])
  const cachedViews = ref<string[]>([])
  const MAX_TAGS = 8

  // Trouver et supprimer le plus ancien tag non-affix
  const removeOldestNonAffixTag = () => {
    const oldestNonAffixIndex = visitedViews.value.findIndex((v: TagView) => !v.affix)
    if (oldestNonAffixIndex > -1) {
      const removedView = visitedViews.value[oldestNonAffixIndex]
      visitedViews.value.splice(oldestNonAffixIndex, 1)
      // Supprimer également du cache si nécessaire
      if (removedView.name && !removedView.affix) {
        const cacheIndex = cachedViews.value.indexOf(removedView.name)
        if (cacheIndex > -1) {
          cachedViews.value.splice(cacheIndex, 1)
        }
      }
    }
  }

  // Ajouter une vue visitée
  const addView = (view: TagView) => {
    addVisitedView(view)
    if (view.name) {
      addCachedView(view)
    }
  }

  // Ajouter aux vues visitées
  const addVisitedView = (view: TagView) => {
    if (visitedViews.value.some((v: TagView) => v.path === view.path)) return
    
    // Si on a déjà atteint la limite maximale de tags, supprimer le plus ancien non-affix
    if (visitedViews.value.length >= MAX_TAGS) {
      removeOldestNonAffixTag()
    }
    
    visitedViews.value.push({
      path: view.path,
      name: view.name,
      title: view.title || 'no-name',
      query: view.query,
      affix: view.affix || false
    })
  }

  // Ajouter aux vues en cache
  const addCachedView = (view: TagView) => {
    if (cachedViews.value.includes(view.name)) return
    if (view.affix) return
    
    // Si on a déjà atteint la limite maximale de vues en cache, supprimer la plus ancienne
    if (cachedViews.value.length >= MAX_TAGS) {
      cachedViews.value.shift() // Supprime le premier élément (le plus ancien)
    }
    
    cachedViews.value.push(view.name)
  }

  // Supprimer une vue
  const delView = (view: TagView) => {
    delVisitedView(view)
    delCachedView(view)
  }

  // Supprimer des vues visitées
  const delVisitedView = (view: TagView) => {
    const index = visitedViews.value.findIndex((v: TagView) => v.path === view.path)
    if (index > -1) {
      visitedViews.value.splice(index, 1)
    }
  }

  // Supprimer des vues en cache
  const delCachedView = (view: TagView) => {
    const index = cachedViews.value.indexOf(view.name)
    if (index > -1) {
      cachedViews.value.splice(index, 1)
    }
  }

  // Supprimer toutes les vues
  const delAllViews = () => {
    visitedViews.value = visitedViews.value.filter((v: TagView) => v.affix)
    cachedViews.value = []
  }

  return {
    visitedViews,
    cachedViews,
    addView,
    delView,
    delAllViews
  }
})
