# 寶寶發展管理後台 - Vue 前端

這是一個使用 Vue 3 + TypeScript + Vite + Element Plus 建構的寶寶發展追蹤系統管理後台。

## 技術棧

- **框架**: Vue 3 (Composition API)
- **開發語言**: TypeScript
- **構建工具**: Vite
- **UI 組件庫**: Element Plus
- **狀態管理**: Pinia
- **路由**: Vue Router 4
- **HTTP 客戶端**: Axios

## 專案結構

```
vue-app/
├── src/
│   ├── api/          # API 服務層
│   ├── assets/       # 靜態資源
│   ├── components/   # 通用組件
│   ├── layouts/      # 佈局組件
│   ├── router/       # 路由配置
│   ├── stores/       # Pinia 狀態管理
│   ├── types/        # TypeScript 類型定義
│   ├── utils/        # 工具函數
│   ├── views/        # 頁面組件
│   │   ├── Age/          # 年齡管理
│   │   ├── Article/      # 文章管理
│   │   ├── Baby/         # 寶寶管理
│   │   ├── Category/     # 分類管理
│   │   ├── FlashCard/    # 閃卡管理
│   │   ├── Milestone/    # 里程碑管理
│   │   └── Video/        # 影片管理
│   ├���─ App.vue
│   └── main.ts
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

## 開發指南

### 安裝依賴

```bash
npm install
```

### 啟動開發伺服器

```bash
npm run dev
```

開發伺服器將在 http://localhost:5173/ 啟動。

### 構建生產版本

```bash
npm run build
```

### 預覽生產版本

```bash
npm run preview
```

## 功能模組

### 已完成的管理功能

1. **儀表板** - 系統統計數據概覽
2. **里程碑管理** - 寶寶發展里程碑的 CRUD 操作
3. **分類管理** - 分類類別的 CRUD 操作
4. **年齡段管理** - 年齡段配置的 CRUD 操作
5. **文章管理** - 文章內容的 CRUD 操作
6. **閃卡管理** - 閃卡問題的 CRUD 操作
7. **影片管理** - 影片資源的 CRUD 操作
8. **寶寶管理** - 寶寶基本資料管理

### 用戶認證

- 登入/登出功能
- JWT Token 認證
- 路由守衛保護

## API 配置

API 基礎路徑配置於 `src/utils/request.ts` 和 `vite.config.ts`：

```typescript
// Vite Proxy 配置
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

確保後端 API 服務運行在 `http://localhost:8080`。

## 環境要求

- Node.js >= 18
- npm >= 9

## 後端 API

後端 API 專案位於 `../api` 目錄，使用 Spring Boot 建構。

## License

MIT
