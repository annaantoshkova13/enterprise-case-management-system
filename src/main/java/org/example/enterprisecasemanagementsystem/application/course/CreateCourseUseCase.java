package org.example.enterprisecasemanagementsystem.application.course;

import jakarta.transaction.Transactional;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;

public class CreateCourseUseCase {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CreateCourseUseCase(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public Course execute(String title, String description, Long teacherId) {
        return execute(title, description, teacherId, 30); // Default 30 students
    }

    @Transactional
    public Course execute(String title, String description, Long teacherId, Integer maxStudents) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        Course course = new Course(title, description, teacher, maxStudents);
        return courseRepository.save(course);
    }
}
