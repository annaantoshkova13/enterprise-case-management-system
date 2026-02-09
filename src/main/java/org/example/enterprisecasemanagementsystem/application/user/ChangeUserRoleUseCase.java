package org.example.enterprisecasemanagementsystem.application.user;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChangeUserRoleUseCase {

    private final UserRepository userRepository;

    public ChangeUserRoleUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long userId, Role newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("New role cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getRole() == newRole) {
            return user;
        }

        user.setRole(newRole);

        return userRepository.save(user);
    }
}
