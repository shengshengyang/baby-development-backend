import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types'
import { authApi } from '@/api'

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))

  // 設置 token
  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 清除 token
  function clearToken() {
    token.value = null
    localStorage.removeItem('token')
    user.value = null
  }

  // 獲取當前用戶信息
  async function getCurrentUser() {
    try {
      const data = await authApi.getCurrentUser()
      user.value = data
      return data
    } catch (error) {
      clearToken()
      throw error
    }
  }

  // 登入
  async function login(email: string, password: string) {
    const data = await authApi.login({ email, password })
    setToken(data.token)
    await getCurrentUser()
  }

  // 登出
  async function logout() {
    try {
      await authApi.logout()
    } finally {
      clearToken()
    }
  }

  // 檢查是否已登入
  function isAuthenticated(): boolean {
    return !!token.value
  }

  return {
    user,
    token,
    setToken,
    clearToken,
    getCurrentUser,
    login,
    logout,
    isAuthenticated,
  }
})
