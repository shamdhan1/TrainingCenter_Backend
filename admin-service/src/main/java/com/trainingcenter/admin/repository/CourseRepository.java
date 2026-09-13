package com.trainingcenter.admin.repository;

import com.trainingcenter.admin.entity.Course;
import com.trainingcenter.admin.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseCode(String courseCode);

    Optional<Course> findByCourseCode(String courseCode);

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE " +
            "(:query IS NULL OR LOWER(c.courseName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:centerId IS NULL OR c.center.centerId = :centerId)")
    Page<Course> searchCourses(@Param("query") String query,
                               @Param("status") CourseStatus status,
                               @Param("centerId") Long centerId,
                               Pageable pageable);
}
