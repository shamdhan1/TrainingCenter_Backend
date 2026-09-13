package com.trainingcenter.admin.repository;

import com.trainingcenter.admin.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    List<CourseContent> findByCourseCourseIdOrderByModuleNumberAsc(Long courseId);
}
