#!/bin/bash

# Wait for Kibana to be ready
echo "Waiting for Kibana to be ready..."
until curl -s http://kibana:5601/api/status | grep -q '"level":"available"'; do
  echo "Kibana is not ready yet..."
  sleep 5
done

echo "Kibana is ready!"

# Wait for Elasticsearch index to exist (max 2 minutes)
echo "Waiting for log index to be created..."
WAIT_COUNT=0
MAX_WAIT=24
until curl -s http://elasticsearch:9200/_cat/indices | grep -q "baby-development-backend" || [ $WAIT_COUNT -ge $MAX_WAIT ]; do
  echo "Log index not created yet, waiting for logs... ($WAIT_COUNT/$MAX_WAIT)"
  sleep 5
  WAIT_COUNT=$((WAIT_COUNT + 1))
done

if [ $WAIT_COUNT -ge $MAX_WAIT ]; then
  echo "Timeout waiting for index, proceeding anyway..."
fi

# Import Dashboard and Data View using ndjson
if [ -f /dashboard.ndjson ]; then
  echo "Importing Dashboard and Data View..."

  IMPORT_RESPONSE=$(curl -s -X POST "http://kibana:5601/api/saved_objects/_import?overwrite=true" \
    -H "kbn-xsrf: true" \
    -F file=@/dashboard.ndjson)

  echo "Import response: $IMPORT_RESPONSE"

  if echo "$IMPORT_RESPONSE" | grep -q '"success":true'; then
    echo "Dashboard imported successfully!"
  else
    echo "Dashboard import may have issues, check response above."
  fi
else
  echo "ERROR: dashboard.ndjson not found!"
  exit 1
fi

echo "Setup completed!"
