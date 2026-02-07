package org.example.enterprisecasemanagementsystem.student;

import org.example.enterprisecasemanagementsystem.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStudentRepository extends StudentRepository, JpaRepository<Student, Long> {

    @Override
    List<Student> findByGroupName(String groupName);

    @Override
    Optional<Student> findByUser(User user);

    @Override
    List<Student> findByFirstNameContainingIgnoreCase(String firstName);

    @Override
    List<Student> findByLastName(String lastName);
}
