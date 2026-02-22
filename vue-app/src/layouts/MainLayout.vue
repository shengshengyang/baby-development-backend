<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Expand, Fold, SwitchButton, Sunny, Moon } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { useTheme } from '@/composables/useTheme'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()
const { toggleTheme, isDark } = useTheme()

const menuRoutes = computed(() => {
  const routes = router.getRoutes()
  return routes.filter(
    (r) => r.meta?.title && !r.meta?.hidden && r.path.startsWith('/') && !r.path.includes(':')
  )
})

const activeMenu = computed(() => {
  return route.matched[0]?.path || route.path
})

async function handleLogout() {
  await userStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="layout-container theme-transition">
    <!-- Sidebar -->
    <aside
      :class="['sidebar theme-transition', { 'sidebar-collapsed': appStore.sidebarCollapsed }]"
    >
      <!-- Logo Section -->
      <div class="sidebar-header">
        <div class="logo-container">
          <div class="logo-icon">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <circle cx="16" cy="16" r="12" fill="currentColor"/>
              <circle cx="16" cy="14" r="5" fill="white"/>
              <circle cx="13" cy="13" r="1.5" fill="white"/>
              <circle cx="19" cy="13" r="1.5" fill="white"/>
              <path d="M14 16c0 1.1.9 2 2 2s2-.9 2-2" stroke="white" stroke-width="1.5" stroke-linecap="round"/>
            </svg>
          </div>
          <transition name="fade">
            <div v-if="!appStore.sidebarCollapsed" class="logo-text">
              <span class="logo-title">寶寶發展</span>
              <span class="logo-subtitle">管理平台</span>
            </div>
          </transition>
        </div>
      </div>

      <!-- Navigation Menu -->
      <nav class="sidebar-nav">
        <el-menu
          :default-active="activeMenu"
          :collapse="appStore.sidebarCollapsed"
          :router="true"
          class="sidebar-menu"
        >
          <template v-for="menuRoute in menuRoutes" :key="menuRoute.path">
            <el-menu-item :index="menuRoute.path" :route="menuRoute.path">
              <template #title>
                <span class="menu-text">{{ menuRoute.meta?.title }}</span>
              </template>
              <el-icon v-if="menuRoute.meta?.icon" class="menu-icon">
                <component :is="menuRoute.meta.icon" />
              </el-icon>
            </el-menu-item>
          </template>
        </el-menu>
      </nav>

      <!-- Sidebar Footer -->
      <div class="sidebar-footer">
        <div class="footer-decoration"></div>
      </div>
    </aside>

    <!-- Main Content Area -->
    <div class="main-wrapper">
      <!-- Header -->
      <header class="header theme-transition">
        <div class="header-left">
          <el-button
            :icon="appStore.sidebarCollapsed ? Expand : Fold"
            circle
            class="collapse-button"
            @click="appStore.toggleSidebar()"
          />
        </div>

        <div class="header-right">
          <!-- Theme Toggle -->
          <el-button
            :icon="isDark ? Sunny : Moon"
            circle
            class="theme-button"
            @click="toggleTheme"
          />

          <!-- User Dropdown -->
          <el-dropdown trigger="click" class="user-dropdown">
            <div class="user-info">
              <el-avatar :size="36" :icon="'User'" />
              <span class="username">{{ userStore.user?.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout" class="logout-item">
                  <el-icon><SwitchButton /></el-icon>
                  <span>登出</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- Page Content -->
      <main class="main-content theme-transition">
        <router-view v-slot="{ Component }">
          <transition name="page" mode="out-in">
            <component :is="Component" :key="route.path" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<style scoped>
/* Layout Container */
.layout-container {
  display: flex;
  min-height: 100vh;
  background: var(--color-bg-primary);
}

/* ============================================
   SIDEBAR
   ============================================ */
.sidebar {
  width: 260px;
  background: var(--color-bg-elevated);
  border-right: 1px solid var(--color-border-subtle);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;
  box-shadow: var(--shadow-md);
}

[data-theme="dark"] .sidebar {
  box-shadow: var(--shadow-lg);
}

.sidebar-collapsed {
  width: 72px;
}

/* Sidebar Header */
.sidebar-header {
  height: 72px;
  padding: 20px;
  border-bottom: 1px solid var(--color-border-subtle);
  display: flex;
  align-items: center;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
}

.logo-icon {
  width: 40px;
  height: 40px;
  min-width: 40px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-secondary));
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-base);
}

.logo-icon:hover {
  transform: scale(1.05);
}

.logo-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.logo-title {
  font-family: var(--font-display);
  font-size: 0.9375rem;
  font-weight: 700;
  color: var(--color-text-primary);
  line-height: 1.2;
}

.logo-subtitle {
  font-size: 0.6875rem;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

/* Sidebar Navigation */
.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 16px 12px;
}

.sidebar-nav::-webkit-scrollbar {
  width: 4px;
}

.sidebar-menu {
  border-right: none;
  background: transparent;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 100%;
}

.sidebar-menu .el-menu-item {
  height: 44px;
  margin-bottom: 4px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  padding: 0 16px !important;
  gap: 12px;
  color: var(--color-text-secondary);
  transition: all var(--transition-base);
}

.sidebar-menu .el-menu-item:hover {
  background: var(--color-bg-secondary);
  color: var(--color-text-primary);
}

.sidebar-menu .el-menu-item.is-active {
  background: var(--color-primary-soft);
  color: var(--color-primary);
  font-weight: 600;
}

.sidebar-menu .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 24px;
  background: var(--color-primary);
  border-radius: 0 2px 2px 0;
}

.sidebar-collapsed .sidebar-menu .el-menu-item {
  padding: 0 !important;
  justify-content: center;
}

.menu-icon {
  width: 20px;
  height: 20px;
}

.menu-text {
  font-size: 0.875rem;
  font-weight: 500;
}

/* Sidebar Footer */
.sidebar-footer {
  padding: 16px;
  border-top: 1px solid var(--color-border-subtle);
}

.footer-decoration {
  height: 4px;
  background: linear-gradient(90deg, var(--color-primary), var(--color-secondary));
  border-radius: var(--radius-full);
  opacity: 0.6;
}

/* ============================================
   MAIN WRAPPER
   ============================================ */
.main-wrapper {
  flex: 1;
  margin-left: 260px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left var(--transition-base);
}

.sidebar-collapsed + .main-wrapper {
  margin-left: 72px;
}

/* ============================================
   HEADER
   ============================================ */
.header {
  height: 72px;
  background: var(--color-bg-elevated);
  border-bottom: 1px solid var(--color-border-subtle);
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 50;
  backdrop-filter: blur(8px);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

[data-theme="dark"] .header {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-button {
  border: 1px solid var(--color-border-default);
  background: var(--color-bg-secondary);
  color: var(--color-text-secondary);
  transition: all var(--transition-base);
}

.collapse-button:hover {
  background: var(--color-bg-tertiary);
  color: var(--color-text-primary);
  border-color: var(--color-border-strong);
}

.theme-button {
  border: 1px solid var(--color-border-default);
  background: var(--color-bg-secondary);
  color: var(--color-text-secondary);
  transition: all var(--transition-base);
}

.theme-button:hover {
  background: var(--color-bg-tertiary);
  color: var(--color-text-primary);
  border-color: var(--color-border-strong);
  transform: rotate(15deg);
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  border-radius: var(--radius-full);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border-subtle);
  transition: all var(--transition-base);
}

.user-info:hover {
  background: var(--color-bg-tertiary);
  border-color: var(--color-border-default);
}

.username {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-text-primary);
}

/* ============================================
   MAIN CONTENT
   ============================================ */
.main-content {
  flex: 1;
  padding: 32px;
  min-height: calc(100vh - 72px);
}

/* ============================================
   ANIMATIONS
   ============================================ */
/* Fade transition for logo text */
.fade-enter-active,
.fade-leave-active {
  transition: opacity var(--transition-base);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Page transition */
.page-enter-active,
.page-leave-active {
  transition: all var(--transition-base);
}

.page-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* ============================================
   RESPONSIVE
   ============================================ */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    transition: transform var(--transition-base);
  }

  .sidebar.mobile-open {
    transform: translateX(0);
  }

  .main-wrapper {
    margin-left: 0;
  }

  .sidebar-collapsed + .main-wrapper {
    margin-left: 0;
  }

  .header {
    padding: 0 16px;
  }

  .main-content {
    padding: 20px 16px;
  }

  .username {
    display: none;
  }
}
</style>
