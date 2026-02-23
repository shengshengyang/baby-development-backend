#!/bin/bash
# SSL Certificate Setup - DEPRECATED
# This script is no longer needed as SSL is now handled by Cloudflare
# Keep this file for reference only

echo "=========================================="
echo "NOTE: SSL is now handled by Cloudflare"
echo "=========================================="
echo ""
echo "This script is deprecated. Please configure SSL in Cloudflare instead:"
echo ""
echo "1. Go to Cloudflare Dashboard > Your Domain > SSL/TLS"
echo "2. Set encryption mode to 'Flexible' (Cloudflare HTTPS -> Origin HTTP)"
echo "   or 'Full' if you have origin certificates"
echo "3. Enable 'Always Use HTTPS' in Edge Certificates"
echo ""
echo "Your services will be accessible via:"
echo "  API:    https://api.your-domain.com"
echo "  Kibana: https://kibana.your-domain.com"
echo ""
echo "The server runs HTTP only on port 80."
echo "Cloudflare handles HTTPS termination and proxies requests to your server."
echo ""
