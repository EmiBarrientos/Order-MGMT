# Order MGMT

Microservices-based order management system built with **Java and Spring Boot**, designed to explore distributed systems, service-to-service communication, centralized configuration, API Gateway patterns, authentication, and different architectural approaches.

The project simulates an ecosystem where users can interact with products and create orders through a centralized API Gateway.

---

## Architecture

The system is composed of several independent services, each responsible for a specific business or infrastructure concern.

```text
                         ┌─────────────────┐
                         │     Client      │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │   API Gateway   │
                         │   WebFlux       │
                         │ Security / JWT  │
                         └────────┬────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    │             │             │
                    ▼             ▼             ▼
             ┌────────────┐ ┌────────────┐ ┌────────────┐
             │   Users    │ │   Orders   │ │  Products  │
             │  Service   │ │  Service   │ │  Service   │
             └─────┬──────┘ └─────┬──────┘ └─────┬──────┘
                   │              │              │
                   ▼              ▼              ▼
                MongoDB       PostgreSQL        MySQL

                    ┌─────────────────────────┐
                    │      Eureka Server      │
                    │    Service Discovery    │
                    └─────────────────────────┘

                    ┌─────────────────────────┐
                    │      Config Server      │
                    │ Centralized Configuration│
                    └─────────────────────────┘
```

### Business services

| Service             | Responsibility                     | Database   | Architecture |
| ------------------- | ---------------------------------- | ---------- | ------------ |
| **User Service**    | User management and authentication | MongoDB    | Hexagonal    |
| **Product Service** | Product management                 | MySQL      | MVC          |
| **Order Service**   | Order creation and management      | PostgreSQL | MVC          |

### Infrastructure services

| Service           | Responsibility                   |
| ----------------- | -------------------------------- |
| **API Gateway**   | Routing and centralized security |
| **Eureka Server** | Service discovery                |
| **Config Server** | Centralized configuration        |

---

## Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Cloud
* Spring Web
* Spring WebFlux
* Spring Security
* JWT
* OpenFeign
* Spring Data JPA / Hibernate
* Spring Data MongoDB

### Databases

* PostgreSQL
* MySQL
* MongoDB

Each business microservice owns its own database, avoiding direct database access between services.

### Infrastructure & Development

* Docker
* Docker Compose
* Eureka
* Spring Cloud Config Server
* Maven
* Git

### Testing

* JUnit 5
* Mockito
* MockMvc

---

## Main Features

### Product Management

The Product Service manages the product catalog and exposes endpoints used by both clients and other services.

The service has its own MySQL database and is isolated from the databases of the other microservices.

### Order Management

The Order Service is responsible for creating and managing orders.

When an order is created, the service communicates with the Product Service through **OpenFeign** to validate the requested products before persisting the order.

Orders are persisted independently in PostgreSQL.

### User Management

The User Service manages users and authentication.

Unlike the other business services, it follows a **Hexagonal Architecture** approach, separating domain logic from infrastructure concerns.

```text
┌─────────────────────────────┐
│         Infrastructure      │
│                             │
│ Controllers / Adapters      │
│ Security / Configuration    │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          Application        │
│                             │
│ Application Services        │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│            Domain           │
│                             │
│ Models / Ports / Exceptions │
└─────────────────────────────┘
```

---

## API Gateway

All external requests enter the system through the API Gateway.

The Gateway is implemented using **Spring Cloud Gateway and WebFlux**, providing a reactive entry point to the microservices ecosystem.

Its current responsibilities include:

* Request routing
* Service discovery integration
* Centralized authentication
* JWT validation
* Cookie-based authentication
* Request filtering

The Gateway communicates with services using their logical service names through Eureka instead of relying on hardcoded service addresses.

---

## Authentication

The project currently uses **JWT-based authentication** with Spring Security.

The authentication flow is designed around an HTTP-only cookie containing the JWT.

```text
Client
   │
   │ Login
   ▼
User Service
   │
   │ JWT
   ▼
API Gateway
   │
   │ HTTP-only Cookie
   ▼
Client
```

For subsequent requests:

```text
Client
   │
   │ HTTP-only Cookie
   ▼
API Gateway
   │
   │ JWT validation
   ▼
Microservice
```

The JWT currently contains information used to identify the authenticated user and support role-based access control.

Supported roles currently include:

* `USER`
* `ADMIN`

> Authentication and authorization are still under active development.

---

## Service-to-Service Communication

Business microservices communicate through APIs rather than accessing each other's databases.

For example:

```text
Order Service
      │
      │ OpenFeign
      ▼
Product Service
      │
      ▼
   MySQL
```

This keeps database ownership isolated and allows each microservice to evolve its persistence layer independently.

---

## Centralized Configuration

Configuration is managed through **Spring Cloud Config Server**.

The microservices retrieve their configuration from the Config Server during startup.

```text
             Config Server
                  │
        ┌─────────┼─────────┐
        ▼         ▼         ▼
      Users     Orders    Products
```

This centralizes environment-specific configuration and avoids duplicating configuration across individual services.

---

## Service Discovery

The system uses **Netflix Eureka** for service discovery.

Each microservice registers itself with Eureka and can be located by its logical service name.

This allows the Gateway and inter-service communication to avoid hardcoding service hostnames and ports.

---

## Database Architecture

Each business microservice has an independent database:

```text
User Service    → MongoDB
Product Service → MySQL
Order Service   → PostgreSQL
```

No business microservice directly accesses another service's database.

Communication between services is performed through their APIs.

This follows the **database-per-service** approach commonly used in microservice architectures.

---

## Docker

The entire ecosystem is containerized.

Each microservice has its own `Dockerfile`, and the complete environment can be started using Docker Compose.

```bash
docker compose up
```

This allows the application, infrastructure services, and databases to be started together without manually configuring each component.

---

## Testing

The project includes automated tests using:

* JUnit 5
* Mockito
* MockMvc

Tests currently cover controllers and service-layer behavior across the microservices.

Each service also implements centralized exception handling through a `GlobalExceptionHandler` and a consistent `ApiError` response structure.

---

## Current Development

The project is actively evolving.

Planned improvements include:

* Completing the security model across the microservices ecosystem.
* Implementing stronger authorization and role-based access control.
* Improving order and inventory management.
* Introducing asynchronous communication with Apache Kafka.
* Addressing concurrency and stock consistency.
* Improving resilience and failure handling.
* Adding observability and monitoring.
* Expanding automated test coverage.
* Adding API documentation.
* Implementing CI/CD.

---

## Goals

Order MGMT is primarily a learning and portfolio project focused on applying backend engineering concepts in a distributed architecture.

The main objectives are:

* Understanding microservice architecture in practice.
* Working with service discovery and centralized configuration.
* Designing independent persistence layers.
* Implementing synchronous service-to-service communication.
* Exploring reactive API Gateway development.
* Applying Spring Security and JWT authentication.
* Practicing Hexagonal Architecture.
* Containerizing a complete distributed application.
* Understanding the challenges introduced by distributed systems.

---
## Getting Started

### Prerequisites

Before running the project, make sure you have installed:

* [Git](https://git-scm.com/)
* [Docker](https://www.docker.com/)
* Docker Compose

Java and Maven are only required if you want to run individual services outside Docker.

### Clone the repository

```bash
git clone https://github.com/EmiBarrientos/Order-MGMT.git
cd Order-MGMT
```

### Run the application

The complete application can be started using Docker Compose:

```bash
docker compose up --build
```

Docker Compose builds and starts the application services and connects them through an internal Docker network.

### Architecture and service communication

The **API Gateway** acts as the main entry point for external API requests.

The business microservices communicate internally through the Docker network and are not directly exposed to the host.

```text
Client
   │
   │ HTTP :8080
   ▼
┌─────────────────┐
│   API Gateway   │
│      :8080      │
└────────┬────────┘
         │
         │ Docker Network
         │
    ┌────┼─────────────┐
    ▼    ▼             ▼
 User   Product       Order
Service Service      Service
    │      │             │
    ▼      ▼             ▼
 MongoDB  MySQL      PostgreSQL
```

Services communicate with each other using their Docker service names rather than `localhost`.

For example:

```text
http://product-service:8090
http://order-service:9090
```

These addresses are intended for internal communication within the Docker network.

### API access

External requests should be sent through the API Gateway:

```text
http://localhost:8080
```

The Gateway routes requests to the corresponding microservice.

### Infrastructure services

The application also includes:

* **Config Server** — centralized configuration.
* **Eureka Server** — service discovery.
* **API Gateway** — external entry point and request routing.
* **User Service** — user management and authentication.
* **Product Service** — product management.
* **Order Service** — order management.

### Databases

Each business microservice uses its own database:

| Service         | Database   |
| --------------- | ---------- |
| User Service    | MongoDB    |
| Product Service | MySQL      |
| Order Service   | PostgreSQL |

The databases currently expose their ports to the host to facilitate local development, inspection and database management.

### Stopping the application

To stop the running containers:

```bash
docker compose down
```

To rebuild the application after making changes:

```bash
docker compose up --build
```

To stop the containers and remove their associated volumes:

```bash
docker compose down -v
```

> **Note:** Removing volumes will delete the persisted database data associated with the Docker volumes.



## Author

**Emiliano Barrientos**

Backend Developer focused on **Java, Spring Boot and distributed systems**.

