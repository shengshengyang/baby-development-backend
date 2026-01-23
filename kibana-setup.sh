#!/bin/bash

# 等待 Kibana 啟動
echo "Waiting for Kibana to be ready..."
until curl -s http://kibana:5601/api/status | grep -q '"level":"available"'; do
  echo "Kibana is not ready yet..."
  sleep 5
done

echo "Kibana is ready!"

# 建立 Data View
echo "Creating Data View..."
curl -X POST "http://kibana:5601/api/data_views/data_view" \
  -H "kbn-xsrf: true" \
  -H "Content-Type: application/json" \
  -d '{
        "data_view": {
           "title": "baby-development-backend-*",
           "name": "Baby Backend Logs",
           "timeFieldName": "@timestamp"
        }
      }'

echo "Data View created successfully!"
