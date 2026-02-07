package org.example.enterprisecasemanagementsystem.teacher;

import org.example.enterprisecasemanagementsystem.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTeacherRepository extends TeacherRepository,JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUser(User user);
    List<Teacher> findByFirstNameContainingIgnoreCase(String firstName);
    List<Teacher> findByLastName(String lastName);
    List<Teacher> findByDepartment(String department);
    List<Teacher> findByDepartmentContainingIgnoreCase(String department);
}
