<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { dashboardApi } from '@/api'
import type { DashboardStats } from '@/types'

const loading = ref(false)
const stats = ref<DashboardStats>({
  totalMilestones: 0,
  totalFlashCards: 0,
  totalArticles: 0,
  totalCategories: 0,
  totalAges: 0,
})

onMounted(async () => {
  await loadStats()
})

async function loadStats() {
  loading.value = true
  try {
    const data = await dashboardApi.getStats()
    stats.value = {
      totalMilestones: data.milestoneCount || 0,
      totalFlashCards: data.flashcardCount || 0,
      totalArticles: data.articleCount || 0,
      totalCategories: 0,
      totalAges: 0,
    }
  } catch (error) {
    console.error('Failed to load dashboard stats:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="dashboard animate-fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">儀表板</h1>
        <p class="page-subtitle">歡迎回來！這是您的寶寶發展管理平台概覽</p>
      </div>
    </div>

    <!-- Stats Grid -->
    <el-row :gutter="24" v-loading="loading" class="stats-grid">
      <el-col :xs="24" :sm="12" :lg="8" :xl="6">
        <div class="stat-card milestone-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <circle cx="16" cy="16" r="14" fill="currentColor" opacity="0.2"/>
              <path d="M16 6C10.48 6 6 10.48 6 16s4.48 10 10 10 10-4.48 10-10S21.52 6 16 6zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z" fill="currentColor"/>
              <circle cx="16" cy="13" r="2" fill="currentColor"/>
              <path d="M12 19c0-2.21 1.79-4 4-4s4 1.79 4 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalMilestones }}</div>
            <div class="stat-label">里程碑總數</div>
            <div class="stat-trend positive">
              <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
                <path d="M6 2L10 6H7V10H5V6H2L6 2Z" fill="currentColor"/>
              </svg>
              <span>+12%</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="8" :xl="6">
        <div class="stat-card flashcard-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <rect x="6" y="8" width="20" height="18" rx="2" fill="currentColor" opacity="0.2"/>
              <path d="M8 8h16c1.1 0 2 .9 2 2v14c0 1.1-.9 2-2 2H8c-1.1 0-2-.9-2-2V10c0-1.1.9-2 2-2z" stroke="currentColor" stroke-width="2"/>
              <path d="M8 14h16M8 18h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalFlashCards }}</div>
            <div class="stat-label">閃卡總數</div>
            <div class="stat-trend positive">
              <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
                <path d="M6 2L10 6H7V10H5V6H2L6 2Z" fill="currentColor"/>
              </svg>
              <span>+8%</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="8" :xl="6">
        <div class="stat-card article-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <rect x="5" y="4" width="22" height="24" rx="2" fill="currentColor" opacity="0.2"/>
              <path d="M7 4h18c1.1 0 2 .9 2 2v20c0 1.1-.9 2-2 2H7c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" stroke="currentColor" stroke-width="2"/>
              <path d="M11 12h10M11 16h10M11 20h6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalArticles }}</div>
            <div class="stat-label">文章總數</div>
            <div class="stat-trend neutral">
              <span>穩定</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="8" :xl="6">
        <div class="stat-card category-card">
          <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <path d="M16 4L4 12v12c0 2.21 1.79 4 4 4h16c2.21 0 4-1.79 4-4V12L16 4z" fill="currentColor" opacity="0.2"/>
              <path d="M16 4L4 12v12c0 2.21 1.79 4 4 4h16c2.21 0 4-1.79 4-4V12L16 4z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M16 20v6M16 20l-4-4M16 20l4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalCategories }}</div>
            <div class="stat-label">分類總數</div>
            <div class="stat-trend neutral">
              <span>—</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Quick Actions -->
    <div class="quick-actions">
      <h2 class="section-title">快速操作</h2>
      <div class="action-grid">
        <router-link to="/milestones/create" class="action-card">
          <div class="action-icon milestone">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
              <path d="M12 6v6l4 2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="action-text">
            <div class="action-title">新增里程碑</div>
            <div class="action-desc">創建新的發展里程碑</div>
          </div>
        </router-link>

        <router-link to="/categories/create" class="action-card">
          <div class="action-icon category">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path d="M3 3h7v7H3z" stroke="currentColor" stroke-width="2"/>
              <path d="M14 3h7v7h-7z" stroke="currentColor" stroke-width="2"/>
              <path d="M3 14h7v7H3z" stroke="currentColor" stroke-width="2"/>
              <path d="M14 14h7v7h-7z" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <div class="action-text">
            <div class="action-title">管理分類</div>
            <div class="action-desc">組織和編輯分類</div>
          </div>
        </router-link>

        <router-link to="/articles/create" class="action-card">
          <div class="action-icon article">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8l-6-6z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M14 2v6h6M16 13H8M16 17H8M10 9H8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <div class="action-text">
            <div class="action-title">撰寫文章</div>
            <div class="action-desc">分享育兒知識</div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

/* Page Header */
.page-header {
  margin-bottom: 32px;
}

.header-content {
  text-align: left;
}

.page-title {
  font-family: var(--font-display);
  font-size: 2rem;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 8px;
  letter-spacing: -0.02em;
}

.page-subtitle {
  font-size: 1rem;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

/* Stats Grid */
.stats-grid {
  margin-bottom: 40px;
}

.stat-card {
  background: var(--color-bg-elevated);
  border-radius: var(--radius-lg);
  padding: 24px;
  border: 1px solid var(--color-border-subtle);
  box-shadow: var(--shadow-sm);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all var(--transition-base);
  cursor: pointer;
  height: 100%;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--color-border-default);
}

.stat-icon {
  width: 64px;
  height: 64px;
  min-width: 64px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-base);
}

.milestone-card .stat-icon {
  background: linear-gradient(135deg, rgba(255, 107, 107, 0.15), rgba(255, 107, 107, 0.05));
  color: var(--color-primary);
}

.flashcard-card .stat-icon {
  background: linear-gradient(135deg, rgba(78, 205, 196, 0.15), rgba(78, 205, 196, 0.05));
  color: var(--color-secondary);
}

.article-card .stat-icon {
  background: linear-gradient(135deg, rgba(255, 179, 71, 0.15), rgba(255, 179, 71, 0.05));
  color: var(--color-warning);
}

.category-card .stat-icon {
  background: linear-gradient(135deg, rgba(116, 185, 255, 0.15), rgba(116, 185, 255, 0.05));
  color: var(--color-info);
}

.stat-card:hover .stat-icon {
  transform: scale(1.1);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-family: var(--font-display);
  font-size: 2.25rem;
  font-weight: 700;
  color: var(--color-text-primary);
  line-height: 1.2;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 0.875rem;
  color: var(--color-text-secondary);
  font-weight: 500;
  margin-bottom: 8px;
}

.stat-trend {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: var(--radius-full);
}

.stat-trend.positive {
  color: var(--color-success);
  background: rgba(78, 205, 196, 0.1);
}

.stat-trend.neutral {
  color: var(--color-text-tertiary);
  background: var(--color-bg-tertiary);
}

.stat-trend svg {
  flex-shrink: 0;
}

/* Quick Actions */
.quick-actions {
  background: var(--color-bg-elevated);
  border-radius: var(--radius-lg);
  padding: 32px;
  border: 1px solid var(--color-border-subtle);
  box-shadow: var(--shadow-sm);
}

.section-title {
  font-family: var(--font-display);
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 24px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-radius: var(--radius-md);
  border: 1.5px solid var(--color-border-subtle);
  background: var(--color-bg-secondary);
  text-decoration: none;
  transition: all var(--transition-base);
}

.action-card:hover {
  background: var(--color-bg-tertiary);
  border-color: var(--color-border-default);
  transform: translateX(4px);
  box-shadow: var(--shadow-md);
}

.action-icon {
  width: 48px;
  height: 48px;
  min-width: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-base);
}

.action-icon.milestone {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  color: white;
}

.action-icon.category {
  background: linear-gradient(135deg, var(--color-secondary), var(--color-secondary-light));
  color: white;
}

.action-icon.article {
  background: linear-gradient(135deg, var(--color-warning), #FFD700);
  color: white;
}

.action-card:hover .action-icon {
  transform: scale(1.1) rotate(5deg);
}

.action-text {
  flex: 1;
}

.action-title {
  font-family: var(--font-display);
  font-size: 1rem;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.action-desc {
  font-size: 0.8125rem;
  color: var(--color-text-tertiary);
}

/* Responsive */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }

  .stats-grid {
    margin-bottom: 24px;
  }

  .stat-card {
    padding: 20px;
  }

  .stat-icon {
    width: 56px;
    height: 56px;
    min-width: 56px;
  }

  .stat-value {
    font-size: 1.75rem;
  }

  .quick-actions {
    padding: 24px 20px;
  }

  .action-grid {
    grid-template-columns: 1fr;
  }
}
</style>
