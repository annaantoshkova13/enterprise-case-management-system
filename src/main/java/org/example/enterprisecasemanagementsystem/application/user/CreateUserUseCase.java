package org.example.enterprisecasemanagementsystem.application.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("User with email " + email + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, role);
        return userRepository.save(user);
    }
}
