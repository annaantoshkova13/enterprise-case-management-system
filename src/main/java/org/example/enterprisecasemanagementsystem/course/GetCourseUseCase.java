package org.example.enterprisecasemanagementsystem.course;

import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
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
