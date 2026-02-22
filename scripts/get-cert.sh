#!/bin/bash
# SSL Certificate Setup Script using Let's Encrypt / Certbot
# Run this script after starting the services for the first time

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
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if .env.prod exists and load it
if [ ! -f ".env.prod" ]; then
    log_error ".env.prod file not found!"
    exit 1
fi

# Load environment variables
source .env.prod

# Validate required variables
if [ -z "$API_DOMAIN" ] || [ -z "$KIBANA_DOMAIN" ]; then
    log_error "API_DOMAIN and KIBANA_DOMAIN must be set in .env.prod"
    exit 1
fi

# Extract base domain from API_DOMAIN
BASE_DOMAIN=$(echo "$API_DOMAIN" | sed 's/^api\.//')

log_info "Setting up SSL certificates for:"
echo "  - $API_DOMAIN"
echo "  - $KIBANA_DOMAIN"
echo ""

# Check if Nginx is running
if ! docker ps --format '{{.Names}}' | grep -q "baby-nginx"; then
    log_error "Nginx container is not running. Please start services first:"
    echo "  docker compose -f docker-compose.prod.yml up -d"
    exit 1
fi

# Get certificates
log_info "Obtaining SSL certificates from Let's Encrypt..."

docker run -it --rm \
    -v ./certbot/conf:/etc/letsencrypt \
    -v ./certbot/www:/var/www/certbot \
    certbot/certbot certonly --webroot \
    -w /var/www/certbot \
    -d "$API_DOMAIN" \
    -d "$KIBANA_DOMAIN" \
    --email "${CERTBOT_EMAIL:-admin@$BASE_DOMAIN}" \
    --agree-tos \
    --no-eff-email

if [ $? -eq 0 ]; then
    log_success "SSL certificates obtained successfully!"

    # Update Nginx config if needed (pointing to correct cert path)
    log_info "Reloading Nginx..."
    docker compose -f docker-compose.prod.yml restart nginx

    log_success "Nginx reloaded with new certificates!"
else
    log_error "Failed to obtain SSL certificates."
    exit 1
fi

# Set up auto-renewal cron job
log_info "Setting up certificate auto-renewal..."
CRON_JOB="0 12 * * * docker run --rm -v $(pwd)/certbot/conf:/etc/letsencrypt -v $(pwd)/certbot/www:/var/www/certbot certbot/certbot renew --quiet && docker compose -f $(pwd)/docker-compose.prod.yml restart nginx"

if crontab -l 2>/dev/null | grep -q "certbot renew"; then
    log_info "Auto-renewal cron job already exists."
else
    (crontab -l 2>/dev/null; echo "$CRON_JOB") | crontab -
    log_success "Auto-renewal cron job added!"
fi

echo ""
echo "=========================================="
log_success "SSL Setup Complete!"
echo "=========================================="
echo ""
echo "Your services are now accessible via HTTPS:"
echo "  API:    https://$API_DOMAIN"
echo "  Kibana: https://$KIBANA_DOMAIN"
echo ""
echo "Certificates will auto-renew daily at 12:00 PM."
echo ""
