# Baby Development Backend

A comprehensive backend system for tracking baby development milestones, including vaccination schedules, growth progress, and educational content.

## Project Structure

```
baby-development-backend/
├── api/                          # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/dean/baby/
│   │       ├── controller/       # REST Controllers
│   │       ├── service/          # Business Logic
│   │       ├── repository/       # Data Access
│   │       ├── entity/           # JPA Entities
│   │       ├── dto/              # Data Transfer Objects
│   │       ├── config/           # Configuration Classes
│   │       ├── aspect/           # AOP Aspects
│   │       └── exception/        # Exception Handling
│   └── src/main/resources/
│       └── logback-spring.xml    # Logging Configuration
├── vue-app/                      # Vue.js Frontend
├── common/                       # Shared Utilities
├── docker-compose.yml            # Basic Docker Setup
├── docker-compose-with-elk.yml   # Full Stack with ELK
├── kibana-setup.sh               # Auto-setup Kibana Dashboard
├── dashboard.ndjson              # Kibana Dashboard Template
├── logstash-docker.conf          # Logstash Pipeline Config
└── check-elk.sh                  # ELK Health Check Script
```

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Security** with JWT Authentication
- **Spring Data JPA** with Hibernate
- **MySQL** Database
- **Redis** for Caching
- **Flyway** for Database Migrations

### Frontend
- **Vue.js 3**
- **TypeScript**
- **Element Plus** UI Components
- **Pinia** State Management
- **Axios** HTTP Client

### Infrastructure
- **Docker** & **Docker Compose**
- **ELK Stack** (Elasticsearch, Logstash, Kibana) for Logging

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for frontend development)
- MySQL 8.0+
- Redis

### Option 1: Full Stack with ELK (Recommended)

```bash
# Start all services (API, ELK Stack)
docker-compose -f docker-compose-with-elk.yml up -d --build

# View logs
docker-compose -f docker-compose-with-elk.yml logs -f
```

Services will be available at:
- **API**: http://localhost:8085
- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200

### Option 2: Basic Docker Setup

```bash
# Start API only
docker-compose up -d --build
```

### Option 3: Local Development

```bash
# Build project
./mvnw clean install

# Run API
./mvnw -pl api spring-boot:run

# Run Vue App
cd vue-app
npm install
npm run dev
```

## ELK Stack Integration

The project includes integrated logging with the ELK Stack for centralized log management and visualization.

### Architecture

```
┌─────────────┐    TCP:5000     ┌───────────┐    HTTP:9200    ┌──────────────┐
│  API (Java) │ ──────────────► │  Logstash │ ──────────────► │Elasticsearch │
│  Logback    │   JSON Lines    │  Filter   │   Index         │   Storage    │
└─────────────┘                 └───────────┘                 └──────────────┘
                                                                     │
                                                               HTTP:5601
                                                                     ▼
                                                               ┌────────┐
                                                               │ Kibana │
                                                               │ Dashboard│
                                                               └────────┘
```

### Auto-Setup Features

When starting with `docker-compose-with-elk.yml`, the following are automatically configured:

1. **Data View**: `baby-development-backend-*` index pattern
2. **Dashboard**: "Baby Backend Dashboard" with visualizations
3. **Visualizations**:
   - Logs Over Time (histogram by level)
   - Log Count by Level (pie chart)
   - Recent Logs (search table)

### Kibana Dashboard

Access the pre-configured dashboard at:
- URL: http://localhost:5601/app/dashboards#/view/baby-backend-dashboard
- Navigation: Analytics > Dashboards > Baby Backend Dashboard

### Log Fields

| Field | Description |
|-------|-------------|
| `@timestamp` | Log timestamp |
| `level` | Log level (INFO, WARN, ERROR) |
| `logger_name` | Logger class name |
| `message` | Log message |
| `thread_name` | Thread name |
| `requestId` | Request trace ID |
| `executionTime` | API execution time (ms) |
| `clientIp` | Client IP address |
| `method` | HTTP method |
| `controller` | Controller name |
| `status` | Request status (SUCCESS/FAILED) |

### Useful Kibana Queries

```
# View API requests
message: "API_REQUEST*"

# View errors only
level: "ERROR"

# Trace by request ID
requestId: "your-request-id"

# View specific endpoint
message: "/api/babies*"
```

## API Documentation

Access Swagger UI at: http://localhost:8085/swagger-ui.html

### Main Endpoints

| Endpoint | Description |
|----------|-------------|
| `/api/auth/**` | Authentication endpoints |
| `/api/babies/**` | Baby management |
| `/api/milestones/**` | Milestone tracking |
| `/api/progress/**` | Progress tracking |
| `/api/articles/**` | Educational articles |
| `/api/videos/**` | Video content |
| `/api/flashcards/**` | Flashcard management |
| `/api/categories/**` | Category management |

## Common Commands

### Docker Operations

```bash
# View service status
docker-compose -f docker-compose-with-elk.yml ps

# View logs
docker-compose -f docker-compose-with-elk.yml logs -f api
docker-compose -f docker-compose-with-elk.yml logs -f elasticsearch

# Restart services
docker-compose -f docker-compose-with-elk.yml restart api

# Rebuild and restart API
./mvnw clean install -pl api -am && docker-compose -f docker-compose-with-elk.yml up -d --build --no-deps api

# Stop all services (keep data)
docker-compose -f docker-compose-with-elk.yml down

# Stop and remove all data
docker-compose -f docker-compose-with-elk.yml down -v
```

### ELK Diagnostics

```bash
# Run health check script
./check-elk.sh

# Check Elasticsearch health
curl http://localhost:9200/_cluster/health?pretty

# Check log indices
curl http://localhost:9200/_cat/indices?v

# Check Kibana status
curl http://localhost:5601/api/status
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `LOGSTASH_ENABLED` | `false` | Enable Logstash logging |
| `LOGSTASH_HOST` | `localhost` | Logstash host |
| `LOGSTASH_PORT` | `5000` | Logstash TCP port |
| `SPRING_PROFILES_ACTIVE` | `dev` | Spring profile |

### Profiles

- **dev**: Development mode with console logging
- **prod**: Production mode with file and Logstash logging

## License

This project is private and proprietary.

## Contributing

1. Create a feature branch
2. Make your changes
3. Submit a pull request to `develop` branch
