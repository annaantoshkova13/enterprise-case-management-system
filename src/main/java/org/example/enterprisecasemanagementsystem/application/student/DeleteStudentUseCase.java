package org.example.enterprisecasemanagementsystem.application.student;

import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;

public class DeleteStudentUseCase {

    private final StudentRepository studentRepository;

    public DeleteStudentUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void execute(Long studentId) {
        studentRepository.deleteById(studentId);
    }
}
