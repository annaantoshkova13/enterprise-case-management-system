package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;

public class GetTeacherByIdUseCase {

    private final TeacherRepository teacherRepository;

    public GetTeacherByIdUseCase(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher execute(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
    }
}
