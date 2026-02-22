<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { articleApi, optionApi } from '@/api'
import type { Category } from '@/types'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const saving = ref(false)
const categories = ref<Category[]>([])

const articleId = computed(() => parseInt(route.params.id as string))
const isEdit = computed(() => !!articleId.value)

const form = ref({
  title: '',
  content: '',
  summary: '',
  categoryId: undefined as number | undefined,
})

const rules = {
  title: [{ required: true, message: '請輸入標題', trigger: 'blur' }],
  content: [{ required: true, message: '請輸入內容', trigger: 'blur' }],
}

const formRef = ref()

onMounted(async () => {
  await loadOptions()
  if (isEdit.value) await loadArticle()
})

async function loadOptions() {
  loading.value = true
  try {
    categories.value = await optionApi.getCategories()
  } catch (error) {
    ElMessage.error('載入選項失敗')
  } finally {
    loading.value = false
  }
}

async function loadArticle() {
  loading.value = true
  try {
    const data = await articleApi.getById(articleId.value)
    form.value = {
      title: data.title,
      content: data.content,
      summary: data.summary || '',
      categoryId: data.categoryId,
    }
  } catch (error) {
    ElMessage.error('載入文章失敗')
    router.push('/articles')
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
      await articleApi.update(articleId.value, form.value)
      ElMessage.success('更新成功')
    } else {
      await articleApi.create(form.value)
      ElMessage.success('創建成功')
    }
    router.push('/articles')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '保存失敗')
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div class="article-form">
    <div class="page-header">
      <h2>{{ isEdit ? '編輯文章' : '新增文章' }}</h2>
    </div>

    <el-card v-loading="loading">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="標題" prop="title">
          <el-input v-model="form.title" placeholder="請輸入標題" />
        </el-form-item>

        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="請輸入摘要" />
        </el-form-item>

        <el-form-item label="分類" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="請選擇分類" style="width: 100%">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="內容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="15" placeholder="請輸入內容" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSubmit">
            {{ isEdit ? '更新' : '創建' }}
          </el-button>
          <el-button @click="router.push('/articles')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.article-form { padding: 20px; }
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
</style>
