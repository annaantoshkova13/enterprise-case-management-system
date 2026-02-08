package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.teacher.Teacher;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course save(Course course);
    Optional<Course> findById(Long id);
    List<Course> findAll();
    boolean existsById(Long id);
    void delete(Course course);
    void deleteById(Long id);
    void deleteAll();
    List<Course> findAllById(Iterable<Long> ids);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByDescriptionContainingIgnoreCase(String description);
    List<Course> findByTeacher(Teacher teacher);
}