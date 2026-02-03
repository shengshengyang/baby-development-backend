<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { flashcardApi } from '@/api'
import type { FlashCard } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const flashcards = ref<FlashCard[]>([])
const pagination = ref({ page: 1, size: 10, total: 0 })

onMounted(async () => {
  await loadFlashCards()
})

async function loadFlashCards() {
  loading.value = true
  try {
    const data = await flashcardApi.getList({
      page: pagination.value.page - 1,
      size: pagination.value.size,
    })
    flashcards.value = data.content || []
    pagination.value.total = data.totalElements || 0
  } catch (error) {
    ElMessage.error('載入閃卡失敗')
  } finally {
    loading.value = false
  }
}

function handleCreate() { router.push('/flashcards/create') }
function handleEdit(row: FlashCard) { router.push(`/flashcards/${row.id}/edit`) }

async function handleDelete(row: FlashCard) {
  try {
    await ElMessageBox.confirm('確定要刪除此閃卡嗎？', '提示', { type: 'warning' })
    await flashcardApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadFlashCards()
  } catch (error: any) {
    if (error !== 'cancel') ElMessage.error(error.message || '刪除失敗')
  }
}

function handlePageChange(page: number) {
  pagination.value.page = page
  loadFlashCards()
}
</script>

<template>
  <div class="flashcard-list">
    <div class="page-header">
      <h2>閃卡管理</h2>
      <el-button type="primary" :icon="'Plus'" @click="handleCreate">新增閃卡</el-button>
    </div>

    <el-card>
      <el-table :data="flashcards" v-loading="loading" stripe>
        <el-table-column prop="question" label="問題" min-width="250" />
        <el-table-column prop="answer" label="答案" min-width="250" show-overflow-tooltip />
        <el-table-column prop="category.name" label="分類" width="150" />
        <el-table-column prop="age.name" label="年齡段" width="150" />
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
          @size-change="loadFlashCards"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.flashcard-list { padding: 20px; }
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
.pagination { display: flex; justify-content: flex-end; margin-top: 20px; }
</style>
