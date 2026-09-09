# Production Deployment Architecture

This document describes the recommended hosting layout, reverse proxy routing rules, and environment configurations for production environments.

---

## 1. Hosting Architecture Map

In production, client browsers query the application through a secure **Nginx** reverse proxy, routing user traffic dynamically based on path queries:

```text
                        INTERNET / BROWSER (HTTPS)
                                    │
                                    ▼
                         Nginx / Reverse Proxy
                                    │
              ┌─────────────────────┴─────────────────────┐
              ▼ (Path: /)                                 ▼ (Path: /api/*)
         Angular SPA                             Spring Boot REST API
     (Static HTML/JS/CSS)                        (Runnable Jar on Port 8082)
                                                          │
                                                          ▼
                                                  PostgreSQL Database
```

---

## 2. Nginx Reverse Proxy Configuration Example

Nginx acts as the front gateway, serving static client files directly and forwarding API transactions back to Spring Boot:

```nginx
server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /etc/ssl/certs/your-domain.crt;
    ssl_certificate_key /etc/ssl/private/your-domain.key;

    # 1. Serve Angular SPA
    location / {
        root /var/www/sms-frontend;
        try_files $uri $uri/ /index.html;
        index index.html;
    }

    # 2. Forward Backend API Transactions
    location /api/ {
        proxy_pass http://localhost:8082;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## 3. Why the Frontend Does Not Contain Database Credentials
The Angular client application compiles into static HTML, CSS, and Javascript. These assets execute inside the **user's web browser**, meaning all client-side code is publicly readable by inspecting the browser inspector.

- Hardcoding DB passwords or JWT secret keys inside Angular components presents a **critical security vulnerability**.
- Instead, the backend (Spring Boot) executes securely inside your private cloud network, keeping database configurations sealed. The browser client authenticates using a temporary JWT bearer signature.
