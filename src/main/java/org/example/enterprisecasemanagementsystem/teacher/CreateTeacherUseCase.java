package org.example.enterprisecasemanagementsystem.teacher;

import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.user.UserRepository;

public class CreateTeacherUseCase {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public CreateTeacherUseCase(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    public Teacher execute(String firstName, String lastName, String department, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return execute(firstName, lastName, department, user);
    }

    public Teacher execute(String firstName, String lastName, String department, User user) {
        if (user == null) {
            throw new BusinessException("User cannot be null");
        }

        Teacher teacher = new Teacher(firstName, lastName, department, user);
        return teacherRepository.save(teacher);
    }
}
