package org.example.enterprisecasemanagementsystem.infrastructure.persistence.adapter;

import org.example.enterprisecasemanagementsystem.domain.EmailValue;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {

    @Override
    @Query("SELECT u FROM User u WHERE u.email.value = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Override
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email.value = :email")
    boolean existsByEmail(@Param("email") String email);

    @Override
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") Role role);

    @Override
    @Query("SELECT u FROM User u WHERE LOWER(u.email.value) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :emailValue")
    Optional<User> findByEmailValue(@Param("emailValue") EmailValue emailValue);
}
