# Future AI Integration Architecture

This document describes the recommended pattern for integrating AI capabilities (like Gemini or OpenAI API queries) securely.

---

## 1. Secure AI Integration Pattern

When connecting third-party AI APIs, the browser frontend must **never** contact the AI provider directly. Communication must be proxied through the Spring Boot backend:

```text
Browser / Angular UI
         │
         │ (REST Query: "Summarize student progress")
         ▼
  Spring Boot API
         │
         ├─► Load Secret API Key securely from System Environment
         ├─► Formulate System Prompt & context
         └─► REST Post Payload to AI Provider (Gemini/OpenAI)
                  │
                  ▼
             AI Provider
```

---

## 2. Why API Keys Must Remain on the Backend
1. **Source Vulnerability**: Any keys stored in the frontend are packaged inside the downloaded browser Javascript files, leaving them exposed to inspection.
2. **CORS Blocks**: Standard AI providers block direct browser client requests (CORS rules) to prevent key leakage.
3. **Usage Limits**: Proxying calls through Spring Boot allows you to enforce request throttling, authorization checks, and token spending budgets.
