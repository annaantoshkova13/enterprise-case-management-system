package org.example.enterprisecasemanagementsystem.course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course save(Course course);
    Optional<Course> findById(Long id);
    List<Course> findAll();
    List<Course> findAllById(Iterable<Long> ids);
    boolean existsById(Long id);
    void delete(Course course);
    void deleteById(Long id);
    void deleteAll();
}