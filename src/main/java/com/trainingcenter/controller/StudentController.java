package com.trainingcenter.controller;

import com.trainingcenter.dto.request.StudentRequest;
import com.trainingcenter.dto.response.ApiResponse;
import com.trainingcenter.dto.response.StudentResponse;
import com.trainingcenter.entity.Enrollment;
import com.trainingcenter.repository.EnrollmentRepository;
import com.trainingcenter.security.UserPrincipal;
import com.trainingcenter.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private com.trainingcenter.repository.UserAccountRepository userAccountRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> searchStudents(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (auth != null && auth.getPrincipal() instanceof UserPrincipal)
                ? (UserPrincipal) auth.getPrincipal()
                : null;
        List<String> roles = principal != null ? principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()) : List.of();

        // Center Manager can only search their center's students
        if (principal != null && roles.contains("ROLE_CENTER_MANAGER")) {
            Long managerCenterId = getCenterIdForUserAccount(principal);
            centerId = managerCenterId;
        }

        Page<StudentResponse> students = studentService.searchStudents(query, status, centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Students fetched successfully", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(
            @PathVariable Long id,
            Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        StudentResponse student = studentService.getStudentById(id);

        // Security validation
        if (roles.contains("ROLE_STUDENT")) {
            if (!student.getUserId().equals(principal.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied: You can only access your own profile"));
            }
        } else if (roles.contains("ROLE_TRAINER")) {
            // Verify Trainer is assigned to this student
            List<Enrollment> enrollments = enrollmentRepository.findByStudentStudentId(id);
            boolean isAssigned = enrollments.stream()
                    .anyMatch(e -> e.getTrainer() != null
                            && e.getTrainer().getUser().getUserId().equals(principal.getId()));
            if (!isAssigned) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied: You are not assigned to this student"));
            }
        } else if (roles.contains("ROLE_CENTER_MANAGER")) {
            Long managerCenterId = getCenterIdForUserAccount(principal);
            if (!student.getCenterId().equals(managerCenterId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied: This student is not in your center"));
            }
        }

        return ResponseEntity.ok(ApiResponse.success("Student profile fetched successfully", student));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody StudentRequest request) {
        // Enforce Center Manager registering student only to their center
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        if (principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CENTER_MANAGER"))) {
            request.setCenterId(getCenterIdForUserAccount(principal));
        }

        StudentResponse student = studentService.createStudent(request);
        return ResponseEntity.ok(ApiResponse.success("Student registered successfully", student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        if (principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CENTER_MANAGER"))) {
            request.setCenterId(getCenterIdForUserAccount(principal));
            // Check student was at manager's center
            StudentResponse orig = studentService.getStudentById(id);
            if (!orig.getCenterId().equals(request.getCenterId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied: Student belongs to another center"));
            }
        }

        StudentResponse student = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", "Deleted student with ID: " + id));
    }

    private Long getCenterIdForUserAccount(UserPrincipal principal) {
        return userAccountRepository.findById(principal.getId())
                .map(user -> user.getCenter() != null ? user.getCenter().getCenterId() : null)
                .orElse(null);
    }
}
