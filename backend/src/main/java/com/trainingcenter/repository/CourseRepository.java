package com.trainingcenter.repository;

import com.trainingcenter.entity.Course;
import com.trainingcenter.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);
    List<Course> findByStatus(CourseStatus status);

    @Query("SELECT c FROM Course c WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           " LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(c.courseName) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:centerId IS NULL OR c.center.centerId = :centerId)")
    Page<Course> searchCourses(@Param("query") String query, 
                               @Param("status") CourseStatus status, 
                               @Param("centerId") Long centerId, 
                               Pageable pageable);

    long countByStatus(CourseStatus status);
}
