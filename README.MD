# Baby Development Backend

A comprehensive backend system for tracking baby development milestones, including vaccination schedules, growth progress, and educational content.

## Project Structure

```
baby-development-backend/
├── api/                          # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/dean/baby/
│   │       ├── controller/       # REST Controllers
│   │       ├── service/          # Business Logic
│   │       ├── repository/       # Data Access
│   │       ├── entity/           # JPA Entities
│   │       ├── dto/              # Data Transfer Objects
│   │       ├── config/           # Configuration Classes
│   │       ├── aspect/           # AOP Aspects
│   │       └── exception/        # Exception Handling
│   └── src/main/resources/
│       └── logback-spring.xml    # Logging Configuration
├── vue-app/                      # Vue.js Frontend
├── common/                       # Shared Utilities
├── docker-compose.yml            # Basic Docker Setup
├── docker-compose-with-elk.yml   # Full Stack with ELK
├── kibana-setup.sh               # Auto-setup Kibana Dashboard
├── dashboard.ndjson              # Kibana Dashboard Template
├── logstash-docker.conf          # Logstash Pipeline Config
└── check-elk.sh                  # ELK Health Check Script
```

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Security** with JWT Authentication
- **Spring Data JPA** with Hibernate
- **MySQL** Database
- **Redis** for Caching
- **Flyway** for Database Migrations

### Frontend
- **Vue.js 3**
- **TypeScript**
- **Element Plus** UI Components
- **Pinia** State Management
- **Axios** HTTP Client

### Infrastructure
- **Docker** & **Docker Compose**
- **ELK Stack** (Elasticsearch, Logstash, Kibana) for Logging

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for frontend development)
- MySQL 8.0+
- Redis

### Option 1: Full Stack with ELK (Recommended)

```bash
# Start all services (API, ELK Stack)
docker-compose -f docker-compose-with-elk.yml up -d --build

# View logs
docker-compose -f docker-compose-with-elk.yml logs -f
```

Services will be available at:
- **API**: http://localhost:8085
- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200

### Option 2: Basic Docker Setup

```bash
# Start API only
docker-compose up -d --build
```

### Option 3: Local Development

```bash
# Build project
./mvnw clean install

# Run API
./mvnw -pl api spring-boot:run

# Run Vue App
cd vue-app
npm install
npm run dev
```

## ELK Stack Integration

The project includes integrated logging with the ELK Stack for centralized log management and visualization.

### Architecture

```
┌─────────────┐    TCP:5000     ┌───────────┐    HTTP:9200    ┌──────────────┐
│  API (Java) │ ──────────────► │  Logstash │ ──────────────► │Elasticsearch │
│  Logback    │   JSON Lines    │  Filter   │   Index         │   Storage    │
└─────────────┘                 └───────────┘                 └──────────────┘
                                                                     │
                                                               HTTP:5601
                                                                     ▼
                                                               ┌────────┐
                                                               │ Kibana │
                                                               │ Dashboard│
                                                               └────────┘
```

### Auto-Setup Features

When starting with `docker-compose-with-elk.yml`, the following are automatically configured:

1. **Data View**: `baby-development-backend-*` index pattern
2. **Dashboard**: "Baby Backend Dashboard" with visualizations
3. **Visualizations**:
   - Logs Over Time (histogram by level)
   - Log Count by Level (pie chart)
   - Recent Logs (search table)

### Kibana Dashboard

Access the pre-configured dashboard at:
- URL: http://localhost:5601/app/dashboards#/view/baby-backend-dashboard
- Navigation: Analytics > Dashboards > Baby Backend Dashboard

### Log Fields

| Field | Description |
|-------|-------------|
| `@timestamp` | Log timestamp |
| `level` | Log level (INFO, WARN, ERROR) |
| `logger_name` | Logger class name |
| `message` | Log message |
| `thread_name` | Thread name |
| `requestId` | Request trace ID |
| `executionTime` | API execution time (ms) |
| `clientIp` | Client IP address |
| `method` | HTTP method |
| `controller` | Controller name |
| `status` | Request status (SUCCESS/FAILED) |

### Useful Kibana Queries

```
# View API requests
message: "API_REQUEST*"

# View errors only
level: "ERROR"

# Trace by request ID
requestId: "your-request-id"

# View specific endpoint
message: "/api/babies*"
```

## API Documentation

Access Swagger UI at: http://localhost:8085/swagger-ui.html

### Main Endpoints

| Endpoint | Description |
|----------|-------------|
| `/api/auth/**` | Authentication endpoints |
| `/api/babies/**` | Baby management |
| `/api/milestones/**` | Milestone tracking |
| `/api/progress/**` | Progress tracking |
| `/api/articles/**` | Educational articles |
| `/api/videos/**` | Video content |
| `/api/flashcards/**` | Flashcard management |
| `/api/categories/**` | Category management |

## Common Commands

### Docker Operations

```bash
# View service status
docker-compose -f docker-compose-with-elk.yml ps

# View logs
docker-compose -f docker-compose-with-elk.yml logs -f api
docker-compose -f docker-compose-with-elk.yml logs -f elasticsearch

# Restart services
docker-compose -f docker-compose-with-elk.yml restart api

# Rebuild and restart API
./mvnw clean install -pl api -am && docker-compose -f docker-compose-with-elk.yml up -d --build --no-deps api

# Stop all services (keep data)
docker-compose -f docker-compose-with-elk.yml down

# Stop and remove all data
docker-compose -f docker-compose-with-elk.yml down -v
```

### ELK Diagnostics

```bash
# Run health check script
./check-elk.sh

# Check Elasticsearch health
curl http://localhost:9200/_cluster/health?pretty

# Check log indices
curl http://localhost:9200/_cat/indices?v

# Check Kibana status
curl http://localhost:5601/api/status
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `LOGSTASH_ENABLED` | `false` | Enable Logstash logging |
| `LOGSTASH_HOST` | `localhost` | Logstash host |
| `LOGSTASH_PORT` | `5000` | Logstash TCP port |
| `SPRING_PROFILES_ACTIVE` | `dev` | Spring profile |

### Profiles

- **dev**: Development mode with console logging
- **prod**: Production mode with file and Logstash logging

## Production Deployment

### Architecture

```
                        ┌─────────────────────────────────────────┐
                        │              Cloudflare                 │
                        │  ┌─────────────────────────────────┐   │
                        │  │   Vue App (Cloudflare Pages)    │   │
                        │  │   https://app.your-domain.com   │   │
                        │  └──────────────┬──────────────────┘   │
                        └─────────────────┼───────────────────────┘
                                          │ HTTPS
                                          ▼
                        ┌─────────────────────────────────────────┐
                        │           GCP Compute Engine            │
                        │  ┌─────────────────────────────────┐   │
                        │  │         Nginx Reverse Proxy     │   │
                        │  │   api.your-domain.com → :8085   │   │
                        │  │   kibana.your-domain.com → :5601│   │
                        │  └──────┬────────────────┬─────────┘   │
                        │         │                │              │
                        │  ┌──────▼─────┐   ┌──────▼─────┐       │
                        │  │  API (Java)│   │   Kibana   │       │
                        │  │   :8085    │   │   :5601    │       │
                        │  └──────┬─────┘   └──────┬─────┘       │
                        │         │                │              │
                        │  ┌──────▼─────┐   ┌──────▼─────┐       │
                        │  │  Logstash  │   │Elasticsearch│       │
                        │  │   :5000    │   │   :9200    │       │
                        │  └────────────┘   └────────────┘       │
                        │         │                               │
                        │  ┌──────▼─────┐                        │
                        │  │   Redis    │                        │
                        │  │   :6379    │                        │
                        │  └────────────┘                        │
                        └─────────────────┬───────────────────────┘
                                          │
                                          ▼
                        ┌─────────────────────────────────────────┐
                        │      MySQL (35.212.228.226:3306)        │
                        │           現有資料庫                    │
                        └─────────────────────────────────────────┘
```

### Prerequisites

- GCP Account with Compute Engine enabled
- Cloudflare Account (for DNS and Pages)
- Domain name configured in Cloudflare

---

### Part 1: GCP Compute Engine 設定

#### 1.1 建立 VM 執行個體

```bash
# 使用 gcloud CLI 建立 VM
gcloud compute instances create baby-backend-prod \
  --project=your-project-id \
  --zone=asia-east1-a \
  --machine-type=e2-medium \
  --image-family=ubuntu-2204-lts \
  --image-project=ubuntu-os-cloud \
  --boot-disk-size=50GB \
  --tags=http-server,https-server
```

#### 1.2 取得靜態 IP

```bash
# 保留靜態外部 IP
gcloud compute addresses create baby-backend-ip \
  --region=asia-east1

# 查看分配的 IP
gcloud compute addresses describe baby-backend-ip \
  --region=asia-east1 --format='get(address)'
```

#### 1.3 連線到 VM 並執行初始設定

```bash
# SSH 連線
gcloud compute ssh baby-backend-prod --zone=asia-east1-a

# 下載並執行設定腳本
git clone https://github.com/your-username/baby-development-backend.git
cd baby-development-backend
chmod +x scripts/*.sh deploy.sh
./scripts/setup-gcp.sh

# 重新登入以套用 Docker 群組
exit
gcloud compute ssh baby-backend-prod --zone=asia-east1-a
```

---

### Part 2: Cloudflare DNS 設定

在 Cloudflare Dashboard 設定以下 DNS 記錄：

| 類型 | 名稱 | 內容 | 代理狀態 |
|------|------|------|----------|
| A | api | GCP_VM_IP | 已代理 (橘色雲) |
| A | kibana | GCP_VM_IP | 已代理 (橘色雲) |
| CNAME | app | your-project.pages.dev | 已代理 (橘色雲) |

#### SSL/TLS 設定

- 模式: **Full (strict)**
- Always Use HTTPS: **On**
- Minimum TLS Version: **1.2**

---

### Part 3: 部署後端到 GCP VM

#### 3.1 設定環境變數

```bash
cd ~/baby-development-backend

# 複製環境變數範本
cp .env.prod.example .env.prod

# 編輯環境變數
nano .env.prod
```

填入以下必要資訊：

```env
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://35.212.228.226:3306/baby_development
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password

# JWT (使用 openssl rand -base64 64 產生)
JWT_SECRET=your-super-secure-jwt-secret

# CORS
CORS_ALLOWED_ORIGINS=https://app.your-domain.com

# Domains
API_DOMAIN=api.your-domain.com
KIBANA_DOMAIN=kibana.your-domain.com
```

#### 3.2 設定 Kibana 密碼保護

```bash
# 安裝 htpasswd 工具 (如果尚未安裝)
sudo apt-get install apache2-utils -y

# 建立密碼檔案
htpasswd -c nginx/htpasswd admin
# 輸入密碼兩次
```

#### 3.3 執行部署

```bash
# 一鍵部署
./deploy.sh

# 或手動執行
docker compose -f docker-compose.prod.yml --env-file .env.prod up -d --build
```

#### 3.4 設定 SSL 憑證

```bash
# 取得 Let's Encrypt 憑證
./scripts/get-cert.sh
```

---

### Part 4: 部署前端到 Cloudflare Pages

#### 4.1 連接 GitHub Repository

1. 到 Cloudflare Dashboard > Pages
2. 選擇 "Create a project" > "Connect to Git"
3. 選擇 GitHub 和 `baby-development-backend` repository

#### 4.2 建置設定

```
Framework preset: Vue.js
Build command: npm run build
Build output directory: dist
Root directory: vue-app
```

#### 4.3 環境變數

```
VITE_API_BASE_URL=https://api.your-domain.com
NODE_VERSION=18
```

#### 4.4 自訂網域

1. 到 Pages project > Custom domains
2. 新增 `app.your-domain.com`

---

### Part 5: 驗證部署

#### 5.1 檢查服務狀態

```bash
# 在 GCP VM 上執行
docker compose -f docker-compose.prod.yml ps

# 執行健康檢查腳本
./scripts/check-elk.sh
```

#### 5.2 檢查外部存取

```bash
# API 健康檢查
curl https://api.your-domain.com/actuator/health

# Kibana (需要 Basic Auth)
curl -u admin https://kibana.your-domain.com/api/status

# 前端
curl https://app.your-domain.com
```

---

### 部署檔案清單

| 檔案 | 說明 |
|------|------|
| `docker-compose.prod.yml` | 生產環境 Docker Compose |
| `nginx/nginx.conf` | Nginx 反向代理配置 |
| `nginx/htpasswd` | Kibana Basic Auth 密碼檔 |
| `.env.prod` | 環境變數 (不提交到 Git) |
| `.env.prod.example` | 環境變數範例 |
| `api/src/main/resources/application-prod.properties` | Spring 生��配置 |
| `vue-app/.env.production` | Vue 生產環境變數 |
| `deploy.sh` | 一鍵部署腳本 |
| `scripts/setup-gcp.sh` | GCP 初始設定腳本 |
| `scripts/check-elk.sh` | ELK 健康檢查腳本 |
| `scripts/get-cert.sh` | SSL 憑證設定腳本 |

---

### 維護操作

#### 更新 API

```bash
# 拉取最新代碼
git pull

# 重新建置並部署
./deploy.sh --build-jar
```

#### 查看日誌

```bash
# 即時日誌
docker compose -f docker-compose.prod.yml logs -f

# 特定服務
docker compose -f docker-compose.prod.yml logs -f api
```

#### 重啟服務

```bash
# 重啟特定服務
docker compose -f docker-compose.prod.yml restart api

# 重啟所有服務
docker compose -f docker-compose.prod.yml restart
```

---

### 安全檢查清單

- [ ] 資料庫密碼使用環境變數
- [ ] JWT Secret 足夠複雜 (64+ 字元)
- [ ] CORS 只允許前端網域
- [ ] Kibana 有密碼保護
- [ ] SSL/TLS 已啟用
- [ ] Elasticsearch 不對外暴露
- [ ] Redis 不對外暴露
- [ ] .env.prod 檔案已加入 .gitignore

## License

This project is private and proprietary.

## Contributing

1. Create a feature branch
2. Make your changes
3. Submit a pull request to `develop` branch
