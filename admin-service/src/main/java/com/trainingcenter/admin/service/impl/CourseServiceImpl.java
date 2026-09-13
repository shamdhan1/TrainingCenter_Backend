package com.trainingcenter.admin.service.impl;

import com.trainingcenter.admin.dto.request.CourseContentRequest;
import com.trainingcenter.admin.dto.request.CourseRequest;
import com.trainingcenter.admin.dto.response.CourseContentResponse;
import com.trainingcenter.admin.dto.response.CourseResponse;
import com.trainingcenter.admin.entity.Center;
import com.trainingcenter.admin.entity.Course;
import com.trainingcenter.admin.entity.CourseContent;
import com.trainingcenter.admin.enums.CourseStatus;
import com.trainingcenter.admin.exception.ConflictException;
import com.trainingcenter.admin.exception.ResourceNotFoundException;
import com.trainingcenter.admin.repository.CenterRepository;
import com.trainingcenter.admin.repository.CourseContentRepository;
import com.trainingcenter.admin.repository.CourseRepository;
import com.trainingcenter.admin.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseContentRepository courseContentRepository;
    private final CenterRepository centerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> searchCourses(String query, String status, Long centerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        CourseStatus courseStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            courseStatus = CourseStatus.valueOf(status.toUpperCase());
        }
        return courseRepository.searchCourses(query, courseStatus, centerId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        return mapToResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new ConflictException("Course code already exists: " + request.getCourseCode());
        }

        Center center = null;
        if (request.getCenterId() != null) {
            center = centerRepository.findById(request.getCenterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Center not found with ID: " + request.getCenterId()));
        }

        Course course = Course.builder()
                .center(center)
                .courseCode(request.getCourseCode().toUpperCase())
                .courseName(request.getCourseName())
                .description(request.getDescription())
                .durationWeeks(request.getDurationWeeks())
                .fees(request.getFees())
                .status(request.getStatus() != null ? request.getStatus() : CourseStatus.ACTIVE)
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        if (!course.getCourseCode().equalsIgnoreCase(request.getCourseCode()) &&
                courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new ConflictException("Course code already exists: " + request.getCourseCode());
        }

        Center center = null;
        if (request.getCenterId() != null) {
            center = centerRepository.findById(request.getCenterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Center not found with ID: " + request.getCenterId()));
        }

        course.setCenter(center);
        course.setCourseCode(request.getCourseCode().toUpperCase());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setDurationWeeks(request.getDurationWeeks());
        course.setFees(request.getFees());
        if (request.getStatus() != null) {
            course.setStatus(request.getStatus());
        }

        return mapToResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CourseContentResponse addCourseContent(Long courseId, CourseContentRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        CourseContent content = CourseContent.builder()
                .course(course)
                .moduleNumber(request.getModuleNumber())
                .topicTitle(request.getTopicTitle())
                .description(request.getDescription())
                .contentType(request.getContentType())
                .resourceLink(request.getResourceLink())
                .build();

        CourseContent saved = courseContentRepository.save(content);
        return mapToContentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseContentResponse> getCourseContents(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
        return courseContentRepository.findByCourseCourseIdOrderByModuleNumberAsc(courseId).stream()
                .map(this::mapToContentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCourseContent(Long contentId) {
        if (!courseContentRepository.existsById(contentId)) {
            throw new ResourceNotFoundException("Course content not found with ID: " + contentId);
        }
        courseContentRepository.deleteById(contentId);
    }

    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .durationWeeks(course.getDurationWeeks())
                .fees(course.getFees())
                .status(course.getStatus())
                .centerId(course.getCenter() != null ? course.getCenter().getCenterId() : null)
                .centerName(course.getCenter() != null ? course.getCenter().getCenterName() : null)
                .build();
    }

    private CourseContentResponse mapToContentResponse(CourseContent content) {
        return CourseContentResponse.builder()
                .contentId(content.getContentId())
                .courseId(content.getCourse().getCourseId())
                .moduleNumber(content.getModuleNumber())
                .topicTitle(content.getTopicTitle())
                .description(content.getDescription())
                .contentType(content.getContentType())
                .resourceLink(content.getResourceLink())
                .build();
    }
}
