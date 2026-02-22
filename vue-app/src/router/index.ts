import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '儀表板', icon: 'Odometer' },
      },
      {
        path: '/milestones',
        name: 'Milestones',
        component: () => import('@/views/Milestone/List.vue'),
        meta: { title: '里程碑管理', icon: 'Trophy' },
      },
      {
        path: '/milestones/create',
        name: 'MilestoneCreate',
        component: () => import('@/views/Milestone/Form.vue'),
        meta: { title: '新增里程碑', hidden: true },
      },
      {
        path: '/milestones/:id/edit',
        name: 'MilestoneEdit',
        component: () => import('@/views/Milestone/Form.vue'),
        meta: { title: '編輯里程碑', hidden: true },
      },
      {
        path: '/categories',
        name: 'Categories',
        component: () => import('@/views/Category/List.vue'),
        meta: { title: '分類管理', icon: 'FolderOpened' },
      },
      {
        path: '/categories/create',
        name: 'CategoryCreate',
        component: () => import('@/views/Category/Form.vue'),
        meta: { title: '新增分類', hidden: true },
      },
      {
        path: '/categories/:id/edit',
        name: 'CategoryEdit',
        component: () => import('@/views/Category/Form.vue'),
        meta: { title: '編輯分類', hidden: true },
      },
      {
        path: '/ages',
        name: 'Ages',
        component: () => import('@/views/Age/List.vue'),
        meta: { title: '年齡管理', icon: 'Timer' },
      },
      {
        path: '/ages/create',
        name: 'AgeCreate',
        component: () => import('@/views/Age/Form.vue'),
        meta: { title: '新增年齡段', hidden: true },
      },
      {
        path: '/ages/:id/edit',
        name: 'AgeEdit',
        component: () => import('@/views/Age/Form.vue'),
        meta: { title: '編輯年齡段', hidden: true },
      },
      {
        path: '/articles',
        name: 'Articles',
        component: () => import('@/views/Article/List.vue'),
        meta: { title: '文章管理', icon: 'Document' },
      },
      {
        path: '/articles/create',
        name: 'ArticleCreate',
        component: () => import('@/views/Article/Form.vue'),
        meta: { title: '新增文章', hidden: true },
      },
      {
        path: '/articles/:id/edit',
        name: 'ArticleEdit',
        component: () => import('@/views/Article/Form.vue'),
        meta: { title: '編輯文章', hidden: true },
      },
      {
        path: '/flashcards',
        name: 'FlashCards',
        component: () => import('@/views/FlashCard/List.vue'),
        meta: { title: '閃卡管理', icon: 'Memo' },
      },
      {
        path: '/flashcards/create',
        name: 'FlashCardCreate',
        component: () => import('@/views/FlashCard/Form.vue'),
        meta: { title: '新增閃卡', hidden: true },
      },
      {
        path: '/flashcards/:id/edit',
        name: 'FlashCardEdit',
        component: () => import('@/views/FlashCard/Form.vue'),
        meta: { title: '編輯閃卡', hidden: true },
      },
      {
        path: '/videos',
        name: 'Videos',
        component: () => import('@/views/Video/List.vue'),
        meta: { title: '影片管理', icon: 'VideoCamera' },
      },
      {
        path: '/videos/create',
        name: 'VideoCreate',
        component: () => import('@/views/Video/Form.vue'),
        meta: { title: '新增影片', hidden: true },
      },
      {
        path: '/videos/:id/edit',
        name: 'VideoEdit',
        component: () => import('@/views/Video/Form.vue'),
        meta: { title: '編輯影片', hidden: true },
      },
      {
        path: '/babies',
        name: 'Babies',
        component: () => import('@/views/Baby/List.vue'),
        meta: { title: '寶寶管理', icon: 'BabyCarriage' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守衛
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth !== false && !userStore.isAuthenticated()) {
    next('/login')
  } else if (to.path === '/login' && userStore.isAuthenticated()) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
