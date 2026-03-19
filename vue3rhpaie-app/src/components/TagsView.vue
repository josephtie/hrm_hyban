<template>
  <div class="tags-view-container">
    <div class="tags-view-wrapper">
      <el-scrollbar class="tags-view-scrollbar">
        <div class="tags-view">
          <router-link
            v-for="tag in visitedViews"
            ref="tag"
            :key="tag.path"
            :class="isActive(tag) ? 'active' : ''"
            :to="{ path: tag.path, query: tag.query }"
            tag="span"
            class="tags-view-item"
            @click.middle="tag && !isAffix(tag) ? closeSelectedTag(tag) : ''"
            @contextmenu.prevent="tag && openMenu(tag, $event)"
          >
            {{ tag.title }}
            <el-icon
              v-if="tag && !isAffix(tag)"
              class="close-icon"
              @click.prevent.stop="closeSelectedTag(tag)"
            >
              <Close />
            </el-icon>
          </router-link>
        </div>
      </el-scrollbar>
    </div>
    
    <!-- Menu contextuel -->
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">
        <el-icon><Refresh /></el-icon>
        Rafraîchir
      </li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
        <el-icon><Close /></el-icon>
        Fermer
      </li>
      <li @click="closeOthersTags">
        <el-icon><CircleClose /></el-icon>
        Fermer les autres
      </li>
      <li @click="closeAllTags(selectedTag)">
        <el-icon><FolderDelete /></el-icon>
        Fermer tout
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Close, Refresh, CircleClose, FolderDelete } from '@element-plus/icons-vue'
import { useTagsViewStore } from '@/stores/tagsView'

// Simple path resolution function
const resolvePath = (basePath: string, relativePath: string): string => {
  if (relativePath.startsWith('/')) {
    return relativePath
  }
  return basePath.replace(/\/$/, '') + '/' + relativePath.replace(/^\//, '')
}

interface TagView {
  path: string
  name: string
  fullPath: string
  title: string
  query?: any
  affix?: boolean
}

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()

const visible = ref(false)
const top = ref(0)
const left = ref(0)
const selectedTag = ref<TagView | null>(null)
const affixTags = ref<TagView[]>([])

const visitedViews = computed(() => tagsViewStore.visitedViews)
const routes = computed(() => router.getRoutes())

const isActive = (tag: TagView | null) => {
  return tag?.path === route.path
}

const isAffix = (tag: TagView | null) => {
  return tag?.affix || false
}

const filterAffixTags = (routes: any[], basePath = '/') => {
  let tags: TagView[] = []
  
  routes.forEach(route => {
    if (route.meta && route.meta.affix) {
      const tagPath = resolvePath(basePath, route.path)
      tags.push({
        fullPath: tagPath,
        path: tagPath,
        name: route.name as string,
        title: (route.meta?.title as string) || 'no-name',
        affix: true
      })
    }
    
    if (route.children) {
      const tempTags = filterAffixTags(route.children, route.path)
      if (tempTags.length >= 1) {
        tags = [...tags, ...tempTags]
      }
    }
  })
  
  return tags
}

const initTags = () => {
  const affixTags = filterAffixTags(routes.value)
  for (const tag of affixTags) {
    // Must have tag name
    if (tag.name) {
      tagsViewStore.addVisitedView(tag)
    }
  }
}

const addTags = () => {
  const { name, meta } = route
  if (name && meta?.title) {
    tagsViewStore.addView({
      path: route.path,
      name: name as string,
      fullPath: route.fullPath,
      title: meta.title as string,
      query: route.query
    })
  }
  return false
}

const moveToCurrentTag = () => {
  const tags = document.querySelectorAll('.tags-view-item')
  nextTick(() => {
    for (const tag of tags) {
      if ((tag as any).to.path === route.path) {
        // TODO: scroll to tag
        break
      }
    }
  })
}

const refreshSelectedTag = (view: TagView | null) => {
  if (!view) return
  tagsViewStore.delCachedView(view).then(() => {
    const { fullPath } = view
    nextTick(() => {
      router.replace({
        path: '/redirect' + fullPath
      })
    })
  })
}

const closeSelectedTag = (view: TagView | null) => {
  if (!view) return
  tagsViewStore.delView(view).then(({ visitedViews }: any) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}

const closeOthersTags = () => {
  if (!selectedTag.value) return
  router.push(selectedTag.value.path)
  tagsViewStore.delOthersViews(selectedTag.value).then(() => {
    moveToCurrentTag()
  })
}

const closeAllTags = (view: TagView | null) => {
  if (!view) return
  tagsViewStore.delAllViews().then(({ visitedViews }: any) => {
    if (affixTags.value.some(tag => tag.path === view.path)) {
      return
    }
    toLastView(visitedViews, view)
  })
}

const toLastView = (visitedViews: TagView[], view: TagView) => {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView && latestView.fullPath !== view.fullPath) {
    router.push(latestView.fullPath)
  } else {
    // now the default is to redirect to the home page if there is no tags-view,
    // you can adjust it according to your needs.
    if (view.name === 'Dashboard') {
      // to reload home page
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}

const openMenu = (tag: TagView | null, e: MouseEvent) => {
  if (!tag) return
  
  const menuMinWidth = 105
  const offsetLeft = (e.target as HTMLElement).getBoundingClientRect().left // container margin left
  const offsetWidth = (e.target as HTMLElement).offsetWidth // container width
  const maxLeft = offsetLeft + offsetWidth - menuMinWidth // left boundary
  const left_ = e.clientX - offsetLeft + 15 // 15: margin right

  if (left_ > maxLeft) {
    left.value = maxLeft
  } else {
    left.value = left_
  }

  top.value = e.clientY
  visible.value = true
  selectedTag.value = tag
}

const closeMenu = () => {
  visible.value = false
}

const handleScroll = () => {
  closeMenu()
}

watch(route, () => {
  addTags()
  moveToCurrentTag()
})

watch(visible, (value) => {
  if (value) {
    document.body.addEventListener('click', closeMenu)
  } else {
    document.body.removeEventListener('click', closeMenu)
  }
})

onMounted(() => {
  initTags()
  addTags()
})
</script>

<style scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
  
  .tags-view-wrapper {
    .tags-view-scrollbar {
      height: 34px;
      
      .tags-view {
        display: inline-block;
        height: 34px;
        line-height: 34px;
        vertical-align: top;
        padding-left: 15px;
        
        .tags-view-item {
          display: inline-block;
          position: relative;
          cursor: pointer;
          height: 26px;
          line-height: 26px;
          border: 1px solid #d8dce5;
          color: #495060;
          background: #fff;
          padding: 0 8px;
          font-size: 12px;
          margin-left: 5px;
          margin-top: 4px;
          border-radius: 2px;
          text-decoration: none;
          
          &:first-of-type {
            margin-left: 15px;
          }
          
          &:last-of-type {
            margin-right: 15px;
          }
          
          &.active {
            background-color: #42b983;
            color: #fff;
            border-color: #42b983;
            
            &::before {
              content: '';
              background: #fff;
              display: inline-block;
              width: 8px;
              height: 8px;
              border-radius: 50%;
              position: relative;
              margin-right: 2px;
            }
          }
          
          .close-icon {
            width: 16px;
            height: 16px;
            vertical-align: 2px;
            border-radius: 50%;
            text-align: center;
            transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
            transform-origin: 100% 50%;
            
            &:before {
              transform: scale(0.6);
              display: inline-block;
              vertical-align: -3px;
            }
            
            &:hover {
              background-color: #b4bccc;
              color: #fff;
            }
          }
        }
      }
    }
  }
  
  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
    
    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      
      &:hover {
        background: #eee;
      }
      
      .el-icon {
        font-size: 14px;
      }
    }
  }
}
</style>
