#!/bin/bash
# GCP VM Initial Setup Script
# Run this script on a fresh GCP Compute Engine VM

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

log_info "Starting GCP VM setup..."

# Update system
log_info "Updating system packages..."
sudo apt-get update
sudo apt-get upgrade -y

# Install required packages
log_info "Installing required packages..."
sudo apt-get install -y \
    curl \
    wget \
    git \
    vim \
    htop \
    apache2-utils \
    ca-certificates \
    gnupg \
    lsb-release

# Install Docker
log_info "Installing Docker..."
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com | sh
    sudo usermod -aG docker $USER
    log_success "Docker installed successfully!"
else
    log_info "Docker is already installed."
fi

# Install Docker Compose
log_info "Installing Docker Compose..."
sudo apt-get install docker-compose-plugin -y

# Start Docker
log_info "Starting Docker service..."
sudo systemctl enable docker
sudo systemctl start docker

# Create application directory
log_info "Creating application directory..."
mkdir -p ~/baby-backend
cd ~/baby-backend

# Create necessary subdirectories
mkdir -p nginx certbot/conf certbot/www scripts

# Set up firewall rules (if not using GCP firewall)
log_info "Setting up local firewall rules..."
if command -v ufw &> /dev/null; then
    sudo ufw allow 22/tcp   # SSH
    sudo ufw allow 80/tcp   # HTTP
    sudo ufw allow 443/tcp  # HTTPS
    sudo ufw --force enable
    log_success "Firewall configured!"
fi

# Increase file descriptor limits for Elasticsearch
log_info "Setting system limits for Elasticsearch..."
sudo tee /etc/security/limits.d/elasticsearch.conf > /dev/null <<EOF
* soft nofile 65536
* hard nofile 65536
* soft memlock unlimited
* hard memlock unlimited
EOF

# Apply limits for current session
sudo sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" | sudo tee -a /etc/sysctl.conf

log_success "GCP VM setup complete!"
echo ""
echo "=========================================="
echo "Next steps:"
echo "=========================================="
echo "1. Log out and log back in to apply Docker group changes"
echo "   (or run: newgrp docker)"
echo ""
echo "2. Clone the repository:"
echo "   cd ~/baby-backend"
echo "   git clone https://github.com/your-username/baby-development-backend.git ."
echo ""
echo "3. Create environment file:"
echo "   cp .env.prod.example .env.prod"
echo "   nano .env.prod"
echo ""
echo "4. Create htpasswd for Kibana:"
echo "   htpasswd -c nginx/htpasswd admin"
echo ""
echo "5. Run deployment:"
echo "   ./deploy.sh"
echo "=========================================="
