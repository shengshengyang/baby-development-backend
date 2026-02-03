<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { milestoneApi, optionApi } from '@/api'
import type { Milestone, LangFieldObject } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const milestones = ref<Milestone[]>([])

interface OptionItem {
  label: string
  value: string
  displayName?: LangFieldObject
  name?: LangFieldObject
}

const categories = ref<OptionItem[]>([])
const ages = ref<OptionItem[]>([])

const filters = ref({
  ageId: undefined as string | undefined,
  categoryId: undefined as string | undefined,
})

onMounted(async () => {
  await loadOptions()
  await loadMilestones()
})

async function loadOptions() {
  try {
    const [categoriesData, agesData] = await Promise.all([
      optionApi.getCategories(),
      optionApi.getAges(),
    ])
    categories.value = categoriesData
    ages.value = agesData
  } catch (error) {
    console.error('Failed to load options:', error)
  }
}

async function loadMilestones() {
  loading.value = true
  try {
    const data = await milestoneApi.getList({
      ageId: filters.value.ageId,
      categoryId: filters.value.categoryId,
    })
    milestones.value = data || []
  } catch (error) {
    console.error('Failed to load milestones:', error)
    ElMessage.error('載入里程碑失敗')
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  router.push('/milestones/create')
}

function handleEdit(row: Milestone) {
  router.push(`/milestones/${row.id}/edit`)
}

async function handleDelete(row: Milestone) {
  try {
    await ElMessageBox.confirm('確定要刪除此里程碑嗎？', '提示', {
      type: 'warning',
      confirmButtonText: '刪除',
      cancelButtonText: '取消',
    })
    await milestoneApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadMilestones()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '刪除失敗')
    }
  }
}

async function handleSearch() {
  await loadMilestones()
}

async function handleReset() {
  filters.value = {
    ageId: undefined,
    categoryId: undefined,
  }
  await handleSearch()
}

function getDisplayName(langObj: LangFieldObject | undefined) {
  return langObj?.tw || langObj?.en || '-'
}

function getDescription(row: Milestone) {
  return getDisplayName(row.descriptionObject) || row.description || '-'
}
</script>

<template>
  <div class="milestone-list animate-fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">里程碑管理</h1>
          <p class="page-subtitle">追蹤與管理寶寶的成長里程碑</p>
        </div>
        <el-button type="primary" size="large" class="create-button" @click="handleCreate">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" style="margin-right: 6px;">
            <path d="M8 2v12M2 8h12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          新增里程碑
        </el-button>
      </div>
    </div>

    <!-- Filters Card -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filters" class="filter-form">
        <el-form-item label="年齡段">
          <el-select
            v-model="filters.ageId"
            placeholder="選擇年齡段"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="age in ages"
              :key="age.value"
              :label="getDisplayName(age.displayName)"
              :value="age.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="分類">
          <el-select
            v-model="filters.categoryId"
            placeholder="選擇分類"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="category in categories"
              :key="category.value"
              :label="getDisplayName(category.name)"
              :value="category.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <svg width="14" height="14" viewBox="0 0 14 14" fill="none" style="margin-right: 4px;">
              <circle cx="6" cy="6" r="4" stroke="currentColor" stroke-width="2"/>
              <path d="M11 11L9 9" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            搜���
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Table Card -->
    <el-card class="table-card">
      <el-table :data="milestones" v-loading="loading" stripe class="milestone-table">
        <el-table-column label="描述" min-width="300" show-overflow-tooltip>
          <template #default="{ row }">
            {{ getDescription(row) }}
          </template>
        </el-table-column>
        <el-table-column label="年齡段" width="150">
          <template #default="{ row }">
            {{ getDisplayName(row.age?.displayNameObject || row.ageName) }}
            <span v-if="row.age?.month" class="age-month">({{ row.age.month }} 個月)</span>
          </template>
        </el-table-column>
        <el-table-column label="分類" width="150">
          <template #default="{ row }">
            {{ getDisplayName(row.category?.name) }}
          </template>
        </el-table-column>
        <el-table-column label="媒體" width="80" align="center">
          <template #default="{ row }">
            <span v-if="row.videoUrl || row.imageBase64" class="media-badge">
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <rect x="2" y="3" width="12" height="10" rx="2" stroke="currentColor" stroke-width="1.5"/>
                <circle cx="6" cy="7" r="1.5" stroke="currentColor" stroke-width="1.5"/>
                <path d="M2 13l4-4 2.5 2.5L14 6v5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span v-else class="no-media">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
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
      <div v-if="!loading && milestones.length === 0" class="empty-state">
        <svg width="64" height="64" viewBox="0 0 64 64" fill="none">
          <circle cx="32" cy="32" r="28" stroke="currentColor" stroke-width="2" opacity="0.2"/>
          <path d="M32 20v14M32 38l-6-6M32 38l6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" opacity="0.4"/>
        </svg>
        <p class="empty-text">暫無里程碑資料</p>
        <el-button type="primary" @click="handleCreate">新增第一個里程碑</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.milestone-list {
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

/* Filter Card */
.filter-card {
  margin-bottom: 20px;
  border: 1px solid var(--color-border-subtle);
}

.filter-form :deep(.el-form-item) {
  margin-bottom: 0;
}

.filter-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--color-text-secondary);
}

/* Table Card */
.table-card {
  border: 1px solid var(--color-border-subtle);
}

.milestone-table {
  width: 100%;
}

.age-month {
  font-size: 0.875rem;
  color: var(--color-text-tertiary);
  margin-left: 4px;
}

.media-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: var(--color-primary);
}

.no-media {
  color: var(--color-text-quaternary);
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

  .filter-form :deep(.el-form-item) {
    display: block;
    margin-right: 0;
    margin-bottom: 16px;
  }

  .filter-form :deep(.el-select) {
    width: 100% !important;
  }
}
</style>
