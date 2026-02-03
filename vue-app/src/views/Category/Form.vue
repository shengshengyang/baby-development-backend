<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { categoryApi } from '@/api'
import type { LangFieldObject } from '@/types'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const saving = ref(false)

// Use UUID directly as string, not parseInt
const categoryId = computed(() => route.params.id as string)
const isEdit = computed(() => !!categoryId.value)

const form = ref<{
  name: LangFieldObject
}>({
  name: {
    tw: '',
    en: '',
  },
})

const rules = {
  'name.tw': [{ required: true, message: '請輸入繁體中文名稱', trigger: 'blur' }],
  'name.en': [{ required: true, message: '請輸入英文名稱', trigger: 'blur' }],
}

const formRef = ref()

onMounted(async () => {
  if (isEdit.value) await loadCategory()
})

async function loadCategory() {
  loading.value = true
  try {
    const data = await categoryApi.getById(categoryId.value)
    form.value = {
      name: data.name || { tw: '', en: '' },
    }
  } catch (error) {
    console.error('Failed to load category:', error)
    ElMessage.error('載入分類失敗')
    router.push('/categories')
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
    // Convert form data to match backend API format (just the name object)
    const apiData = {
      name: form.value.name,
    }

    if (isEdit.value) {
      await categoryApi.update(categoryId.value, apiData)
      ElMessage.success('更新成功')
    } else {
      await categoryApi.create(apiData)
      ElMessage.success('創建成功')
    }
    router.push('/categories')
  } catch (error: any) {
    console.error('Failed to save category:', error)
    ElMessage.error(error.response?.data?.message || '保存失敗')
  } finally {
    saving.value = false
  }
}

function handleCancel() {
  router.push('/categories')
}
</script>

<template>
  <div class="category-form-page animate-fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">{{ isEdit ? '編輯分類' : '新增分類' }}</h1>
          <p class="page-subtitle">{{ isEdit ? '修改現有分類設定' : '建立新的發展分類' }}</p>
        </div>
      </div>
    </div>

    <!-- Form Card -->
    <el-card class="form-card" v-loading="loading">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        class="category-form"
      >
        <!-- Multi-language Name Section -->
        <div class="form-section">
          <div class="section-title">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <path d="M10 2v16M2 10h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span>分類名稱</span>
          </div>

          <div class="lang-fields">
            <el-form-item label="繁體中文" prop="name.tw">
              <el-input
                v-model="form.name.tw"
                placeholder="例如：運動發展"
                size="large"
              />
            </el-form-item>

            <el-form-item label="English" prop="name.en">
              <el-input
                v-model="form.name.en"
                placeholder="例如：Motor Development"
                size="large"
              />
            </el-form-item>
          </div>
        </div>

        <!-- Form Actions -->
        <div class="form-actions">
          <el-button
            type="primary"
            size="large"
            :loading="saving"
            @click="handleSubmit"
            class="submit-button"
          >
            <svg v-if="!saving" width="16" height="16" viewBox="0 0 16 16" fill="none" style="margin-right: 6px;">
              <path d="M8 2.5v11M2.5 8h11" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            {{ isEdit ? '更新分類' : '新增分類' }}
          </el-button>
          <el-button size="large" @click="handleCancel" class="cancel-button">
            取消
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.category-form-page {
  max-width: 900px;
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

/* Form Card */
.form-card {
  border: 1px solid var(--color-border-subtle);
}

.category-form {
  padding: 8px 0;
}

/* Form Sections */
.form-section {
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1rem;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-subtle);
}

.section-title svg {
  color: var(--color-primary);
}

/* Language Fields */
.lang-fields {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* Form Actions */
.form-actions {
  display: flex;
  gap: 12px;
  padding-top: 24px;
  border-top: 1px solid var(--color-border-subtle);
}

.submit-button {
  height: 44px;
  padding: 0 32px;
  font-weight: 600;
}

.cancel-button {
  height: 44px;
  padding: 0 32px;
  font-weight: 500;
}

/* Responsive */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }

  .lang-fields {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }

  .submit-button,
  .cancel-button {
    width: 100%;
  }
}
</style>
