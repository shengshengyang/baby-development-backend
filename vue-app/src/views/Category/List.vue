<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { categoryApi } from '@/api'
import type { Category } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const categories = ref<Category[]>([])

onMounted(async () => {
  await loadCategories()
})

async function loadCategories() {
  loading.value = true
  try {
    const data = await categoryApi.getList()
    categories.value = data
  } catch (error) {
    ElMessage.error('載入分類失敗')
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  router.push('/categories/create')
}

function handleEdit(row: Category) {
  router.push(`/categories/${row.id}/edit`)
}

async function handleDelete(row: Category) {
  try {
    await ElMessageBox.confirm('確定要刪除此分類嗎？', '提示', { type: 'warning' })
    await categoryApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadCategories()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '刪除失敗')
    }
  }
}
</script>

<template>
  <div class="category-list">
    <div class="page-header">
      <h2>分類管理</h2>
      <el-button type="primary" :icon="'Plus'" @click="handleCreate">新增分類</el-button>
    </div>

    <el-card>
      <el-table :data="categories" v-loading="loading" stripe>
        <el-table-column prop="name" label="名稱" min-width="200">
          <template #default="{ row }">
            {{ row.name?.tw || row.name?.en || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="'Edit'" @click="handleEdit(row)">
              編輯
            </el-button>
            <el-button type="danger" size="small" :icon="'Delete'" @click="handleDelete(row)">
              刪除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.category-list { padding: 20px; }
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
</style>
