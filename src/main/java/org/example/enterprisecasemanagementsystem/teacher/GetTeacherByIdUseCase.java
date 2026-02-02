package org.example.enterprisecasemanagementsystem.teacher;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;

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
