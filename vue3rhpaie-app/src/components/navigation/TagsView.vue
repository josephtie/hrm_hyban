<template>
  <div class="tags-view-container">
    <el-scrollbar class="tags-view-scrollbar">
      <div class="tags-view">
        <router-link
          v-for="tag in visitedViews"
          :key="tag.path"
          :to="{ path: tag.path, query: tag.query }"
          :class="['tags-view-item', { active: isActive(tag) }]"
        >
          {{ tag.title }}
          <el-icon
            v-if="!tag.affix"
            class="close-icon"
            @click.prevent.stop="closeSelectedTag(tag)"
          >
            <Close />
          </el-icon>
        </router-link>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Close } from '@element-plus/icons-vue'
import { useTagsViewStore } from '@/stores/tagsViewNew'

interface TagView {
  path: string
  name: string
  title: string
  query?: Record<string, any>
  affix?: boolean
}

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()

const visitedViews = computed(() => tagsViewStore.visitedViews)

const isActive = (tag: TagView) => {
  return tag.path === route.path
}

const closeSelectedTag = (tag: TagView) => {
  tagsViewStore.delView(tag)
  if (isActive(tag)) {
    toLastView()
  }
}

const toLastView = () => {
  const lastView = visitedViews.value.slice(-1)[0]
  if (lastView) {
    router.push(lastView.path)
  } else {
    router.push('/')
  }
}

// Ajouter automatiquement la route actuelle
const addTags = () => {
  const { name, meta } = route
  if (name && meta?.title) {
    tagsViewStore.addView({
      path: route.path,
      name: name as string,
      title: meta.title as string,
      query: route.query,
      affix: meta.affix as boolean
    })
  }
}

// Surveiller les changements de route
watch(
  () => route.path,
  () => {
    addTags()
  }
)

onMounted(() => {
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
}

.tags-view-scrollbar {
  height: 34px;
}

.tags-view {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 10px;
}

.tags-view-item {
  display: inline-flex;
  align-items: center;
  padding: 0 8px;
  margin-right: 4px;
  height: 26px;
  line-height: 26px;
  border: 1px solid #d8dce5;
  border-radius: 4px;
  background: #fff;
  color: #495057;
  font-size: 12px;
  text-decoration: none;
  transition: all 0.3s;
  cursor: pointer;
}

.tags-view-item:hover {
  background: #f5f7fa;
}

.tags-view-item.active {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

.close-icon {
  margin-left: 4px;
  font-size: 12px;
  cursor: pointer;
  border-radius: 50%;
  padding: 2px;
}

.close-icon:hover {
  background: rgba(255, 255, 255, 0.2);
}
</style>
