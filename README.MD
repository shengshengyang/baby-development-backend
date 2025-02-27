# Baby Development Backend

## 1. 專案概覽

本專案為 Baby Development 後端系統，採用 Maven 多模組架構管理，主要分為以下三個模組：

- **common**：共用邏輯模組，存放所有共用的 Entity、Repository、Service 等元件。
- **api**：對外 API 模組，提供 RESTful 服務。
- **mvc**：後台管理模組，使用 Thymeleaf 建構管理介面與自訂登入頁面。

## 2. 專案結構

``` text
├── backend/
│   ├── pom.xml               // 父專案 pom，用來聚合所有後端模組
│   ├── common/               // 共用邏輯（Entity、Repository）
│   │   └── pom.xml
│   ├── api/                  // 對外 API 模組

│   │   └── pom.xml
│   └── mvc/                  // 後台 MVC 模組
│       └── pom.xml
├── README.md
```
## 3. 前置需求

- **Java**：版本 17 以上
- **Maven**：版本 3.6 以上

## 4. 建置與運行

### 建置所有模組

在 `backend` 目錄下執行以下命令，進行專案編譯與打包：

```bash
mvn clean install
```

### 運行單一模組

#### API 模組

```text
cd backend/api
mvn spring-boot:run
```

#### MVC 模組

```text
cd backend/mvc
mvn spring-boot:run
```

