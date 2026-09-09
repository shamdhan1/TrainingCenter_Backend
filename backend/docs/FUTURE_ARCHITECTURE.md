# Future Scalability Architecture

This document describes how this system can scale from a single monolith to a cached, distributed microservice architecture.

---

## Stage 1: Current Monolithic Baseline
- **Structure**:
  `Angular -> Spring Boot -> PostgreSQL`
- **Characteristics**: Single deployable archive. Simple, fast, and easy to maintain locally.

---

## Stage 2: Monolith with Redis Caching
- **Structure**:
  ```text
  Angular -> Spring Boot -> Redis (Caching Layer)
                               │
                               ▼
                          PostgreSQL
  ```
- **When is it needed?**: When read-heavy requests (e.g. course listings or center locations) slow down database execution times. Redis stores matches in memory for near-instant retrieval.

---

## Stage 3: Distributed Microservices
- **Structure**:
  ```text
  Angular -> Nginx/API Gateway -> [Student Service]    ──► PostgreSQL (Student DB)
                              ├──► [Payment Service]    ──► PostgreSQL (Payment DB)
                              └──► [Notification Serv.] ──► Kafka Message Broker
  ```
- **When is it needed?**: When development teams scale beyond 10-15 members or system modules need to scale independently.
- **Key Challenges Introduced**:
  - Requires distributed transactions (Saga Pattern).
  - High infrastructure complexity (API Gateway, Eureka, Zipkin, Kafka, Kubernetes).
