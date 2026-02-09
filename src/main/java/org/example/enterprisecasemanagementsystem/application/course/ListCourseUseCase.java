package org.example.enterprisecasemanagementsystem.application.course;

import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.domain.Course;

import java.util.List;

public class ListCourseUseCase {

    private final CourseRepository courseRepository;

    public ListCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> execute() {
        return courseRepository.findAll();
    }
}
