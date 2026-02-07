package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.EmailValue;
import org.example.enterprisecasemanagementsystem.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    List<User> findAll();
    boolean existsById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    void deleteById(Long id);
    void delete(User user);
    List<User> findByRole(Role role);
    long count();
    Optional<User> findByEmailIgnoreCase(String email);
}
