package org.example.enterprisecasemanagementsystem.application.course;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UnenrollStudentUseCase {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public Course execute(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        course.unenrollStudent(student);
        return courseRepository.save(course);
    }
}
