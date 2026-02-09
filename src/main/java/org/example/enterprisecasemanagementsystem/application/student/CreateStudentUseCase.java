package org.example.enterprisecasemanagementsystem.application.student;

import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;


public class CreateStudentUseCase {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public CreateStudentUseCase(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public Student execute(String firstName, String lastName, String groupName, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return execute(firstName, lastName, groupName, user);
    }

    public Student execute(String firstName, String lastName, String groupName, User user) {
        if (user == null) {
            throw new BusinessException("User cannot be null");
        }

        Student student = new Student(firstName, lastName, groupName, user);
        return studentRepository.save(student);
    }
}
