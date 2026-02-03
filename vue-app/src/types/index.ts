// 用戶相關類型
export interface User {
  id: string
  username: string
  email: string
  createdAt?: string
}

// 寶寶相關類型
export interface Baby {
  id: string
  name: string
  birthdate: string
  gender: 'MALE' | 'FEMALE'
  userId: string
  createdAt?: string
}

// 分類相關類型
export interface Category {
  id: string
  name: LangFieldObject
  createdAt?: string
}

// 年齡段相關類型
export interface Age {
  id: string
  displayName: LangFieldObject
  month: number
  createdAt?: string
}

// 多語言欄位物件
export interface LangFieldObject {
  en?: string
  tw?: string
  cn?: string
  ja?: string
  ko?: string
  vi?: string
}

// 里程碑相關類型
export interface Milestone {
  id: string
  description: string
  descriptionObject?: LangFieldObject
  age?: AgeDto
  category?: CategoryDto
  videoUrl?: string
  imageBase64?: string
  flashcards?: FlashcardSummaryDTO[]
  ageId?: string
  categoryId?: string
  ageName?: string
  categoryName?: string
  createdAt?: string
}

// Age DTO
export interface AgeDto {
  id: string
  month: number
  displayName: string
  displayNameObject?: LangFieldObject
}

// Category DTO
export interface CategoryDto {
  id: string
  name: string
}

// Flashcard Summary DTO
export interface FlashcardSummaryDTO {
  id: string
  question: string
  answer: string
}

// 文章相關類型
export interface Article {
  id: string
  title: string
  content: string
  summary?: string
  categoryId?: string
  category?: Category
  coverImage?: string
  createdAt?: string
}

// 閃卡相關類型
export interface FlashCard {
  id: string
  question: string
  answer: string
  categoryId?: string
  category?: Category
  ageId?: string
  age?: Age
  milestoneId?: string
  milestone?: Milestone
  createdAt?: string
}

// 影片相關類型
export interface Video {
  id: string
  title: string
  description?: string
  url: string
  thumbnailUrl?: string
  duration?: number
  milestoneId?: string
  milestone?: Milestone
  articleId?: string
  article?: Article
  flashCardIds?: string[]
  createdAt?: string
}

// 進度相關類型
export interface Progress {
  id: string
  babyId: string
  milestoneId: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED'
  baby?: Baby
  milestone?: Milestone
  completedAt?: string
  createdAt?: string
}

// 儀表板統計類型
export interface DashboardStats {
  totalMilestones: number
  totalFlashCards: number
  totalArticles: number
  totalCategories: number
  totalAges: number
}

// 分頁響應類型
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

// API 響應類型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}
