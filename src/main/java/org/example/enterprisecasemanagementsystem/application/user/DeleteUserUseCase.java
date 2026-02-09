package org.example.enterprisecasemanagementsystem.application.user;

import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
    }
}
