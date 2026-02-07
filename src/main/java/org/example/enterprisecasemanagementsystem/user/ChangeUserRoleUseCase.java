package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
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
