<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { babyApi } from '@/api'
import type { Baby } from '@/types'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const babies = ref<Baby[]>([])

onMounted(async () => {
  await loadBabies()
})

async function loadBabies() {
  loading.value = true
  try {
    const data = await babyApi.getList()
    babies.value = data
  } catch (error) {
    ElMessage.error('載入寶寶資料失敗')
  } finally {
    loading.value = false
  }
}

async function handleDelete(row: Baby) {
  try {
    await ElMessageBox.confirm('確定要刪除此寶寶資料嗎？', '提示', { type: 'warning' })
    await babyApi.delete(row.id)
    ElMessage.success('刪除成功')
    await loadBabies()
  } catch (error: any) {
    if (error !== 'cancel') ElMessage.error(error.message || '刪除失敗')
  }
}
</script>

<template>
  <div class="baby-list">
    <div class="page-header">
      <h2>寶寶管理</h2>
    </div>

    <el-card>
      <el-table :data="babies" v-loading="loading" stripe>
        <el-table-column prop="name" label="姓名" min-width="150" />
        <el-table-column prop="birthdate" label="生日" width="180" />
        <el-table-column prop="gender" label="性別" width="100">
          <template #default="{ row }">
            {{ row.gender === 'MALE' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
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
.baby-list { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
</style>
