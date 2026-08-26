# Backend Integration Guide for Onboarding Profile Feature

This guide contains step-by-step instructions on where to copy each of the Java files in this `logic/` folder to enable a **comprehensive Trainer Profile** registration endpoint `POST /api/v1/trainers/register`.

---

## 1. Directory Structure Mapping

Place the files in this directory under the corresponding backend source packages:

| Source File in `logic/` | Destination Target Package Path | Description |
|---|---|---|
| [Trainer.java](file:///d:/project/Student-Management-System/logic/Trainer.java) | `src/main/java/com/trainingcenter/entity/` | **[CRITICAL]** Overrides existing Entity with extended JPA columns (designation, contract, bank, bio details) |
| [TrainerRegistrationRequest.java](file:///d:/project/Student-Management-System/logic/TrainerRegistrationRequest.java) | `src/main/java/com/trainingcenter/dto/request/` | Input validation request payload |
| [TrainerRegistrationResponse.java](file:///d:/project/Student-Management-System/logic/TrainerRegistrationResponse.java) | `src/main/java/com/trainingcenter/dto/response/` | Detailed profile response payload |
| [TrainerRegistrationService.java](file:///d:/project/Student-Management-System/logic/TrainerRegistrationService.java) | `src/main/java/com/trainingcenter/service/` | Service Interface declaration |
| [TrainerRegistrationServiceImpl.java](file:///d:/project/Student-Management-System/logic/TrainerRegistrationServiceImpl.java) | `src/main/java/com/trainingcenter/service/impl/` | Service Transaction logic class (onboarding mappings) |
| [TrainerRegistrationController.java](file:///d:/project/Student-Management-System/logic/TrainerRegistrationController.java) | `src/main/java/com/trainingcenter/controller/` | Rest Controller Mapping |

---

## 2. JPA Database Alteration

When you copy [Trainer.java](file:///d:/project/Student-Management-System/logic/Trainer.java) into `src/main/java/com/trainingcenter/entity/`, the application will automatically perform DDL Dumper table alterations upon startup (since H2 has `spring.jpa.hibernate.ddl-auto=update` enabled).

The following columns will be automatically added to the `trainers` table:
- `gender` (VARCHAR 15)
- `date_of_birth` (DATE)
- `aadhaar_no` (VARCHAR 12)
- `pan_no` (VARCHAR 10)
- `alternative_mobile` (VARCHAR 15)
- `address` (VARCHAR 255)
- `city` (VARCHAR 50)
- `state` (VARCHAR 50)
- `pincode` (VARCHAR 10)
- `designation` (VARCHAR 50)
- `contract_type` (VARCHAR 20)
- `salary` (NUMERIC 10,2)
- `bank_name` (VARCHAR 100)
- `bank_account_number` (VARCHAR 30)
- `ifsc_code` (VARCHAR 20)
- `bio` (TEXT)

---

## 3. Spring Security Mappings (Optional)

Any new endpoint `/api/v1/trainers/register` requires JWT Token authentication.

### A. To Restrict Registration to Admin/Center Managers
Keep the default Spring Security setup and use Method Security in your controller:
```java
@PostMapping("/register")
@PreAuthorize("hasAnyRole('ADMIN', 'CENTER_MANAGER')")
public ResponseEntity<ApiResponse<TrainerRegistrationResponse>> registerTrainer(...)
```

### B. To Permit Public Registration (for Testing/Sign up)
Add the endpoint request matcher to the public whitelist in `com/trainingcenter/security/SecurityConfig.java`:
```java
.requestMatchers(new AntPathRequestMatcher("/api/v1/trainers/register")).permitAll()
```
