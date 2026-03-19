import { defineStore } from 'pinia'

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

  // Ajouter une vue visitée
  const addView = (view: TagView) => {
    addVisitedView(view)
    if (view.name) {
      addCachedView(view)
    delAllCachedViews() {
      return new Promise(resolve => {
        this.cachedViews = []
        resolve([...this.cachedViews])
      })
    },

    delOthersCachedViews(view: TagView) {
      return new Promise(resolve => {
        this.cachedViews = this.cachedViews.filter(name => name === view.name)
        resolve([...this.cachedViews])
      })
    }
  }
})
