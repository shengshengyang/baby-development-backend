<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { milestoneApi, optionApi } from '@/api'
import type { Category, Age, LangFieldObject } from '@/types'
import { ElMessage, ElUpload } from 'element-plus'
import type { UploadFile } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const saving = ref(false)
const categories = ref<Category[]>([])
const ages = ref<Age[]>([])

// Use UUID directly as string, not parseInt
const milestoneId = computed(() => route.params.id as string)
const isEdit = computed(() => !!milestoneId.value)

const form = ref<{
  age: { id: string }
  category: { id: string }
  descriptionObject: LangFieldObject
  videoUrl: string
  imageBase64: string
}>({
  age: { id: '' },
  category: { id: '' },
  descriptionObject: {
    tw: '',
    en: '',
  },
  videoUrl: '',
  imageBase64: '',
})

const fileList = ref<UploadFile[]>([])

const rules = {
  'descriptionObject.tw': [{ required: true, message: '請輸入繁體中文描述', trigger: 'blur' }],
  'descriptionObject.en': [{ required: true, message: '請輸入英文描述', trigger: 'blur' }],
  'age.id': [{ required: true, message: '請選擇年齡段', trigger: 'change' }],
  'category.id': [{ required: true, message: '請選擇分類', trigger: 'change' }],
}

const formRef = ref()

onMounted(async () => {
  await loadOptions()
  if (isEdit.value) {
    await loadMilestone()
  }
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
    console.error('Failed to load options:', error)
    ElMessage.error('載入選項失敗')
  } finally {
    loading.value = false
  }
}

async function loadMilestone() {
  loading.value = true
  try {
    const data = await milestoneApi.getById(milestoneId.value)
    form.value = {
      age: { id: data.age?.id || data.ageId || '' },
      category: { id: data.category?.id || data.categoryId || '' },
      descriptionObject: data.descriptionObject || { tw: '', en: '' },
      videoUrl: data.videoUrl || '',
      imageBase64: data.imageBase64 || '',
    }
    // Set image file list if there's an existing image
    if (data.imageBase64) {
      fileList.value = [{
        name: 'image.png',
        url: data.imageBase64,
        status: 'success',
        uid: Date.now(),
      }]
    }
  } catch (error) {
    console.error('Failed to load milestone:', error)
    ElMessage.error('載入里程碑失敗')
    router.push('/milestones')
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  saving.value = true
  try {
    const submitData = {
      ...form.value,
      // Only include imageBase64 if it's not empty
      ...(form.value.imageBase64 ? { imageBase64: form.value.imageBase64 } : {}),
    }

    if (isEdit.value) {
      await milestoneApi.update(milestoneId.value, submitData)
      ElMessage.success('更新成功')
    } else {
      await milestoneApi.create(submitData)
      ElMessage.success('創建成功')
    }
    router.push('/milestones')
  } catch (error: any) {
    console.error('Failed to save milestone:', error)
    ElMessage.error(error.response?.data?.message || '保存失敗')
  } finally {
    saving.value = false
  }
}

function handleCancel() {
  router.push('/milestones')
}

// Handle image upload and convert to base64
function handleImageUpload(file: File) {
  const reader = new FileReader()
  reader.onload = (e) => {
    form.value.imageBase64 = e.target?.result as string
  }
  reader.readAsDataURL(file)
  return false // Prevent auto upload
}

function handleImageRemove() {
  form.value.imageBase64 = ''
  fileList.value = []
}

function getDisplayName(langObj: LangFieldObject | undefined) {
  return langObj?.tw || langObj?.en || '-'
}
</script>

<template>
  <div class="milestone-form-page animate-fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1 class="page-title">{{ isEdit ? '編輯里程碑' : '新增里程碑' }}</h1>
          <p class="page-subtitle">{{ isEdit ? '修改現有里程碑設定' : '建立新的發展里程碑' }}</p>
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
        class="milestone-form"
      >
        <!-- Age & Category Section -->
        <div class="form-section">
          <div class="section-title">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <path d="M10 2v16M2 10h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span>基本設定</span>
          </div>

          <div class="form-grid">
            <el-form-item label="年齡段" prop="age.id">
              <el-select
                v-model="form.age.id"
                placeholder="請選擇年齡段"
                size="large"
                class="full-width"
              >
                <el-option
                  v-for="age in ages"
                  :key="age.id"
                  :label="`${getDisplayName(age.displayName)} (${age.month} 個月)`"
                  :value="age.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="分類" prop="category.id">
              <el-select
                v-model="form.category.id"
                placeholder="請選擇分類"
                size="large"
                class="full-width"
              >
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="getDisplayName(category.name)"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </div>
        </div>

        <!-- Multi-language Description Section -->
        <div class="form-section">
          <div class="section-title">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <path d="M3 5h14M3 10h14M3 15h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span>里程碑描述</span>
          </div>

          <div class="lang-fields">
            <el-form-item label="繁體中文" prop="descriptionObject.tw">
              <el-input
                v-model="form.descriptionObject.tw"
                type="textarea"
                :rows="4"
                placeholder="例如：能夠獨立坐穩"
                size="large"
              />
            </el-form-item>

            <el-form-item label="English" prop="descriptionObject.en">
              <el-input
                v-model="form.descriptionObject.en"
                type="textarea"
                :rows="4"
                placeholder="例如：Can sit independently"
                size="large"
              />
            </el-form-item>
          </div>
        </div>

        <!-- Media Section -->
        <div class="form-section">
          <div class="section-title">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <rect x="3" y="3" width="14" height="14" rx="2" stroke="currentColor" stroke-width="2"/>
              <circle cx="8.5" cy="8.5" r="1.5" stroke="currentColor" stroke-width="2"/>
              <path d="M3 15l5-5 3 3 5-6v5" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span>媒體資源</span>
          </div>

          <el-form-item label="影片連結">
            <el-input
              v-model="form.videoUrl"
              placeholder="請輸入 YouTube 或其他影片連結"
              size="large"
            />
            <span class="field-hint">可選項，輸入演示此里程碑的影片 URL</span>
          </el-form-item>

          <el-form-item label="示意圖片">
            <el-upload
              v-model:file-list="fileList"
              class="image-uploader"
              :auto-upload="false"
              :on-change="handleImageUpload"
              :on-remove="handleImageRemove"
              :limit="1"
              list-type="picture-card"
              accept="image/*"
            >
              <svg v-if="fileList.length === 0" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </el-upload>
            <span class="field-hint">可選項，上傳示意圖片（支援 JPG、PNG）</span>
          </el-form-item>
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
            {{ isEdit ? '更新里程碑' : '新增里程碑' }}
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
.milestone-form-page {
  max-width: 1100px;
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

.milestone-form {
  padding: 8px 0;
}

/* Form Sections */
.form-section {
  margin-bottom: 32px;
}

.form-section:last-of-type {
  margin-bottom: 24px;
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

/* Form Grid */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* Language Fields */
.lang-fields {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* Full Width */
.full-width {
  width: 100%;
}

/* Field Hint */
.field-hint {
  display: block;
  margin-top: 8px;
  font-size: 0.875rem;
  color: var(--color-text-tertiary);
  line-height: 1.5;
}

/* Image Uploader */
.image-uploader {
  width: 100%;
}

.image-uploader :deep(.el-upload-list--picture-card) {
  --el-upload-list-picture-card-size: 120px;
}

.image-uploader :deep(.el-upload--picture-card) {
  width: 120px;
  height: 120px;
  border: 2px dashed var(--color-border-default);
  border-radius: var(--radius-md);
  background: var(--color-bg-secondary);
  transition: all var(--transition-base);
}

.image-uploader :deep(.el-upload--picture-card:hover) {
  border-color: var(--color-primary);
  background: var(--color-primary-soft);
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

  .form-grid,
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
