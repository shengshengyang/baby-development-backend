# Baby Development Backend

> 請選擇語言 | Please select language
> [繁體中文 | English]

---

## 繁體中文版

### 1. 專案概覽

本專案為 Baby Development 後端系統，採用 Maven 多模組架構，主要分為三個模組：
- **common**：共用邏輯模組，包含所有共用的 Entity、Repository、Service 等元件。
- **api**：RESTful API 模組，提供對外服務。
- **mvc**：後台管理模組，使用 Thymeleaf 建構管理介面與自訂登入頁面。

### 2. 專案結構

```text
├── docker-compose.yml           // 一鍵啟動多服務
├── pom.xml                      // 父專案 pom，聚合所有模組
├── README.MD
├── api/                         // 對外 API 模組
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/dean/baby/api/
│       │   ├── BabyDevelopmentBackendApplication.java // API 入口
│       │   ├── config/           // 安全、國際化、Swagger 等設定
│       │   ├── controller/       // REST API 控制器
│       │   ├── service/          // 業務邏輯
│       │   └── util/             // 工具類
│       └── main/resources/
│           ├── application.properties
│           └── db/migration/     // Flyway SQL migration 檔案
├── common/                      // 共用邏輯模組
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/dean/baby/common/
│       │   ├── dto/ entity/ exception/ repository/ service/ util/
│       └── main/resources/application.properties
├── mvc/                         // 後台 MVC 模組
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/dean/baby/mvc/
│       │   ├── BabyDevelopmentMvcApplication.java // MVC 入口
│       │   ├── config/ controller/
│       └── main/resources/
│           ├── application.properties
│           ├── static/css/       // 前端樣式
│           └── templates/        // Thymeleaf 頁面
│               ├── dashboard.html ...
│               ├── age/ category/ flashcard/ milestone/ video/
```

### 3. 前置需求

- **Java**：版本 17 以上
- **Maven**：版本 3.6 以上
- **Docker**：如需容器化部署

### 4. 建置與運行

#### 建置所有模組

在專案根目錄執行：
```bash
mvn clean install
```

#### 運行單一模組

API 模組：
```bash
cd api
mvn spring-boot:run
```

MVC 模組：
```bash
cd mvc
mvn spring-boot:run
```

#### 使用 Docker Compose 啟動

如需一鍵啟動所有服務（需先設定好環境變數與資料庫）：
```bash
docker-compose up --build
```

### 5. 資料庫 Migration

API 模組下 `src/main/resources/db/migration/` 目錄，存放所有 Flyway SQL migration 檔案，包含資料表建立、結構調整、測試資料等。

### 6. 主要入口

- API：`com.dean.baby.api.BabyDevelopmentBackendApplication`
- MVC：`com.dean.baby.mvc.BabyDevelopmentMvcApplication`

### 7. 其他

- 共用模組（common）無獨立啟動入口，僅供其他模組依賴。
- 若需自訂配置，請修改各模組下的 `application.properties`。

---

如有問題請參考各模組原始碼或聯絡專案維護者。

---

## English Version

### 1. Project Overview

This project is the backend system for Baby Development, using a Maven multi-module architecture with three main modules:
- **common**: Shared logic module, including all common entities, repositories, services, etc.
- **api**: RESTful API module, providing external services.
- **mvc**: Backend management module, using Thymeleaf for admin interface and custom login page.

### 2. Project Structure

```text
├── docker-compose.yml           // One-click multi-service startup
├── pom.xml                      // Parent pom, aggregates all modules
├── README.MD
├── api/                         // API module
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/dean/baby/api/
│       │   ├── BabyDevelopmentBackendApplication.java // API entry point
│       │   ├── config/           // Security, i18n, Swagger config
│       │   ├── controller/       // REST API controllers
│       │   ├── service/          // Business logic
│       │   └── util/             // Utilities
│       └── main/resources/
│           ├── application.properties
│           └── db/migration/     // Flyway SQL migration files
├── common/                      // Common logic module
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/dean/baby/common/
│       │   ├── dto/ entity/ exception/ repository/ service/ util/
│       └── main/resources/application.properties
├── mvc/                         // Backend MVC module
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/dean/baby/mvc/
│       │   ├── BabyDevelopmentMvcApplication.java // MVC entry point
│       │   ├── config/ controller/
│       └── main/resources/
│           ├── application.properties
│           ├── static/css/       // Frontend styles
│           └── templates/        // Thymeleaf templates
│               ├── dashboard.html ...
│               ├── age/ category/ flashcard/ milestone/ video/
```

### 3. Prerequisites

- **Java**: 17+
- **Maven**: 3.6+
- **Docker**: For containerized deployment

### 4. Build & Run

#### Build All Modules

Run in project root:
```bash
mvn clean install
```

#### Run Single Module

API module:
```bash
cd api
mvn spring-boot:run
```

MVC module:
```bash
cd mvc
mvn spring-boot:run
```

#### Start with Docker Compose

To start all services at once (make sure env variables and DB are set):
```bash
docker-compose up --build
```

### 5. Database Migration

All Flyway SQL migration files are in `api/src/main/resources/db/migration/`, including table creation, schema changes, and test data.

### 6. Main Entry Classes

- API: `com.dean.baby.api.BabyDevelopmentBackendApplication`
- MVC: `com.dean.baby.mvc.BabyDevelopmentMvcApplication`

### 7. Others

- The common module has no standalone entry, only used as dependency.
- For custom config, edit `application.properties` in each module.

---

For questions, refer to source code or contact the project maintainer.
