package com.trainingcenter.admin.controller;

import com.trainingcenter.admin.dto.request.CourseContentRequest;
import com.trainingcenter.admin.dto.request.CourseRequest;
import com.trainingcenter.admin.dto.response.ApiResponse;
import com.trainingcenter.admin.dto.response.CourseContentResponse;
import com.trainingcenter.admin.dto.response.CourseResponse;
import com.trainingcenter.admin.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Courses fetched successfully", courses));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> searchCourses(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long centerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseResponse> courses = courseService.searchCourses(query, status, centerId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Courses searched successfully", courses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success("Course fetched successfully", course));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse course = courseService.createCourse(request);
        return ResponseEntity.ok(ApiResponse.success("Course created successfully", course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        CourseResponse course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", "Deleted course with ID: " + id));
    }

    @PostMapping("/{id}/contents")
    public ResponseEntity<ApiResponse<CourseContentResponse>> addCourseContent(
            @PathVariable Long id,
            @Valid @RequestBody CourseContentRequest request) {
        CourseContentResponse content = courseService.addCourseContent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course content added successfully", content));
    }

    @GetMapping("/{id}/contents")
    public ResponseEntity<ApiResponse<List<CourseContentResponse>>> getCourseContents(@PathVariable Long id) {
        List<CourseContentResponse> contents = courseService.getCourseContents(id);
        return ResponseEntity.ok(ApiResponse.success("Course contents fetched successfully", contents));
    }

    @DeleteMapping("/contents/{contentId}")
    public ResponseEntity<ApiResponse<String>> deleteCourseContent(@PathVariable Long contentId) {
        courseService.deleteCourseContent(contentId);
        return ResponseEntity.ok(ApiResponse.success("Course content deleted successfully", "Deleted content with ID: " + contentId));
    }
}
