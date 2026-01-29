package org.example.enterprisecasemanagementsystem.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(String email, String password, Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("User with email " + email + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, role);
        return userRepository.save(user);
    }
}
