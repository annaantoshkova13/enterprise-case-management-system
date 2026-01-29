package org.example.enterprisecasemanagementsystem.user;

import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
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

        if (!user.getEmail().getValue().equals(newEmail) &&
                userRepository.findByEmail(newEmail).isPresent()) {
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
