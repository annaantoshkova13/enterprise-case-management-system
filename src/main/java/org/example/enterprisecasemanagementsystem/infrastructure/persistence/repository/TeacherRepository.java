package org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository;

import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.domain.User;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {

    Teacher save(Teacher teacher);
    Optional<Teacher> findById(Long id);
    List<Teacher> findAll();
    void deleteById(Long id);
    void delete(Teacher teacher);
    boolean existsById(Long id);
    Optional<Teacher> findByUser(User user);
    List<Teacher> findByFirstNameContainingIgnoreCase(String firstName);
    List<Teacher> findByLastName(String lastName);
    List<Teacher> findByDepartment(String department);
    List<Teacher> findByDepartmentContainingIgnoreCase(String department);
    List<Teacher> findAllById(Iterable<Long> ids);
}
