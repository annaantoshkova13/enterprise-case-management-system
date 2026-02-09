package org.example.enterprisecasemanagementsystem.application.student;

import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;

public class UpdateStudentGroupUseCase {

    private final StudentRepository studentRepository;

    public UpdateStudentGroupUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student execute(Long studentId, String newGroup) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        student.changeGroup(newGroup);
        return studentRepository.save(student);
    }
}
