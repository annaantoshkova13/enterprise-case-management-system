package org.example.enterprisecasemanagementsystem.application.user;

import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(Long userId, String newEmail, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String currentEmail = user.getEmailString();

        if (!currentEmail.equals(newEmail) &&
                userRepository.existsByEmail(newEmail)) {
            throw new BusinessException("Email " + newEmail + " is already taken");
        }

        user.setEmail(newEmail);

        if (newPassword != null && !newPassword.isBlank()) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(encodedPassword);
        }

        return userRepository.save(user);
    }
}
