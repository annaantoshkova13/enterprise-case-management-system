package org.example.enterprisecasemanagementsystem.teacher;

import org.example.enterprisecasemanagementsystem.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTeacherRepository extends TeacherRepository,JpaRepository<Teacher, Long> {

    @Override
    @Query("SELECT t FROM Teacher t WHERE t.user = :user")
    Optional<Teacher> findByUser(@Param("user") User user);

    @Override
    List<Teacher> findByFirstNameContainingIgnoreCase(String firstName);

    @Override
    List<Teacher> findByLastName(String lastName);

    @Override
    List<Teacher> findByDepartment(String department);

    @Override
    List<Teacher> findByDepartmentContainingIgnoreCase(String department);

    @Override
    List<Teacher> findAllById(Iterable<Long> ids);

    @Override
    boolean existsById(Long id);

    @Override
    void delete(Teacher teacher);
}
