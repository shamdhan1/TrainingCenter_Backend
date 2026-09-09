# API Standards & REST Conventions

This document outlines the RESTful API design standards followed by the Student Management System.

---

## 1. API Versioning & Endpoints
All API endpoints are versioned with the `/api/v1` prefix to allow backwards-compatible modifications.

Endpoints use plural nouns representing resources. Verbs must **not** be included in endpoint URIs.

### Correct Conventions:
- **`GET /api/v1/students`** : Fetch list of students.
- **`POST /api/v1/students`** : Register a new student.
- **`GET /api/v1/students/{id}`** : Fetch details of a specific student.
- **`PUT /api/v1/students/{id}`** : Update an existing student.
- **`DELETE /api/v1/students/{id}`** : Remove a student record.

### Incorrect (Avoid):
- `GET /api/v1/getStudents` (verb in URI)
- `POST /api/v1/createStudent` (verb in URI)
- `DELETE /api/v1/deleteStudent/5` (verb in URI)

---

## 2. HTTP Status Codes

We use explicit, standard HTTP status codes in response payloads:

| HTTP Status Code | Meaning | Usage Scenario |
| :--- | :--- | :--- |
| **`200 OK`** | Request Succeeded | Successful `GET`, `PUT`, or `DELETE` transactions. |
| **`201 Created`** | Resource Generated | Successful `POST` creations. |
| **`204 No Content`** | Empty Body | Successful deletion where no response body is needed. |
| **`400 Bad Request`** | Validation Error | Client-side input fails constraint checks. |
| **`401 Unauthorized`** | Missing Auth | Client is not authenticated (missing or expired JWT). |
| **`403 Forbidden`** | Insufficient Roles | Client is authenticated but lacks required permission levels. |
| **`404 Not Found`** | Resource Missing | Database entity matching the ID does not exist. |
| **`409 Conflict`** | Data Lock/Duplication | Constraint violation (e.g. duplicating unique codes). |
| **`500 Internal Error`** | System Crash | Unexpected runtime database failures. |

---

## 3. Query Parameters (Pagination, Filtering, Sorting)
Endpoints delivering collections of resources support sorting, pagination, and query filters:

- **Pagination**: Use `page` (0-indexed) and `size` parameters:
  - `GET /api/v1/centers/paginated?page=0&size=10`
- **Filtering**: Use matching keys as query filters:
  - `GET /api/v1/centers?status=ACTIVE`
- **Sorting**: Map parameters as `sort=property,direction`:
  - `GET /api/v1/students?sort=name,asc`
