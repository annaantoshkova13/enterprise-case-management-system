package org.example.enterprisecasemanagementsystem.student;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
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
