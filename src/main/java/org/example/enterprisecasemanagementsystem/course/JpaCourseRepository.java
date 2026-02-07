package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCourseRepository extends CourseRepository, JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByDescriptionContainingIgnoreCase(String description);
    List<Course> findByTeacher(Teacher teacher);
}