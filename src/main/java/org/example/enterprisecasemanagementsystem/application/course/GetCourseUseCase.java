package org.example.enterprisecasemanagementsystem.application.course;

import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCourseUseCase {

    private final CourseRepository courseRepository;

    public Course execute(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id)); // ✅ Исправлено исключение
    }
}
