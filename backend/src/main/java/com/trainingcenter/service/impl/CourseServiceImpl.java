package com.trainingcenter.service.impl;

import com.trainingcenter.dto.request.CourseContentRequest;
import com.trainingcenter.dto.request.CourseRequest;
import com.trainingcenter.dto.response.CourseContentResponse;
import com.trainingcenter.dto.response.CourseResponse;
import com.trainingcenter.entity.Center;
import com.trainingcenter.entity.Course;
import com.trainingcenter.entity.CourseContent;
import com.trainingcenter.enums.ContentType;
import com.trainingcenter.enums.CourseStatus;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.repository.CourseContentRepository;
import com.trainingcenter.repository.CourseRepository;
import com.trainingcenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseContentRepository courseContentRepository;

    @Autowired
    private CenterRepository centerRepository;

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
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
        return mapToResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new RuntimeException("Course code already exists: " + request.getCourseCode());
        }

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        Course course = Course.builder()
                .center(center)
                .courseCode(request.getCourseCode().toUpperCase())
                .courseName(request.getCourseName())
                .description(request.getDescription())
                .duration(request.getDuration())
                .totalFee(request.getTotalFee())
                .status(CourseStatus.valueOf(request.getStatus().toUpperCase()))
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));

        if (!course.getCourseCode().equalsIgnoreCase(request.getCourseCode()) &&
                courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new RuntimeException("Course code already exists: " + request.getCourseCode());
        }

        Center center = centerRepository.findById(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found with ID: " + request.getCenterId()));

        course.setCenter(center);
        course.setCourseCode(request.getCourseCode().toUpperCase());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setTotalFee(request.getTotalFee());
        course.setStatus(CourseStatus.valueOf(request.getStatus().toUpperCase()));

        return mapToResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CourseContentResponse addCourseContent(Long courseId, CourseContentRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        CourseContent content = CourseContent.builder()
                .course(course)
                .moduleNo(request.getModuleNo())
                .title(request.getTitle())
                .description(request.getDescription())
                .contentType(ContentType.valueOf(request.getContentType().toUpperCase()))
                .resourceUrl(request.getResourceUrl())
                .sequenceNo(request.getSequenceNo())
                .durationMinutes(request.getDurationMinutes())
                .build();

        return mapToContentResponse(courseContentRepository.save(content));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseContentResponse> getCourseContents(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
        return courseContentRepository.findByCourseCourseIdOrderBySequenceNoAsc(courseId).stream()
                .map(this::mapToContentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCourseContent(Long contentId) {
        if (!courseContentRepository.existsById(contentId)) {
            throw new RuntimeException("Course content not found with ID: " + contentId);
        }
        courseContentRepository.deleteById(contentId);
    }

    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .centerId(course.getCenter().getCenterId())
                .centerName(course.getCenter().getName())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .duration(course.getDuration())
                .totalFee(course.getTotalFee())
                .status(course.getStatus().name())
                .createdAt(course.getCreatedAt())
                .build();
    }

    private CourseContentResponse mapToContentResponse(CourseContent content) {
        return CourseContentResponse.builder()
                .contentId(content.getContentId())
                .courseId(content.getCourse().getCourseId())
                .courseName(content.getCourse().getCourseName())
                .moduleNo(content.getModuleNo())
                .title(content.getTitle())
                .description(content.getDescription())
                .contentType(content.getContentType().name())
                .resourceUrl(content.getResourceUrl())
                .sequenceNo(content.getSequenceNo())
                .durationMinutes(content.getDurationMinutes())
                .createdAt(content.getCreatedAt())
                .build();
    }
}
