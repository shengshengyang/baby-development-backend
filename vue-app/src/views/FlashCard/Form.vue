<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { flashcardApi, optionApi } from '@/api'
import type { Category, Age } from '@/types'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const saving = ref(false)
const categories = ref<Category[]>([])
const ages = ref<Age[]>([])

const flashcardId = computed(() => parseInt(route.params.id as string))
const isEdit = computed(() => !!flashcardId.value)

const form = ref({
  question: '',
  answer: '',
  categoryId: undefined as number | undefined,
  ageId: undefined as number | undefined,
  milestoneId: undefined as number | undefined,
})

const rules = {
  question: [{ required: true, message: '請輸入問題', trigger: 'blur' }],
  answer: [{ required: true, message: '請輸入答案', trigger: 'blur' }],
}

const formRef = ref()

onMounted(async () => {
  await loadOptions()
  if (isEdit.value) await loadFlashCard()
})

async function loadOptions() {
  loading.value = true
  try {
    const [categoriesData, agesData] = await Promise.all([
      optionApi.getCategories(),
      optionApi.getAges(),
    ])
    categories.value = categoriesData
    ages.value = agesData
  } catch (error) {
    ElMessage.error('載入選項失敗')
  } finally {
    loading.value = false
  }
}

async function loadFlashCard() {
  loading.value = true
  try {
    const data = await flashcardApi.getById(flashcardId.value)
    form.value = {
      question: data.question,
      answer: data.answer,
      categoryId: data.categoryId,
      ageId: data.ageId,
      milestoneId: data.milestoneId,
    }
  } catch (error) {
    ElMessage.error('載入閃卡失敗')
    router.push('/flashcards')
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
      await flashcardApi.update(flashcardId.value, form.value)
      ElMessage.success('更新成功')
    } else {
      await flashcardApi.create(form.value)
      ElMessage.success('創建成功')
    }
    router.push('/flashcards')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '保存失敗')
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div class="flashcard-form">
    <div class="page-header">
      <h2>{{ isEdit ? '編輯閃卡' : '新增閃卡' }}</h2>
    </div>

    <el-card v-loading="loading">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="問題" prop="question">
          <el-input v-model="form.question" type="textarea" :rows="3" placeholder="請輸入問題" />
        </el-form-item>

        <el-form-item label="答案" prop="answer">
          <el-input v-model="form.answer" type="textarea" :rows="5" placeholder="請輸入答案" />
        </el-form-item>

        <el-form-item label="分類" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="請選擇分類" clearable style="width: 100%">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="年齡段" prop="ageId">
          <el-select v-model="form.ageId" placeholder="請選擇年齡段" clearable style="width: 100%">
            <el-option v-for="age in ages" :key="age.id" :label="age.name" :value="age.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="關聯里程碑" prop="milestoneId">
          <el-input v-model="form.milestoneId" type="number" placeholder="請輸入里程碑 ID" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSubmit">
            {{ isEdit ? '更新' : '創建' }}
          </el-button>
          <el-button @click="router.push('/flashcards')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.flashcard-form { padding: 20px; }
.page-header h2 { font-size: 24px; color: #333; margin: 0; }
</style>
