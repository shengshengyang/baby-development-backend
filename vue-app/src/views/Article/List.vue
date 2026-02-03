<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { articleApi, optionApi } from '@/api'
import type { Article, Category } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const articles = ref<Article[]>([])
const categories = ref<Category[]>([])

const filters = ref({ categoryId: undefined as number | undefined })
const pagination = ref({ page: 1, size: 10, total: 0 })

onMounted(async () => {
  await loadOptions()
  await loadArticles()
})

async function loadOptions() {
  try {
    categories.value = await optionApi.getCategories()
  } catch (error) { console.error('Failed to load options:', error) }
}

async function loadArticles() {
  loading.value = true
  try {
    const data = await articleApi.getList({
      page: pagination.value.page - 1,
      size: pagination.value.size,
      categoryId: filters.value.categoryId,
    })
    articles.value = data.content || []
    pagination.value.total = data.totalElements || 0
  } catch (error) {
    ElMessage.error('載入文章失敗')
  } finally {
    loading.value = false
  }
}

function handleCreate() { router.push('/articles/create') }
function handleEdit(row: Article) { router.push(`/articles/${row.id}/edit`) }

async function handleDelete(row: Article) {
  try {
    await ElMessageBox.confirm('確定要刪除此文章嗎？', '提示', { type: 'warning' })
    await articleApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadArticles()
  } catch (error: any) {
    if (error !== 'cancel') ElMessage.error(error.message || '刪除失敗')
  }
}

function handlePageChange(page: number) {
  pagination.value.page = page
  loadArticles()
}
</script>

<template>
  <div class="article-list">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" :icon="'Plus'" @click="handleCreate">新增文章</el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="分類">
          <el-select v-model="filters.categoryId" placeholder="請選擇分類" clearable style="width: 200px">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="'Search'" @click="loadArticles">搜尋</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="articles" v-loading="loading" stripe>
        <el-table-column prop="title" label="標題" min-width="200" />
        <el-table-column prop="summary" label="摘要" min-width="300" show-overflow-tooltip />
        <el-table-column prop="category.name" label="分類" width="150" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="'Edit'" @click="handleEdit(row)">編輯</el-button>
            <el-button type="danger" size="small" :icon="'Delete'" @click="handleDelete(row)">刪除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @current-change="handlePageChange"
          @size-change="loadArticles"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.article-list { padding: 20px; }
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
.filter-card { margin-bottom: 20px; }
.pagination { display: flex; justify-content: flex-end; margin-top: 20px; }
</style>
