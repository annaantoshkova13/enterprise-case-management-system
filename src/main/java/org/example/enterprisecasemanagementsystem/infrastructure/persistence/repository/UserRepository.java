package org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository;

import org.example.enterprisecasemanagementsystem.domain.EmailValue;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
    void delete(User user);
    long count();
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmailValue(EmailValue emailValue);
}
