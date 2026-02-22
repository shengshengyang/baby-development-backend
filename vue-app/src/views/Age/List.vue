<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ageApi } from '@/api'
import type { Age } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const ages = ref<Age[]>([])

onMounted(async () => {
  await loadAges()
})

async function loadAges() {
  loading.value = true
  try {
    const data = await ageApi.getList()
    ages.value = data
    console.log('Ages loaded:', data)
  } catch (error) {
    console.error('Failed to load ages:', error)
    ElMessage.error('載入年齡段失敗')
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  router.push('/ages/create')
}

function handleEdit(row: Age) {
  console.log('Edit age:', row)
  router.push(`/ages/${row.id}/edit`)
}

async function handleDelete(row: Age) {
  try {
    await ElMessageBox.confirm('確定要刪除此年齡段嗎？', '提示', {
      type: 'warning',
      confirmButtonText: '刪除',
      cancelButtonText: '取消',
    })
    await ageApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadAges()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '刪除失敗')
    }
  }
}
</script>

<template>
  <div class="age-list animate-fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">年齡段管理</h1>
          <p class="page-subtitle">管理寶寶發展的年齡分段設定</p>
        </div>
        <el-button type="primary" size="large" class="create-button" @click="handleCreate">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" style="margin-right: 6px;">
            <path d="M8 2v12M2 8h12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          新增年齡段
        </el-button>
      </div>
    </div>

    <!-- Table Card -->
    <el-card class="table-card">
      <el-table :data="ages" v-loading="loading" stripe class="age-table">
        <el-table-column prop="displayName" label="名稱" min-width="200">
          <template #default="{ row }">
            {{ row.displayName?.tw || row.displayName?.en || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="month" label="月份數" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" size="small" @click="handleEdit(row)">
                編輯
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">
                刪除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- Empty State -->
      <div v-if="!loading && ages.length === 0" class="empty-state">
        <svg width="64" height="64" viewBox="0 0 64 64" fill="none">
          <circle cx="32" cy="32" r="28" stroke="currentColor" stroke-width="2" opacity="0.2"/>
          <path d="M32 20v14M32 38l-6-6M32 38l6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" opacity="0.4"/>
        </svg>
        <p class="empty-text">暫無年齡段資料</p>
        <el-button type="primary" @click="handleCreate">新增第一個年齡段</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.age-list {
  max-width: 1400px;
  margin: 0 auto;
}

/* Page Header */
.page-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24px;
}

.header-text {
  flex: 1;
}

.page-title {
  font-family: var(--font-display);
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 8px;
  letter-spacing: -0.02em;
}

.page-subtitle {
  font-size: 0.9375rem;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

.create-button {
  height: 44px;
  padding: 0 24px;
  font-weight: 600;
  white-space: nowrap;
}

/* Table Card */
.table-card {
  border: 1px solid var(--color-border-subtle);
}

.action-buttons {
  display: flex;
  gap: 8px;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 64px 32px;
}

.empty-state svg {
  color: var(--color-text-tertiary);
  margin-bottom: 16px;
}

.empty-text {
  font-size: 1rem;
  color: var(--color-text-secondary);
  margin-bottom: 24px;
  font-weight: 500;
}

/* Responsive */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: stretch;
  }

  .page-title {
    font-size: 1.5rem;
  }

  .create-button {
    width: 100%;
  }
}
</style>
