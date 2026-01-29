package org.example.enterprisecasemanagementsystem;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.course.Course;
import org.example.enterprisecasemanagementsystem.course.CourseRepository;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.student.StudentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollStudentUseCase {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public Course execute(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        course.enrollStudent(student);
        return courseRepository.save(course);
    }
}
