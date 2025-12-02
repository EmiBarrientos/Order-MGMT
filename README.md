## 📦 Order-MGMT — Microservices Architecture (Spring Cloud)

Sistema de gestión de productos y pedidos construido sobre una arquitectura de microservicios modular, escalable y centralizada.
Incluye:
- ✔ Spring Cloud Gateway (MVC)
- ✔ Eureka Service Discovery
- ✔ Config Server nativo
- ✔ Microservicios independientes (Products + Orders)
- ✔ Configuración centralizada
- ✔ Comunicación mediante discovery
- ✔ Rutas dinámicas
- ✔ Versionado de API
---

```
🧩 Arquitectura General
                   ┌──────────────────┐
                   │  Config Server    │
                   │  (port 8888)      │
                   └─────────┬────────┘
                             │
                             ▼
                 Centralized application.yml
                             │
     ┌───────────────────────┴────────────────────────┐
     │                        │                       │
     ▼                        ▼                       ▼
┌───────────┐         ┌──────────────┐        ┌──────────────┐
│  Eureka   │◀────────│  Gateway     │───────▶│ Product-MS    │
│ 8761      │         │ 8080         │        │ 8090          │
└───────────┘         └──────────────┘        └──────────────┘
                                             ┌────────────────┐
                                             │ Order-MS 9090  │
                                             └────────────────┘
```
---

Componentes
- Servicio   	Puerto	Rol
- Config Server	8888	Centraliza configuraciones para todos los microservicios
- Eureka Server	8761	Registro de servicios / discovery dinámico
- Gateway (MVC)	8080	Entrada única al sistema, rutas dinámicas
- Product-Service	8090	CRUD productos
- Order-Service	9090	CRUD pedidos
---
---
## 🚀 Características principales
1. Configuración Centralizada

Todos los microservicios cargan su configuración desde Config Server a través de:

spring:
  config:
    import: optional:configserver:http://localhost:8888


Esto incluye puertos, names, rutas, DB credentials, etc.

2. Service Discovery con Eureka

Cada servicio se auto-registra:

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/


Y se accede por su nombre lógico, no por su puerto.

3. API Gateway (Spring Cloud Gateway MVC)

Rutas configuradas en el config repo:

spring:
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/product/**

        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/order/**


Esto permite:

Load balancing automático

Rutas por nombre del servicio

Rewrites si se desean

Aislamiento de puertos internos

4. Microservicios Reales

Cada micro:

- ✔ Tiene su propio application.yml centralizado
- ✔ Su propio controller
- ✔ Su propio modelo
- ✔ Su propia capa de persistencia

Endpoints del estilo:

GET  /api/product/find/{id}
GET  /api/order/find/{id}
POST /api/order/create
...
---

## 🛠️ Tecnologías utilizadas

| Tecnología               | Uso principal                                                   |
|--------------------------|-----------------------------------------------------------------|
| Java 17 / 21 / 23         | Lenguaje de programación utilizado para construir la aplicación |
| Spring Boot 3.5.6         | Framework principal para el backend y configuración automática  |
| Spring Data JPA           | Abstracción para la persistencia de datos con Hibernate         |
| Eureka                    | Para el registro de los microservicios                          |
| Gateway                   | para la centralizacion de los puertos                           |
| Maven                     | Gestión de dependencias y ciclo de vida del proyecto            |
| MySQL / PostgreSQL        | Bases de datos para el almacenamiento persistente               |
| Lombok                    | Eliminación de código repetitivo (getters, setters, etc.)       |
| Actuator                  | monitoreo la aplicación, recopilación de métricas               |
| Config server             | centralizacion de las configuraciones                           |



---
## 📡 Comportamiento del Sistema

Los servicios levantan sin configuración local.

Todo viene desde Config Server.

Eureka registra servicios automáticamente.

El gateway lee Eureka y enruta dinámicamente.

Si mañana cambiás puertos → no cambiás código, solo configuración remota.
---
## 🧪 Cómo Probar
- 1️⃣ Levantar Config Server:
- mvn spring-boot:run


- Puerto: 8888

- 2️⃣ Levantar Eureka:
- mvn spring-boot:run


- Puerto: 8761

- Abrir en navegador:

- http://localhost:8761

- 3️⃣ Levantar Gateway:
- mvn spring-boot:run


- Puerto: 8080

- 4️⃣ Levantar Product-Service y Order-Service
- 5️⃣ Probar endpoints vía Gateway:
- GET http://localhost:8080/api/product/find/1
- GET http://localhost:8080/api/order/find/1


- Si llegan correctamente → routing OK.
---
## 📚 Diagrama de secuencia (flujo de request)
User → Gateway → Eureka (resolve) → Service → Response → Gateway → User

## 🛡️ Mejoras futuras

🔹 Agregarle front funcional

🔹 Implementar concurrencia

🔹 Migrar a Docker Compose

🔹 Añadir un servicio de autenticación

🔹 Logging distribuido con traceId

🔹 Implementar Kafka para eventos (alta de pedidos, etc.)

## 👨‍💻 Autor

**Emiliano Barrientos**
Backend Developer — Java / Spring Boot
