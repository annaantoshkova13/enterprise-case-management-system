package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;

public class UpdateCourseUseCase {

    private final CourseRepository courseRepository;

    public UpdateCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Course execute(Long id, String name, String description) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        course.update(name, description);
        return courseRepository.save(course);
    }
}
