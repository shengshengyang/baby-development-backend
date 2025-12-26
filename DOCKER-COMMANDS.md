# Docker Compose ELK 常用命令快速參考

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

# 查看服務狀態
docker-compose -f docker-compose-with-elk.yml ps

# 查看即時 log
docker-compose -f docker-compose-with-elk.yml logs -f

# 查看特定服務 log
docker-compose -f docker-compose-with-elk.yml logs -f elasticsearch
docker-compose -f docker-compose-with-elk.yml logs -f api
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

## ELK 服務訪問

- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200
- **Logstash 監控**: http://localhost:9600

## 故障排除

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

# 查看最近的 log
docker-compose -f docker-compose-with-elk.yml logs --tail=100 elasticsearch
```

## 日常使用流程

```bash
# 第一次啟動
docker-compose -f docker-compose-with-elk.yml up -d --build

# 每日開發流程
docker-compose -f docker-compose-with-elk.yml restart api mvc

# 每週維護（重啟 ELK 服務）
docker-compose -f docker-compose-with-elk.yml restart elasticsearch logstash kibana

# 備份資料
./backup-elk.sh
```

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
