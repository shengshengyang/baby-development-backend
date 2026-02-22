#!/bin/bash
# ELK Stack Health Check and Diagnostics Script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[OK]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[FAIL]${NC} $1"
}

echo "=========================================="
echo "ELK Stack Health Check"
echo "=========================================="
echo ""

# Check if Docker containers are running
log_info "Checking Docker containers..."

check_container() {
    local container=$1
    local name=$2

    if docker ps --format '{{.Names}}' | grep -q "^${container}$"; then
        log_success "$name container is running"
        return 0
    else
        log_error "$name container is NOT running"
        return 1
    fi
}

check_container "baby-elasticsearch" "Elasticsearch"
ES_STATUS=$?

check_container "baby-logstash" "Logstash"
LS_STATUS=$?

check_container "baby-kibana" "Kibana"
KB_STATUS=$?

echo ""
log_info "Checking service endpoints..."

# Elasticsearch Health
echo ""
log_info "Elasticsearch:"
if curl -sf http://localhost:9200/_cluster/health 2>/dev/null | grep -q '"status":"green\|yellow"'; then
    HEALTH=$(curl -s http://localhost:9200/_cluster/health 2>/dev/null | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    log_success "Cluster health: $HEALTH"
else
    HEALTH=$(curl -s http://localhost:9200/_cluster/health 2>/dev/null | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    if [ -n "$HEALTH" ]; then
        log_warning "Cluster health: $HEALTH"
    else
        log_error "Cannot connect to Elasticsearch"
    fi
fi

# Show indices
log_info "Indices:"
curl -s http://localhost:9200/_cat/indices?v 2>/dev/null | head -10 || log_error "Cannot fetch indices"

# Logstash Health
echo ""
log_info "Logstash:"
if curl -sf http://localhost:9600/_node/stats 2>/dev/null | grep -q '"status"'; then
    log_success "Logstash is responding"
    # Show pipeline status
    curl -s http://localhost:9600/_node/stats/pipelines 2>/dev/null | grep -o '"pipeline":{"workers":[^}]*' | head -1
else
    log_warning "Logstash API not accessible (this may be normal if port not exposed)"
fi

# Kibana Health
echo ""
log_info "Kibana:"
KB_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:5601/api/status 2>/dev/null)
if [ "$KB_STATUS" == "200" ]; then
    log_success "Kibana is responding (HTTP $KB_STATUS)"
else
    log_error "Kibana returned HTTP $KB_STATUS"
fi

# Redis Health
echo ""
log_info "Redis:"
if docker exec baby-redis redis-cli ping 2>/dev/null | grep -q "PONG"; then
    log_success "Redis is responding"
    docker exec baby-redis redis-cli info server 2>/dev/null | grep -E "redis_version|uptime_in_seconds"
else
    log_error "Redis is not responding"
fi

# API Health
echo ""
log_info "API:"
API_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8085/actuator/health 2>/dev/null)
if [ "$API_STATUS" == "200" ]; then
    log_success "API is healthy (HTTP $API_STATUS)"
else
    log_error "API returned HTTP $API_STATUS"
fi

# Container resource usage
echo ""
log_info "Container resource usage:"
docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}" 2>/dev/null | head -10

# Disk usage
echo ""
log_info "Docker volume disk usage:"
docker system df 2>/dev/null

echo ""
echo "=========================================="
echo "Health Check Complete"
echo "=========================================="
echo ""
echo "Useful commands:"
echo "  View logs:     docker compose -f docker-compose.prod.yml logs -f [service]"
echo "  Restart:       docker compose -f docker-compose.prod.yml restart [service]"
echo "  Stop all:      docker compose -f docker-compose.prod.yml down"
echo "  Start all:     docker compose -f docker-compose.prod.yml up -d"
echo ""
