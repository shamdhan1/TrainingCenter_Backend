package com.trainingcenter.student.controller;

import com.trainingcenter.student.dto.request.StudentRequest;
import com.trainingcenter.student.dto.response.ApiResponse;
import com.trainingcenter.student.dto.response.StudentResponse;
import com.trainingcenter.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/count")
    public ResponseEntity<Long> getStudentCount() {
        return ResponseEntity.ok(studentService.countStudents());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> searchStudents(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<StudentResponse> students = studentService.searchStudents(query, status, centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Students fetched successfully", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Long id) {
        StudentResponse student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student profile fetched successfully", student));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse student = studentService.createStudent(request);
        return ResponseEntity.ok(ApiResponse.success("Student registered successfully", student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        StudentResponse student = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", "Deleted student with ID: " + id));
    }
}
