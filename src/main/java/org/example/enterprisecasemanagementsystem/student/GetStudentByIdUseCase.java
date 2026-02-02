package org.example.enterprisecasemanagementsystem.student;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;

public class GetStudentByIdUseCase {

    private final StudentRepository studentRepository;

    public GetStudentByIdUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student execute(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }
}
