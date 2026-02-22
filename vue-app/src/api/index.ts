import { request } from '@/utils/request'

// 用戶相關 API
export const authApi = {
  // 登入
  login(data: { email: string; password: string }) {
    return request.post('/auth/login', data)
  },
  // 註冊
  register(data: { email: string; password: string; username: string }) {
    return request.post('/auth/create', data)
  },
  // 登出
  logout() {
    return request.post('/auth/logout')
  },
  // 獲取當前用戶信息
  getCurrentUser() {
    return request.get('/user')
  },
}

// 寶寶相關 API
export const babyApi = {
  // 獲取所有寶寶
  getList() {
    return request.get('/baby')
  },
  // 創建寶寶
  create(data: any) {
    return request.post('/baby', data)
  },
  // 更新寶寶
  update(id: string, data: any) {
    return request.put(`/baby/${id}`, data)
  },
  // 刪除寶寶
  delete(id: string) {
    return request.delete(`/baby/${id}`)
  },
}

// 里程碑相關 API
export const milestoneApi = {
  // 獲取里程碑列表
  getList(params?: { ageId?: string; categoryId?: string }) {
    return request.get('/milestone', { params })
  },
  // 獲取單個里程碑
  getById(id: string) {
    return request.get(`/milestone/${id}`)
  },
  // 創建里程碑 - expects MilestoneDTO with nested age, category objects
  create(data: {
    age: { id: string }
    category: { id: string }
    descriptionObject: Record<string, string>
    videoUrl?: string
    imageBase64?: string
  }) {
    return request.post('/milestone', data)
  },
  // 更新里程碑 - expects MilestoneDTO with nested age, category objects
  update(id: string, data: {
    age: { id: string }
    category: { id: string }
    descriptionObject: Record<string, string>
    videoUrl?: string
    imageBase64?: string
  }) {
    return request.put(`/milestone/${id}`, data)
  },
  // 刪除里程碑
  delete(id: string) {
    return request.delete(`/milestone/${id}`)
  },
}

// 分類相關 API
export const categoryApi = {
  // 獲取所有分類
  getList() {
    return request.get('/categories')
  },
  // 獲取單個分類
  getById(id: string) {
    return request.get(`/categories/${id}`)
  },
  // 創建分類 - expects { name: LangFieldObject }
  create(data: { name: Record<string, string> }) {
    return request.post('/categories', data.name)
  },
  // 更新分類 - expects { name: LangFieldObject }
  update(id: string, data: { name: Record<string, string> }) {
    return request.put(`/categories/${id}`, data.name)
  },
  // 刪除分類
  delete(id: string) {
    return request.delete(`/categories/${id}`)
  },
}

// 年齡相關 API
export const ageApi = {
  // 獲取所有年齡段
  getList() {
    return request.get('/ages')
  },
  // 獲取單個年齡段
  getById(id: string) {
    return request.get(`/ages/${id}`)
  },
  // 創建年齡段 - expects { ageDto: { month }, displayName: LangFieldObject }
  create(data: { month: number; displayName: Record<string, string> }) {
    return request.post('/ages', {
      ageDto: { month: data.month },
      displayName: data.displayName,
    })
  },
  // 更新年齡段 - expects { ageDto: { month }, displayName: LangFieldObject }
  update(id: string, data: { month: number; displayName: Record<string, string> }) {
    return request.put(`/ages/${id}`, {
      ageDto: { month: data.month },
      displayName: data.displayName,
    })
  },
  // 刪除年齡段
  delete(id: string) {
    return request.delete(`/ages/${id}`)
  },
}

// 文章相關 API
export const articleApi = {
  // 獲取文章列表
  getList(params?: { page?: number; size?: number; categoryId?: string }) {
    return request.get('/article', { params })
  },
  // 獲取單篇文章
  getById(id: string) {
    return request.get(`/article/${id}`)
  },
  // 創建文章
  create(data: any) {
    return request.post('/article', data)
  },
  // 更新文章
  update(id: string, data: any) {
    return request.put(`/article/${id}`, data)
  },
  // 刪除文章
  delete(id: string) {
    return request.delete(`/article/${id}`)
  },
}

// 閃卡相關 API
export const flashcardApi = {
  // 獲取閃卡列表
  getList(params?: { page?: number; size?: number }) {
    return request.get('/flashcard', { params })
  },
  // 獲取單個閃卡
  getById(id: string) {
    return request.get(`/flashcard/${id}`)
  },
  // 創建閃卡
  create(data: any) {
    return request.post('/flashcard', data)
  },
  // 更新閃卡
  update(id: string, data: any) {
    return request.put(`/flashcard/${id}`, data)
  },
  // 刪除閃卡
  delete(id: string) {
    return request.delete(`/flashcard/${id}`)
  },
  // 關聯影片
  linkVideo(flashcardId: string, videoId: string) {
    return request.post(`/flashcard/${flashcardId}/video/${videoId}`)
  },
  // 取消關聯影片
  unlinkVideo(flashcardId: string, videoId: string) {
    return request.delete(`/flashcard/${flashcardId}/video/${videoId}`)
  },
}

// 影片相關 API
export const videoApi = {
  // 獲取影片列表
  getList(params?: { page?: number; size?: number }) {
    return request.get('/videos', { params })
  },
  // 獲取單個影片
  getById(id: string) {
    return request.get(`/videos/${id}`)
  },
  // 創建影片
  create(data: any) {
    return request.post('/videos', data)
  },
  // 更新影片
  update(id: string, data: any) {
    return request.put(`/videos/${id}`, data)
  },
  // 刪除影片
  delete(id: string) {
    return request.delete(`/videos/${id}`)
  },
}

// 進度相關 API
export const progressApi = {
  // 獲取寶寶進度
  getByBaby(babyId: string) {
    return request.get(`/progress/baby/${babyId}`)
  },
  // 更新進度
  update(data: { babyId: string; milestoneId: string; status: string }) {
    return request.post('/progress', data)
  },
  // 刪除進度
  delete(id: string) {
    return request.delete(`/progress/${id}`)
  },
}

// 儀表板相關 API
export const dashboardApi = {
  // 獲取統計數據
  getStats() {
    return request.get('/dashboard/stats')
  },
}

// 選項相關 API
export const optionApi = {
  // 獲取年齡選項
  getAges() {
    return request.get('/options/ages')
  },
  // 獲取分類選項
  getCategories() {
    return request.get('/options/categories')
  },
}
