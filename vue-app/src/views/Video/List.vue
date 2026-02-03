<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { videoApi } from '@/api'
import type { Video } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const videos = ref<Video[]>([])
const pagination = ref({ page: 1, size: 10, total: 0 })

onMounted(async () => {
  await loadVideos()
})

async function loadVideos() {
  loading.value = true
  try {
    const data = await videoApi.getList({
      page: pagination.value.page - 1,
      size: pagination.value.size,
    })
    videos.value = data.content || []
    pagination.value.total = data.totalElements || 0
  } catch (error) {
    ElMessage.error('載入影片失敗')
  } finally {
    loading.value = false
  }
}

function handleCreate() { router.push('/videos/create') }
function handleEdit(row: Video) { router.push(`/videos/${row.id}/edit`) }

async function handleDelete(row: Video) {
  try {
    await ElMessageBox.confirm('確定要刪除此影片嗎？', '提示', { type: 'warning' })
    await videoApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadVideos()
  } catch (error: any) {
    if (error !== 'cancel') ElMessage.error(error.message || '刪除失敗')
  }
}

function handlePageChange(page: number) {
  pagination.value.page = page
  loadVideos()
}
</script>

<template>
  <div class="video-list">
    <div class="page-header">
      <h2>影片管理</h2>
      <el-button type="primary" :icon="'Plus'" @click="handleCreate">新增影片</el-button>
    </div>

    <el-card>
      <el-table :data="videos" v-loading="loading" stripe>
        <el-table-column prop="title" label="標題" min-width="200" />
        <el-table-column prop="url" label="影片 URL" min-width="300" show-overflow-tooltip />
        <el-table-column prop="milestone.title" label="關聯里程碑" width="200" />
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
          @size-change="loadVideos"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.video-list { padding: 20px; }
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
.pagination { display: flex; justify-content: flex-end; margin-top: 20px; }
</style>
