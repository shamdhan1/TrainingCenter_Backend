# Database Configuration & Schema

This document details the database configuration, relational schema, table structures, and seed data.

---

## 1. Database Specifications
- **Database Engine**: PostgreSQL
- **Database Name**: `student_management_System`
- **Default Port**: `5432`
- **Schema Management**: Managed via Hibernate auto-creation (`ddl-auto=update` in development).

---

## 2. Core Entities & Schema Map

The database includes the following key tables and relations:

### 🏙️ Center (`centers`)
Represents physical locations of training institutes (e.g. Pune, Mumbai).
- **Key Columns**: `center_id` (PK), `center_code` (Unique), `name`, `status`.

### 📚 Course (`courses`)
Represents training tracks available (e.g. C++, Java).
- **Key Columns**: `course_id` (PK), `course_code` (Unique), `course_name`, `total_fee`, `center_id` (FK -> `centers`).

### 👤 User Account (`user_accounts`)
Stores login profiles.
- **Key Columns**: `user_id` (PK), `username` (Unique), `password_hash`, `role_id` (FK -> `roles`).

### 🧑‍🏫 Trainer (`trainers`)
Staff teaching batches.
- **Key Columns**: `trainer_id` (PK), `name`, `center_id` (FK -> `centers`), `user_id` (FK -> `user_accounts`).

### 🧑‍🎓 Student (`students`)
Enrolled trainees.
- **Key Columns**: `student_id` (PK), `name`, `center_id` (FK -> `centers`), `user_id` (FK -> `user_accounts`).

---

## 3. Database Seeding
Seed data is initialized during development via `DatabaseInitializer.java` to set up initial static Roles (`ROLE_ADMIN`, `ROLE_TRAINER`, `ROLE_STUDENT`) and default administrator access accounts.

---

## 4. Production Migration Recommendation (Flyway)
For production environments, using Hibernate's `ddl-auto=update` is **strongly discouraged** because it can result in loss of data and schema inconsistencies.

### Flyway Setup Recommendation:
1. Add the Flyway dependency to `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.flywaydb</groupId>
       <artifactId>flyway-core</artifactId>
   </dependency>
   ```
2. Disable Hibernate ddl-auto check in production properties:
   ```properties
   spring.jpa.hibernate.ddl-auto=none
   ```
3. Place SQL migrations under `src/main/resources/db/migration/` naming them starting with a version tag (e.g. `V1__init_schema.sql`, `V2__add_courses.sql`). Flyway will apply migrations sequentially.
