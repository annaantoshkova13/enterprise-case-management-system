package org.example.enterprisecasemanagementsystem.application.student;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;

import java.util.List;

public class ListStudentsUseCase {

    private final StudentRepository studentRepository;

    public ListStudentsUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<Student> execute() {
        return studentRepository.findAll();
    }

}
