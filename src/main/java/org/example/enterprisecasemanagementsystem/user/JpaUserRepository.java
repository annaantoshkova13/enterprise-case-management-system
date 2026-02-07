package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.EmailValue;
import org.example.enterprisecasemanagementsystem.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email.value = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email.value = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :emailValue")
    Optional<User> findByEmailValue(@Param("emailValue") EmailValue emailValue);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") Role role);

    @Query("SELECT u FROM User u WHERE LOWER(u.email.value) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);
}
