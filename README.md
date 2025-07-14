# optimQuery

Proyecto Java Spring Boot para la optimización de consultas sobre una base de datos PostgreSQL, utilizando Redis como cache y buenas prácticas de arquitectura y rendimiento.

## Descripción

optimQuery es una API REST que permite consultar y exportar resultados optimizados desde una base de datos, aplicando filtros y paginación eficiente. El sistema está preparado para manejar grandes volúmenes de datos y soporta cache distribuido con Redis.

## Arquitectura

- **Backend:** Java 21, Spring Boot 3
- **Base de datos:** PostgreSQL
- **Cache:** Redis
- **ORM:** JPA/Hibernate
- **DTOs:** Para desacoplar la entidad de la API
- **Paginación:** Soportada en endpoints principales
- **Exportación:** Soporte para exportar resultados en formato TXT separado por "|"

## Estructura del proyecto

```
optimQuery/
├── src/
│   ├── main/
│   │   ├── java/com/kast/optimQuery/
│   │   │   ├── controller/         # Controladores REST
│   │   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── model/              # Entidades JPA
│   │   │   ├── repository/         # Repositorios JPA
│   │   │   └── ...
│   │   └── resources/
│   │       ├── application.yml     # Configuración principal
│   │       └── ...
│   └── test/
└── pom.xml                        # Dependencias Maven
```

## Configuración

Configura las variables de entorno para la base de datos en tu sistema:

```
DB_USER=usuario
DB_PASS=contraseña
```

El archivo `src/main/resources/application.yml` contiene la configuración de:

- Conexión a PostgreSQL
- Cache Redis
- Propiedades de Hibernate para batch y rendimiento

## Dependencias principales

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Data Redis
- PostgreSQL Driver
- Lombok
- MapStruct (solo si se requiere mapeo avanzado)
- QueryDSL (opcional para consultas avanzadas)

## Endpoints principales

### 1. Consultar resultados optimizados (con filtros y paginación)

```
GET /api/resultados?campana={campana}&dia={dia}&mes={mes}&page={page}&size={size}
```

- **Parámetros opcionales:** campana, dia, mes, page, size
- **Respuesta:** Página de ResultadosResumenDto

### 2. Exportar resultados en formato TXT

```
GET /api/resultados/export?campana={campana}&dia={dia}&mes={mes}
```

- **Respuesta:** Archivo TXT separado por "|"

## Optimización y buenas prácticas implementadas

- **Índices en la base de datos** para los campos más consultados y combinaciones frecuentes.
- **Cálculo de campos como `dia` y `mes` en la entidad** para evitar funciones costosas en las consultas.
- **Cache distribuido con Redis** para acelerar respuestas y reducir carga en la base de datos.
- **Paginación obligatoria** en endpoints de consulta masiva.
- **Configuración de batch en Hibernate** para operaciones masivas.

## Recomendaciones para el futuro

- **Monitoreo:** Integrar Spring Boot Actuator, Prometheus y Grafana.
- **Seguridad:** Añadir autenticación y autorización.
- **Pruebas:** Ampliar cobertura de pruebas unitarias y de integración.
- **Microservicios:** Considerar migrar a microservicios si el proyecto crece.
- **Documentación:** Mantener Swagger/OpenAPI actualizado.
- **Métricas y alertas:** Para anticipar cuellos de botella y problemas de rendimiento.

## Ejecución

1. Instala PostgreSQL y Redis en tu entorno.
2. Configura las variables de entorno `DB_USER` y `DB_PASS`.
3. Ejecuta el proyecto:
   ```
   ./mvnw spring-boot:run
   ```
4. Accede a los endpoints documentados.
