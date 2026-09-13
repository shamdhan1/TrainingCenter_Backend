package com.trainingcenter.admin.service;

import com.trainingcenter.admin.dto.request.CourseContentRequest;
import com.trainingcenter.admin.dto.request.CourseRequest;
import com.trainingcenter.admin.dto.response.CourseContentResponse;
import com.trainingcenter.admin.dto.response.CourseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses();
    Page<CourseResponse> searchCourses(String query, String status, Long centerId, int page, int size);
    CourseResponse getCourseById(Long id);
    CourseResponse createCourse(CourseRequest request);
    CourseResponse updateCourse(Long id, CourseRequest request);
    void deleteCourse(Long id);

    CourseContentResponse addCourseContent(Long courseId, CourseContentRequest request);
    List<CourseContentResponse> getCourseContents(Long courseId);
    void deleteCourseContent(Long contentId);
}
