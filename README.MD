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

#### 使用 Docker Compose 啟動（含 ELK Stack）

如需一鍵啟動所有服務並包含完整的 ELK 日誌系統：
```bash
docker-compose -f docker-compose-with-elk.yml up --build
```

此配置會啟動以下服務：
- **Elasticsearch** (端口 9200): 日誌儲存與搜尋引擎
- **Logstash** (端口 5000): 日誌處理管道
- **Kibana** (端口 5601): 日誌視覺化介面
- **API 模組** (端口 8085): 自動啟用 Logstash 日誌傳送
- **MVC 模組** (端口 8084): 自動啟用 Logstash 日誌傳送

**訪問 ELK 服務**：
- Kibana 儀表板：http://localhost:5601
- Elasticsearch API：http://localhost:9200
- Logstash 監控：http://localhost:9600

**第一次啟動注意事項**：
1. Elasticsearch 需要約 30-60 秒完成初始化
2. 等待 Elasticsearch 健康檢查通過後，Logstash 才會連接
3. 所有服務就緒後，Kibana 才能正常訪問
4. 日誌會自動發送到 Elasticsearch，可在 Kibana 中查看

**Kibana 初始設定**：
1. 打開 http://localhost:5601
2. 前往「Stack Management」>「Index Patterns」
3. 建立索引模式：`baby-development-backend-*`
4. 選擇時間欄位：`@timestamp`
5. 前往「Discover」頁面開始查詢日誌

**停止與清理**：
```bash
# 停止服務（保留所有日誌資料）
docker-compose -f docker-compose-with-elk.yml down

# 重新啟動（日誌資料會保留）
docker-compose -f docker-compose-with-elk.yml up -d

# 停止並刪除資料卷（⚠️ 清空所有日誌數據，不可恢復）
docker-compose -f docker-compose-with-elk.yml down -v
```

**Volume 持久化說明**：
- Elasticsearch 資料儲存在命名卷 `elasticsearch_data` 中
- Logstash 資料儲存在命名卷 `logstash_data` 中
- 這些 volumes 在容器停止或重啟後會**自動保留**
- 只有使用 `down -v` 參數才會刪除這些資料

**查看 Volumes 資料**：
```bash
# 列出所有相關的 volumes
docker volume ls | grep baby-dev

# 查看 elasticsearch volume 詳細資訊
docker volume inspect baby-development-backend_elasticsearch_data

# 查看 logstash volume 詳細資訊
docker volume inspect baby-development-backend_logstash_data

# 查看磁碟使用量
docker system df -v | grep baby-dev
```

**備份與還原 Volume 資料**：
```bash
# 備份 elasticsearch 資料
docker run --rm \
  -v baby-development-backend_elasticsearch_data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/elasticsearch-backup.tar.gz -C /data .

# 還原 elasticsearch 資料
docker run --rm \
  -v baby-development-backend_elasticsearch_data:/data \
  -v $(pwd):/backup \
  alpine tar xzf /backup/elasticsearch-backup.tar.gz -C /data
```

### 5. 資料庫 Migration

API 模組下 `src/main/resources/db/migration/` 目錄，存放所有 Flyway SQL migration 檔案，包含資料表建立、結構調整、測試資料等。

### 6. 日誌系統與 ELK 整合

本專案使用 **Logback** 作為日誌框架，並整合 **Logstash** 將日誌傳送至 **ELK Stack**（Elasticsearch, Logstash, Kibana）進行集中管理。

#### 6.1 日誌功能

- **自動請求日誌**：使用 AOP 切面自動記錄所有 API 請求/響應
- **異動日誌**：Service 層記錄資料建立、更新、刪除操作
- **結構化日誌**：支援 JSON 格式輸出，便於 ELK 分析
- **多環境支援**：開發環境輸出到控制台，生產環境輸出到檔案與 Logstash

#### 6.2 日誌配置

檔案位置：`api/src/main/resources/logback-spring.xml`

預設行為：
- **開發環境**：輸出到控制台 + 檔案
- **生產環境**：輸出到檔案（**預設不啟用 Logstash**）

日誌檔案位置：
- 一般日誌：`./logs/baby-development-backend.log`
- 錯誤日誌：`./logs/baby-development-backend-error.log`
- 自動滾動：單檔最大 100MB，保留 30 天

#### 6.3 啟用 ELK 整合

若要啟用 Logstash 傳送日誌到 ELK，需設置環境變數：

```bash
# 啟用 Logstash
export LOGSTASH_ENABLED=true
export LOGSTASH_HOST=your-logstash-host  # 預設: localhost
export LOGSTASH_PORT=5000                # 預設: 5000

# 啟動應用（生產環境）
java -jar api/target/baby-development-api.jar --spring.profiles.active=prod
```

**重要**：即使未部署 ELK 或設置錯誤，應用也能正常運作，不會報錯。

#### 6.4 ELK 設定範例

專案根目錄提供 `logstash.conf` 範例配置：

```bash
# 啟動 Logstash
logstash -f /path/to/logstash.conf
```

Logstash 配置要點：
- 輸入：TCP 連接埠 5000，接收 JSON 格式日誌
- 輸出：Elasticsearch 索引 `baby-development-backend-*`
- 處理：自動解析時間戳記、日誌等級、應用名稱等欄位

#### 6.5 Kibana 查詢範例

在 Kibana 中可使用以下查詢：

```javascript
// 查看所有 API 請求
message: "API_REQUEST*"

// 查看特定實體的變更
action: "CREATE" AND entity: "Baby"

// 查看錯誤日誌
status: "ERROR" OR level: "ERROR"

// 追蹤特定請求
requestId: "550e8400-e29b-41d4-a716-446655440000"
```

#### 6.6 自訂日誌使用

在 Service 中使用異動日誌工具：

```java
import com.dean.baby.common.util.ChangeLogUtil;

@Service
public class YourService {
    public void createEntity(YourDto dto) {
        // 業務邏輯...
        repository.save(entity);

        // 記錄異動
        ChangeLogUtil.logCreate("YourEntity", entity.getId(), dto);
    }
}
```

### 7. 主要入口

- API：`com.dean.baby.api.BabyDevelopmentBackendApplication`
- MVC：`com.dean.baby.mvc.BabyDevelopmentMvcApplication`

### 8. 其他

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

#### Start with Docker Compose (with ELK Stack)

To start all services with complete ELK logging system:
```bash
docker-compose -f docker-compose-with-elk.yml up --build
```

This configuration starts:
- **Elasticsearch** (port 9200): Log storage and search engine
- **Logstash** (port 5000): Log processing pipeline
- **Kibana** (port 5601): Log visualization interface
- **API module** (port 8085): Auto-enabled Logstash log forwarding
- **MVC module** (port 8084): Auto-enabled Logstash log forwarding

**Access ELK Services**:
- Kibana Dashboard: http://localhost:5601
- Elasticsearch API: http://localhost:9200
- Logstash Monitoring: http://localhost:9600

**First Startup Notes**:
1. Elasticsearch takes about 30-60 seconds to initialize
2. Logstash connects only after Elasticsearch health check passes
3. Kibana becomes accessible after all services are ready
4. Logs are automatically sent to Elasticsearch and viewable in Kibana

**Kibana Initial Setup**:
1. Open http://localhost:5601
2. Go to "Stack Management" > "Index Patterns"
3. Create index pattern: `baby-development-backend-*`
4. Select time field: `@timestamp`
5. Go to "Discover" page to query logs

**Stop and Cleanup**:
```bash
# Stop services (keeps all log data)
docker-compose -f docker-compose-with-elk.yml down

# Restart (log data will be preserved)
docker-compose -f docker-compose-with-elk.yml up -d

# Stop and remove volumes (⚠️ clears all log data, cannot be recovered)
docker-compose -f docker-compose-with-elk.yml down -v
```

**Volume Persistence Details**:
- Elasticsearch data is stored in named volume `elasticsearch_data`
- Logstash data is stored in named volume `logstash_data`
- These volumes are **automatically preserved** when containers stop or restart
- Only the `down -v` command will delete this data

**View Volume Data**:
```bash
# List all related volumes
docker volume ls | grep baby-dev

# Inspect elasticsearch volume details
docker volume inspect baby-development-backend_elasticsearch_data

# Inspect logstash volume details
docker volume inspect baby-development-backend_logstash_data

# Check disk usage
docker system df -v | grep baby-dev
```

**Backup and Restore Volume Data**:
```bash
# Backup elasticsearch data
docker run --rm \
  -v baby-development-backend_elasticsearch_data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/elasticsearch-backup.tar.gz -C /data .

# Restore elasticsearch data
docker run --rm \
  -v baby-development-backend_elasticsearch_data:/data \
  -v $(pwd):/backup \
  alpine tar xzf /backup/elasticsearch-backup.tar.gz -C /data
```

### 5. Database Migration

All Flyway SQL migration files are in `api/src/main/resources/db/migration/`, including table creation, schema changes, and test data.

### 6. Logging System & ELK Integration

This project uses **Logback** as the logging framework and integrates with **Logstash** to send logs to the **ELK Stack** (Elasticsearch, Logstash, Kibana) for centralized management.

#### 6.1 Logging Features

- **Automatic Request Logging**: AOP aspect automatically logs all API requests/responses
- **Change Logging**: Service layer logs data creation, updates, and deletions
- **Structured Logging**: JSON format output for easy ELK analysis
- **Multi-Environment Support**: Dev environment outputs to console, production outputs to files and Logstash

#### 6.2 Logging Configuration

File location: `api/src/main/resources/logback-spring.xml`

Default behavior:
- **Development environment**: Console + File output
- **Production environment**: File output (**Logstash disabled by default**)

Log file locations:
- General logs: `./logs/baby-development-backend.log`
- Error logs: `./logs/baby-development-backend-error.log`
- Auto-rotation: Max 100MB per file, 30 days retention

#### 6.3 Enable ELK Integration

To enable Logstash to send logs to ELK, set environment variables:

```bash
# Enable Logstash
export LOGSTASH_ENABLED=true
export LOGSTASH_HOST=your-logstash-host  # default: localhost
export LOGSTASH_PORT=5000                # default: 5000

# Start application (production mode)
java -jar api/target/baby-development-api.jar --spring.profiles.active=prod
```

**Important**: The application works normally even without ELK deployment or with misconfiguration, no errors will occur.

#### 6.4 ELK Configuration Example

The project root provides `logstash.conf` example configuration:

```bash
# Start Logstash
logstash -f /path/to/logstash.conf
```

Logstash configuration highlights:
- Input: TCP port 5000, receives JSON format logs
- Output: Elasticsearch index `baby-development-backend-*`
- Processing: Automatically parses timestamp, log level, application name, etc.

#### 6.5 Kibana Query Examples

Use these queries in Kibana:

```javascript
// View all API requests
message: "API_REQUEST*"

// View specific entity changes
action: "CREATE" AND entity: "Baby"

// View error logs
status: "ERROR" OR level: "ERROR"

// Trace specific request
requestId: "550e8400-e29b-41d4-a716-446655440000"
```

#### 6.6 Custom Logging Usage

Use change logging utility in Service:

```java
import com.dean.baby.common.util.ChangeLogUtil;

@Service
public class YourService {
    public void createEntity(YourDto dto) {
        // Business logic...
        repository.save(entity);

        // Log change
        ChangeLogUtil.logCreate("YourEntity", entity.getId(), dto);
    }
}
```

### 7. Main Entry Classes

- API: `com.dean.baby.api.BabyDevelopmentBackendApplication`
- MVC: `com.dean.baby.mvc.BabyDevelopmentMvcApplication`

### 8. Others

- The common module has no standalone entry, only used as dependency.
- For custom config, edit `application.properties` in each module.

---

For questions, refer to source code or contact the project maintainer.
