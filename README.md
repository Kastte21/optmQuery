# OptimQuery - API de Optimización de Consultas

Proyecto Spring Boot para optimización de consultas con Java y buenas prácticas, implementando cache con Redis y métricas de rendimiento.

## Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [Optimizaciones Implementadas](#-optimizaciones-implementadas)
- [Endpoints](#-endpoints)
- [Estructura del Proyecto](#-estructura-del-proyecto)

## Características

- **API REST** para consultas optimizadas de resultados
- **Cache con Redis** para mejorar rendimiento (80-90% más rápido)
- **Paginación** automática con Spring Data
- **Filtros dinámicos** por campaña, día y mes
- **Clasificación automática** de teléfonos (TELEFONO/CELULAR/DESCONOCIDO)
- **Métricas de rendimiento** con Micrometer
- **Documentación automática** con Swagger/OpenAPI
- **Índices optimizados** en base de datos PostgreSQL

## Tecnologías

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **PostgreSQL** - Base de datos principal
- **Redis** - Cache para optimización
- **MapStruct** - Mapeo de objetos
- **Lombok** - Reducción de código boilerplate
- **Swagger/OpenAPI** - Documentación de API
- **Micrometer** - Métricas de rendimiento

## Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controller    │    │     Service     │    │   Repository    │
│   (REST API)    │◄──►│   (Business)    │◄──►│   (Data Access) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      DTO        │    │     Cache       │    │   PostgreSQL    │
│  (Response)     │    │    (Redis)      │    │   (Database)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Instalación

### Prerrequisitos

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+

### 1. Clonar el repositorio

```bash
git clone https://github.com/Kastte21/optmQuery.git
cd optimQuery
```

### 2. Configurar base de datos

```sql
-- Crear base de datos
CREATE DATABASE testdb;

-- Crear tabla (se crea automáticamente con JPA)
-- La tabla se creará automáticamente al ejecutar la aplicación
```

### 3. Configurar Redis

```bash
# Instalar Redis (Windows con WSL o Docker)
docker run -d -p 6379:6379 redis:latest

# O instalar Redis localmente
# https://redis.io/download
```

### 4. Configurar variables de entorno

```bash
# Crear archivo .env o configurar variables del sistema
DB_USER=postgres
DB_PASS=tu_contraseña
```

## ⚙️ Configuración

### Perfiles de aplicación

#### Perfil Local (`application-local.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/testdb
    username: postgres
    password: tu_contraseña
  data:
    redis:
      host: localhost
      port: 6379
```

#### Perfil de Producción

```yaml
spring:
  datasource:
    url: jdbc:postgresql://servidor:5432/proddb
    username: ${DB_USER}
    password: ${DB_PASS}
```

## 🎯 Uso

### Ejecutar la aplicación

```bash
# Con perfil local
mvn spring-boot:run "-Dspring-boot.run.profiles=local"

# Con perfil de producción
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```

### Acceder a la documentación

```
http://localhost:8081/swagger-ui.html
```

### Acceder a métricas

```
http://localhost:8081/actuator
```

## 🚀 Optimizaciones Implementadas

### 1. **Cache con Redis**

- **TTL**: 5 minutos para resultados, 1 hora para usuarios
- **Serialización**: JSON para compatibilidad
- **Configuración**: Cache por defecto de 30 minutos

### 2. **Índices de Base de Datos**

```sql
-- Índices simples
CREATE INDEX idx_resultados_listas_documento ON resultados_listas(documento);
CREATE INDEX idx_resultados_listas_telefono ON resultados_listas(telefono);
CREATE INDEX idx_resultados_listas_start ON resultados_listas(start);
CREATE INDEX idx_resultados_listas_estado ON resultados_listas(estado);

-- Índices compuestos
CREATE INDEX idx_resultados_listas_campana_start ON resultados_listas(campana, start);
CREATE INDEX idx_resultados_listas_campana_estado ON resultados_listas(campana, estado);
```

### 3. **Configuración Hibernate Optimizada**

```yaml
hibernate:
  jdbc:
    batch_size: 50
  order_inserts: true
  order_updates: true
  batch_versioned_data: true
```

### 4. **Métricas de Rendimiento**

- Tiempo de ejecución de consultas
- Métricas de cache hit/miss
- Métricas de base de datos

## 📡 Endpoints

### GET /api/resultados/resumen

Consulta optimizada de resultados con filtros y paginación.

**Parámetros:**

- `campana` (opcional, default: "nan") - Filtro por campaña
- `dia` (opcional) - Filtro por día (formato: DD)
- `mes` (opcional) - Filtro por mes (formato: MM)
- `page` (opcional, default: 0) - Número de página
- `size` (opcional, default: 20) - Tamaño de página

**Ejemplos:**

```bash
# Consulta básica
GET /api/resultados/resumen

# Con filtros
GET /api/resultados/resumen?campana=VENTAS&dia=15&mes=12&page=0&size=10

# Solo por día
GET /api/resultados/resumen?dia=01

# Solo por mes
GET /api/resultados/resumen?mes=06
```

**Respuesta:**

```json
{
  "content": [
    {
      "documento": "12345678",
      "telefono": "987654321",
      "start": "2024-01-15 10:30:00",
      "estado": "COMPLETADO",
      "obs": "CELULAR",
      "dia": "15",
      "mes": "01"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10
}
```

## 📁 Estructura del Proyecto

```
src/main/java/com/kast/optimQuery/
├── config/
│   ├── CacheConfig.java          # Configuración de Redis
│   └── MetricsConfig.java        # Configuración de métricas
├── controller/
│   └── ResultadosController.java # Controlador REST
├── dto/
│   └── ResultadosResumenDto.java # DTO de respuesta
├── mapper/
│   └── ResultadosResumenMapper.java # Mapeo con MapStruct
├── model/
│   └── ResultadosListas.java     # Entidad JPA
├── repository/
│   └── ResultadosListasRepository.java # Repositorio JPA
├── serivce/
│   └── ResultadosService.java    # Lógica de negocio
└── OptimQueryApplication.java    # Clase principal
```

## 🔧 Configuración de Desarrollo

### Variables de entorno recomendadas

```bash
# Base de datos
DB_USER=postgres
DB_PASS=tu_contraseña_segura

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# Aplicación
SERVER_PORT=8081
SPRING_PROFILES_ACTIVE=local
```

### Comandos útiles

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar con perfil específico
mvn spring-boot:run "-Dspring-boot.run.profiles=local"

# Empaquetar
mvn clean package

# Ejecutar JAR
java -jar target/optimQuery-0.0.1-SNAPSHOT.jar
```

## 📊 Monitoreo y Métricas

### Actuator Endpoints

- `/actuator/health` - Estado de la aplicación
- `/actuator/metrics` - Métricas disponibles
- `/actuator/metrics/resultados.resumen` - Métricas específicas

### Métricas disponibles

- `resultados.resumen` - Tiempo de ejecución de consultas
- `cache.gets` - Hits de cache
- `cache.misses` - Misses de cache
- `hikaricp.connections` - Conexiones de base de datos

## 🚀 Despliegue

### Docker (Recomendado)

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/optimQuery-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - postgres
      - redis
  
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: testdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
  
  redis:
    image: redis:7
    ports:
      - "6379:6379"
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 📞 Contacto

- **Desarrollador**: [Tu Nombre]
- **Email**: [tu-email@ejemplo.com]
- **Proyecto**: [https://github.com/tu-usuario/optimQuery](https://github.com/tu-usuario/optimQuery)

---

⭐ **¡Si este proyecto te ayuda, dale una estrella!**
