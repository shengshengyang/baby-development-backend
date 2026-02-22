# Docker Compose ELK 常用命令快速參考

## 快速開始

```bash
# 完整啟動（含 ELK Stack + 自動設定 Kibana Dashboard）
docker-compose -f docker-compose-with-elk.yml up -d --build

# 查看服務狀態
docker-compose -f docker-compose-with-elk.yml ps

# 查看日誌
docker-compose -f docker-compose-with-elk.yml logs -f
```

啟動後可訪問：
- **API**: http://localhost:8085
- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200

## 自動設定功能

使用 `docker-compose-with-elk.yml` 啟動時，會自動執行以下設定：

1. **Data View**: 自動建立 `baby-development-backend-*` 索引模式
2. **Dashboard**: 自動匯入 "Baby Backend Dashboard"
3. **Visualizations**:
   - Logs Over Time - 按時間與等級的日誌數量圖
   - Log Count by Level - 日誌等級分佈圓餅圖
   - Recent Logs - 最近日誌列表

### 設定腳本

- `kibana-setup.sh` - Kibana 自動設定腳本
- `dashboard.ndjson` - Dashboard 匯出檔案

## 啟動與停止

```bash
# 完整啟動（含 ELK）
docker-compose -f docker-compose-with-elk.yml up -d --build

# 停止服務（保留所有 log 資料）
docker-compose -f docker-compose-with-elk.yml down

# 重新啟動（log 資料會保留）
docker-compose -f docker-compose-with-elk.yml restart

# 重啟特定服務
docker-compose -f docker-compose-with-elk.yml restart api
docker-compose -f docker-compose-with-elk.yml restart elasticsearch

# 單獨重新 build 並重啟 api (使用 Maven Wrapper)
./mvnw clean install -pl api -am && docker-compose -f docker-compose-with-elk.yml up -d --build --no-deps api

# 重新執行 Kibana 設定（如需手動觸發）
docker rm -f baby-dev-kibana-setup && docker-compose -f docker-compose-with-elk.yml up -d kibana-setup
```

## Volume 管理

```bash
# 列出所有相關 volumes
docker volume ls | grep baby-dev

# 查看詳細資訊
docker volume inspect baby-development-backend_elasticsearch_data
docker volume inspect baby-development-backend_logstash_data

# 查看磁碟使用量
docker system df -v | grep baby-dev

# ⚠️ 完全刪除所有資料（不可恢復）
docker-compose -f docker-compose-with-elk.yml down -v
```

## 備份與還原

```bash
# 備份 Elasticsearch 資料
docker run --rm \
  -v baby-development-backend_elasticsearch_data:/data \
  -v "$(pwd)":/backup \
  alpine tar czf /backup/elasticsearch-backup-$(date +%Y%m%d).tar.gz -C /data .

# 還原 Elasticsearch 資料
docker run --rm \
  -v baby-development-backend_elasticsearch_data:/data \
  -v "$(pwd)":/backup \
  alpine tar xzf /backup/elasticsearch-backup-YYYYMMDD.tar.gz -C /data
```

## Kibana 使用指南

### 直接訪問 Dashboard

啟動後直接訪問：http://localhost:5601/app/dashboards#/view/baby-backend-dashboard

### 手動查看日誌

1. 前往 **Discover** 頁面
2. 選擇 `baby-development-backend-*` Data View
3. 設定時間範圍
4. 開始查詢

### 常用查詢語法

```
# 查看 API 請求
message: "API_REQUEST*"

# 查看錯誤日誌
level: "ERROR"

# 查看特定實體變更
action: "CREATE" AND entity: "Baby"

# 追蹤請求 ID
requestId: "550e8400-e29b-41d4-a716-446655440000"

# 查看回應時間超過 1 秒的請求
executionTime > 1000
```

### 日誌欄位說明

| 欄位 | 說明 |
|------|------|
| `@timestamp` | 日誌時間 |
| `level` | 日誌等級 (INFO, WARN, ERROR) |
| `logger_name` | Logger 類別名稱 |
| `message` | 日誌訊息 |
| `thread_name` | 執行緒名稱 |
| `requestId` | 請求追蹤 ID |
| `executionTime` | API 執行時間 (ms) |
| `clientIp` | 客戶端 IP |
| `method` | HTTP 方法 |
| `status` | 請���狀態 (SUCCESS/FAILED) |

## 故障排除

### ELK 健康檢查腳本

```bash
# 執行診斷腳本
./check-elk.sh
```

### 手動檢查

```bash
# 查看容器健康狀態
docker ps

# 進入容器除錯
docker exec -it baby-dev-elasticsearch bash
docker exec -it baby-dev-logstash bash
docker exec -it baby-dev-kibana bash

# Elasticsearch 健康檢查
curl http://localhost:9200/_cluster/health?pretty

# 查看索引列表
curl http://localhost:9200/_cat/indices?v

# 查看日誌數量
curl http://localhost:9200/baby-development-backend-*/_count

# 檢查 Kibana Data View
curl http://localhost:5601/api/data_views

# 檢查 Dashboard
curl http://localhost:5601/api/saved_objects/_find?type=dashboard

# 查看 kibana-setup 執行日誌
docker logs baby-dev-kibana-setup
```

### 常見問題

| 問題 | 可能原因 | 解決方法 |
|------|----------|----------|
| Dashboard 沒有出現 | kibana-setup 未執行 | 重新執行 `docker-compose -f docker-compose-with-elk.yml up -d kibana-setup` |
| 沒有日誌資料 | API 未連接 Logstash | 確認 `LOGSTASH_ENABLED=true` |
| Kibana 無法啟動 | ES 未就緒 | 等待 ES health check 通過 |
| 索引不存在 | API 尚未產生日誌 | 發送一些 API 請求產生日誌 |

## 清理資源

```bash
# 清理未使用的映像
docker image prune -a

# 清理未使用的容器
docker container prune

# 清理未使用的 volumes
docker volume prune

# ⚠️ 完整清理（會刪除所有資料）
docker-compose -f docker-compose-with-elk.yml down -v
docker system prune -a --volumes
```
