package org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository;

import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.domain.User;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);
    Optional<Student> findById(Long id);
    List<Student> findAll();
    void deleteById(Long id);
    void delete(Student student);
    long count();
    List<Student> findByGroupName(String groupName);
    Optional<Student> findByUser(User user);
    List<Student> findByFirstNameContainingIgnoreCase(String firstName);
    List<Student> findByLastName(String lastName);
}
