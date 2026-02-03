<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useTheme } from '@/composables/useTheme'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const { toggleTheme, isDark } = useTheme()

const loginForm = reactive({
  email: '',
  password: '',
})

const loading = ref(false)

async function handleLogin() {
  if (!loginForm.email || !loginForm.password) {
    ElMessage.warning('請輸入電子郵件和密碼')
    return
  }

  loading.value = true
  try {
    await userStore.login(loginForm.email, loginForm.password)
    ElMessage.success('登入成功')
    router.push('/dashboard')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '登入失敗')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <!-- Theme Toggle -->
    <button
      class="theme-toggle"
      @click="toggleTheme"
      :title="isDark ? '切換到淺色模式' : '切換到深色模式'"
    >
      <svg v-if="!isDark" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
      </svg>
      <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="5"></circle>
        <line x1="12" y1="1" x2="12" y2="3"></line>
        <line x1="12" y1="21" x2="12" y2="23"></line>
        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
        <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
        <line x1="1" y1="12" x2="3" y2="12"></line>
        <line x1="21" y1="12" x2="23" y2="12"></line>
        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
        <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
      </svg>
    </button>

    <!-- Decorative Elements -->
    <div class="decoration decoration-1"></div>
    <div class="decoration decoration-2"></div>
    <div class="decoration decoration-3"></div>

    <div class="login-card animate-scale-in">
      <!-- Logo Section -->
      <div class="login-logo">
        <div class="logo-icon">
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
            <circle cx="24" cy="24" r="20" fill="currentColor" opacity="0.15"/>
            <path d="M24 8C15.163 8 8 15.163 8 24s7.163 16 16 16 16-7.163 16-16S32.837 8 24 8z" fill="currentColor"/>
            <path d="M32 24c0 4.418-3.582 8-8 8s-8-3.582-8-8 3.582-8 8-8 8 3.582 8 8z" fill="white"/>
            <circle cx="20" cy="22" r="2" fill="white"/>
            <circle cx="28" cy="22" r="2" fill="white"/>
            <path d="M20 28c0 2.209 1.791 4 4 4s4-1.791 4-4" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <h1 class="login-title">寶寶發展管理後台</h1>
        <p class="login-subtitle">追蹤與管理寶寶的成長里程碑</p>
      </div>

      <!-- Login Form -->
      <el-form
        :model="loginForm"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item>
          <el-input
            v-model="loginForm.email"
            type="email"
            placeholder="電子郵件"
            size="large"
            :prefix-icon="'User'"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密碼"
            size="large"
            :prefix-icon="'Lock'"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            {{ loading ? '登入中...' : '登入' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- Footer -->
      <div class="login-footer">
        <p class="footer-text">© 2024 Baby Development Platform</p>
      </div>
    </div>

    <!-- Background Pattern -->
    <div class="background-pattern"></div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: var(--color-bg-primary);
}

/* Theme Toggle Button */
.theme-toggle {
  position: fixed;
  top: 24px;
  right: 24px;
  width: 44px;
  height: 44px;
  border-radius: var(--radius-full);
  border: 1px solid var(--color-border-default);
  background: var(--color-bg-elevated);
  color: var(--color-text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-base);
  z-index: 100;
  box-shadow: var(--shadow-md);
}

.theme-toggle:hover {
  background: var(--color-bg-secondary);
  color: var(--color-text-primary);
  transform: scale(1.05);
  box-shadow: var(--shadow-lg);
}

.theme-toggle svg {
  transition: transform var(--transition-base);
}

.theme-toggle:hover svg {
  transform: rotate(15deg);
}

/* Decorative Elements */
.decoration {
  position: absolute;
  border-radius: var(--radius-full);
  opacity: 0.6;
  animation: float 6s ease-in-out infinite;
}

.decoration-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
  background: radial-gradient(circle, var(--color-primary-soft) 0%, transparent 70%);
  animation-delay: 0s;
}

.decoration-2 {
  width: 250px;
  height: 250px;
  bottom: -80px;
  left: -80px;
  background: radial-gradient(circle, var(--color-secondary-soft) 0%, transparent 70%);
  animation-delay: 2s;
}

.decoration-3 {
  width: 200px;
  height: 200px;
  top: 50%;
  left: 10%;
  background: radial-gradient(circle, var(--color-primary-soft) 0%, transparent 70%);
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-20px) scale(1.05);
  }
}

/* Login Card */
.login-card {
  width: 100%;
  max-width: 440px;
  padding: 48px;
  background: var(--color-bg-elevated);
  border-radius: var(--radius-xl);
  border: 1px solid var(--color-border-subtle);
  box-shadow: var(--shadow-xl);
  position: relative;
  z-index: 1;
  margin: 24px;
}

/* Logo Section */
.login-logo {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 24px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-secondary));
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: var(--shadow-lg);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.login-title {
  font-family: var(--font-display);
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 8px;
  letter-spacing: -0.02em;
}

.login-subtitle {
  font-size: 0.9375rem;
  color: var(--color-text-tertiary);
  font-weight: 500;
}

/* Login Form */
.login-form {
  margin-bottom: 32px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.login-form :deep(.el-input__wrapper) {
  padding: 12px 16px;
  font-size: 0.9375rem;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 1rem;
  font-weight: 600;
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-light));
  border: none;
  box-shadow: var(--shadow-md);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.login-button:active {
  transform: translateY(0);
}

/* Footer */
.login-footer {
  text-align: center;
  padding-top: 24px;
  border-top: 1px solid var(--color-border-subtle);
}

.footer-text {
  font-size: 0.8125rem;
  color: var(--color-text-tertiary);
  margin: 0;
}

/* Background Pattern */
.background-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
    radial-gradient(circle at 20% 80%, var(--color-primary-soft) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, var(--color-secondary-soft) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, var(--color-primary-soft) 0%, transparent 40%);
  opacity: 0.4;
  z-index: 0;
}

/* Responsive */
@media (max-width: 640px) {
  .login-card {
    padding: 32px 24px;
    margin: 16px;
  }

  .login-title {
    font-size: 1.5rem;
  }

  .logo-icon {
    width: 56px;
    height: 56px;
  }

  .decoration {
    opacity: 0.3;
  }
}
</style>
