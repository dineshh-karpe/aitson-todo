# Aitson Todo Application

A Spring Boot application with comprehensive health checks and performance monitoring using Spring Boot Actuator.

## Environment Configurations

This application supports multiple environments with specific configurations:

### Available Environments

1. **Development (dev)** - `application-dev.properties`
2. **QA** - `application-qa.properties`
3. **Staging (stg)** - `application-stg.properties`
4. **Production (prod)** - `application-prod.properties`

### Running with Different Environments

#### Development Environment
```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

#### QA Environment
```bash
./mvnw spring-boot:run -Dspring.profiles.active=qa
```

#### Staging Environment
```bash
./mvnw spring-boot:run -Dspring.profiles.active=stg
```

#### Production Environment
```bash
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

### Environment-Specific Features

#### Development (dev)
- **Database**: Local PostgreSQL with `tododb_dev`
- **DDL**: `create-drop` (recreates schema on startup)
- **Logging**: DEBUG level with SQL queries
- **Actuator**: Full exposure with detailed health information
- **Data Initialization**: Enabled

#### QA
- **Database**: QA server with `tododb_qa`
- **DDL**: `validate` (validates schema)
- **Logging**: INFO level
- **Actuator**: Limited exposure with authorization
- **Data Initialization**: Disabled

#### Staging (stg)
- **Database**: Staging server with `tododb_stg`
- **DDL**: `validate` (validates schema)
- **Logging**: WARN level
- **Actuator**: Limited exposure with authorization
- **Connection Pool**: Optimized for staging load
- **Data Initialization**: Disabled

#### Production (prod)
- **Database**: Production server with `tododb_prod`
- **DDL**: `validate` (validates schema)
- **Logging**: ERROR level only
- **Actuator**: Secure exposure with role-based access
- **Connection Pool**: High-performance configuration
- **Security**: Environment variables for credentials
- **Performance**: Optimized JPA settings
- **Error Handling**: No stack traces exposed

### Health Check Endpoints

#### Spring Boot Actuator Endpoints
- `GET /actuator/health` - Overall application health
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Performance metrics
- `GET /actuator/prometheus` - Prometheus format metrics

#### Custom Health Endpoints
- `GET /api/health/custom` - Custom comprehensive health check
- `GET /api/health/info` - Application information

### Database Configuration

Each environment has its own database configuration:

| Environment | Database URL | Database Name | Username |
|-------------|--------------|---------------|----------|
| dev | localhost:5432 | tododb_dev | postgres |
| qa | qa-db-server:5432 | tododb_qa | qa_user |
| stg | stg-db-server:5432 | tododb_stg | stg_user |
| prod | prod-db-server:5432 | tododb_prod | prod_user |

### Environment Variables (Production)

For production, use environment variables for sensitive data:

```bash
export DB_USERNAME=your_prod_username
export DB_PASSWORD=your_prod_password
```

### Monitoring and Metrics

The application provides comprehensive monitoring:

1. **Health Checks**: Database, disk space, application health
2. **Performance Metrics**: HTTP requests, JVM, system metrics
3. **Custom Health**: Detailed application and system status
4. **Prometheus Export**: Metrics in Prometheus format

### Security Considerations

- **Development**: Full debugging information exposed
- **QA/Staging**: Limited information with authorization
- **Production**: Minimal information, role-based access control

### Connection Pool Configuration

Each environment has optimized connection pool settings:

- **Development**: Default HikariCP settings
- **QA**: Moderate pool size
- **Staging**: Optimized for staging load
- **Production**: High-performance configuration with leak detection

### Logging Levels

| Environment | Root Level | Application Level | SQL Level |
|-------------|------------|-------------------|-----------|
| dev | INFO | DEBUG | DEBUG |
| qa | WARN | INFO | WARN |
| stg | WARN | WARN | ERROR |
| prod | ERROR | ERROR | ERROR |