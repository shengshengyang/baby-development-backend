<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { videoApi, milestoneApi } from '@/api'
import type { Milestone } from '@/types'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const saving = ref(false)
const milestones = ref<Milestone[]>([])

const videoId = computed(() => parseInt(route.params.id as string))
const isEdit = computed(() => !!videoId.value)

const form = ref({
  title: '',
  description: '',
  url: '',
  thumbnailUrl: '',
  duration: undefined as number | undefined,
  milestoneId: undefined as number | undefined,
  articleId: undefined as number | undefined,
})

const rules = {
  title: [{ required: true, message: '請輸入標題', trigger: 'blur' }],
  url: [{ required: true, message: '請輸入影片 URL', trigger: 'blur' }],
}

const formRef = ref()

onMounted(async () => {
  await loadMilestones()
  if (isEdit.value) await loadVideo()
})

async function loadMilestones() {
  loading.value = true
  try {
    const data = await milestoneApi.getList({})
    milestones.value = data.content || []
  } catch (error) {
    ElMessage.error('載入里程碑失敗')
  } finally {
    loading.value = false
  }
}

async function loadVideo() {
  loading.value = true
  try {
    const data = await videoApi.getById(videoId.value)
    form.value = {
      title: data.title,
      description: data.description || '',
      url: data.url,
      thumbnailUrl: data.thumbnailUrl || '',
      duration: data.duration,
      milestoneId: data.milestoneId,
      articleId: data.articleId,
    }
  } catch (error) {
    ElMessage.error('載入影片失敗')
    router.push('/videos')
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch { return }

  saving.value = true
  try {
    if (isEdit.value) {
      await videoApi.update(videoId.value, form.value)
      ElMessage.success('更新成功')
    } else {
      await videoApi.create(form.value)
      ElMessage.success('創建成功')
    }
    router.push('/videos')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '保存失敗')
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div class="video-form">
    <div class="page-header">
      <h2>{{ isEdit ? '編輯影片' : '新增影片' }}</h2>
    </div>

    <el-card v-loading="loading">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="標題" prop="title">
          <el-input v-model="form.title" placeholder="請輸入標題" />
        </el-form-item>

        <el-form-item label="影片 URL" prop="url">
          <el-input v-model="form.url" placeholder="請輸入影片 URL (YouTube 或直連)" />
        </el-form-item>

        <el-form-item label="縮圖 URL" prop="thumbnailUrl">
          <el-input v-model="form.thumbnailUrl" placeholder="請輸入縮圖 URL" />
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="請輸入描述" />
        </el-form-item>

        <el-form-item label="時長（秒）" prop="duration">
          <el-input-number v-model="form.duration" :min="0" :max="3600" />
        </el-form-item>

        <el-form-item label="關聯里程碑" prop="milestoneId">
          <el-select v-model="form.milestoneId" placeholder="請選擇里程碑" clearable filterable style="width: 100%">
            <el-option v-for="m in milestones" :key="m.id" :label="m.title" :value="m.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="關聯文章 ID" prop="articleId">
          <el-input v-model="form.articleId" type="number" placeholder="請輸入文章 ID" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSubmit">
            {{ isEdit ? '更新' : '創建' }}
          </el-button>
          <el-button @click="router.push('/videos')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.video-form { padding: 20px; }
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
</style>
