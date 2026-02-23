#!/bin/bash
# One-click Deployment Script for Baby Development Backend
# Usage: ./deploy.sh [--build-jar]

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

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
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if .env.prod exists
if [ ! -f ".env.prod" ]; then
    log_error ".env.prod file not found!"
    log_info "Please copy .env.prod.example to .env.prod and fill in the values:"
    echo "  cp .env.prod.example .env.prod"
    echo "  nano .env.prod"
    exit 1
fi

# Build JAR if requested or if JAR doesn't exist
BUILD_JAR=false
if [ "$1" == "--build-jar" ]; then
    BUILD_JAR=true
elif [ ! -f "api/target/"*"-api-"*.jar ]; then
    log_warning "JAR file not found. Building..."
    BUILD_JAR=true
fi

if [ "$BUILD_JAR" == true ]; then
    log_info "Building API JAR..."
    if [ -f "./mvnw" ]; then
        ./mvnw clean install -pl api -am -DskipTests
    else
        mvn clean install -pl api -am -DskipTests
    fi
    log_success "JAR built successfully!"
fi

# Create necessary directories
log_info "Creating directories..."
mkdir -p nginx

# Check if htpasswd exists, create if not
if [ ! -f "nginx/htpasswd" ]; then
    log_warning "nginx/htpasswd not found!"
    log_info "Creating htpasswd file for Kibana Basic Auth..."
    if command -v htpasswd &> /dev/null; then
        htpasswd -c nginx/htpasswd admin
    else
        log_error "htpasswd command not found. Please install apache2-utils:"
        echo "  sudo apt-get install apache2-utils"
        exit 1
    fi
fi

# Pull latest images
log_info "Pulling Docker images..."
docker compose -f docker-compose.prod.yml pull

# Build and start services
log_info "Building and starting services..."
docker compose -f docker-compose.prod.yml --env-file .env.prod up -d --build

# Wait for services to be healthy
log_info "Waiting for services to be healthy..."
sleep 10

# Check service status
log_info "Service status:"
docker compose -f docker-compose.prod.yml ps

# Health checks
log_info "Running health checks..."

# Check API
if curl -sf http://localhost:8085/actuator/health > /dev/null 2>&1; then
    log_success "API is healthy!"
else
    log_warning "API health check failed. Check logs: docker compose -f docker-compose.prod.yml logs api"
fi

# Check Elasticsearch
if curl -sf http://localhost:9200/_cluster/health > /dev/null 2>&1; then
    log_success "Elasticsearch is healthy!"
else
    log_warning "Elasticsearch health check failed. Check logs: docker compose -f docker-compose.prod.yml logs elasticsearch"
fi

# Check Kibana
if curl -sf http://localhost:5601/api/status > /dev/null 2>&1; then
    log_success "Kibana is healthy!"
else
    log_warning "Kibana health check failed. Check logs: docker compose -f docker-compose.prod.yml logs kibana"
fi

# Check Redis
if docker exec baby-redis redis-cli ping | grep -q "PONG"; then
    log_success "Redis is healthy!"
else
    log_warning "Redis health check failed. Check logs: docker compose -f docker-compose.prod.yml logs redis"
fi

echo ""
log_success "Deployment complete!"
echo ""
echo "=========================================="
echo "Next steps:"
echo "=========================================="
echo "1. Configure Cloudflare SSL (Flexible mode)"
echo "   - Set SSL/TLS to Flexible in Cloudflare dashboard"
echo "   - Cloudflare will handle HTTPS and connect to server via HTTP"
echo ""
echo "2. View logs:"
echo "   docker compose -f docker-compose.prod.yml logs -f"
echo ""
echo "3. Check service status:"
echo "   docker compose -f docker-compose.prod.yml ps"
echo "=========================================="
