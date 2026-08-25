package com.trainingcenter.repository;

import com.trainingcenter.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    List<CourseContent> findByCourseCourseIdOrderBySequenceNoAsc(Long courseId);
}
