#!/bin/bash

echo "=== ELK Stack 診斷腳本 ==="
echo ""

# 1. 檢查 Elasticsearch
echo "1. 檢查 Elasticsearch..."
ES_STATUS=$(curl -s http://localhost:9200/_cluster/health 2>/dev/null)
if [ $? -eq 0 ]; then
    echo "   ✓ Elasticsearch 運行中"
    echo "   狀態: $(echo $ES_STATUS | grep -o '"status":"[^"]*"')"
else
    echo "   ✗ Elasticsearch 無回應"
fi

# 2. 檢查索引
echo ""
echo "2. 檢查 Log 索引..."
INDICES=$(curl -s http://localhost:9200/_cat/indices?v 2>/dev/null | grep baby)
if [ -n "$INDICES" ]; then
    echo "   ✓ 找到索引:"
    echo "$INDICES"
else
    echo "   ✗ 沒有找到 baby-development-backend 索引"
    echo "   可能原因: API 尚未產生日誌"
fi

# 3. 檢查索引中的文件數量
echo ""
echo "3. 檢查日誌數量..."
DOC_COUNT=$(curl -s http://localhost:9200/baby-development-backend-*/_count 2>/dev/null | grep -o '"count":[0-9]*' | cut -d':' -f2)
if [ -n "$DOC_COUNT" ] && [ "$DOC_COUNT" -gt 0 ]; then
    echo "   ✓ 日誌數量: $DOC_COUNT"
else
    echo "   ✗ 沒有日誌資料"
fi

# 4. 檢查 Logstash
echo ""
echo "4. 檢查 Logstash..."
LOGSTASH_STATUS=$(curl -s http://localhost:9600/_node/stats 2>/dev/null | grep -o '"status":"[^"]*"')
if [ $? -eq 0 ]; then
    echo "   ✓ Logstash 運行中 - $LOGSTASH_STATUS"
else
    echo "   ✗ Logstash 無回應"
fi

# 5. 檢查 Kibana
echo ""
echo "5. 檢查 Kibana..."
KIBANA_STATUS=$(curl -s http://localhost:5601/api/status 2>/dev/null | grep -o '"level":"[^"]*"')
if [ $? -eq 0 ]; then
    echo "   ✓ Kibana 運行中 - $KIBANA_STATUS"
else
    echo "   ✗ Kibana 無回應"
fi

# 6. 檢查 Data View
echo ""
echo "6. 檢查 Data View..."
DATA_VIEWS=$(curl -s http://localhost:5601/api/data_views 2>/dev/null | grep -o '"title":"baby-development-backend-\*"')
if [ -n "$DATA_VIEWS" ]; then
    echo "   ✓ Data View 已建立"
else
    echo "   ✗ Data View 未建立"
fi

# 7. 顯示最近的日誌範例
echo ""
echo "7. 最近的日誌範例..."
curl -s "http://localhost:9200/baby-development-backend-*/_search?size=1&sort=@timestamp:desc" 2>/dev/null | python3 -m json.tool 2>/dev/null || \
curl -s "http://localhost:9200/baby-development-backend-*/_search?size=1&sort=@timestamp:desc" 2>/dev/null

echo ""
echo "=== 診斷完成 ==="
