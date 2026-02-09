package org.example.enterprisecasemanagementsystem.application.course;

import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;

public class DeleteCourseUseCase {

    private final CourseRepository courseRepository;

    public DeleteCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: '" + courseId + "'");
        }

        courseRepository.deleteById(courseId);
    }
}
