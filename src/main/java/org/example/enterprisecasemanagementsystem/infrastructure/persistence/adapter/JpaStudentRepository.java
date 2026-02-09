package org.example.enterprisecasemanagementsystem.infrastructure.persistence.adapter;

import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStudentRepository extends StudentRepository, JpaRepository<Student, Long> {

    @Override
    @Query("SELECT s FROM Student s WHERE s.groupName = :groupName")
    List<Student> findByGroupName(@Param("groupName") String groupName);

    @Override
    @Query("SELECT s FROM Student s WHERE s.user = :user")
    Optional<Student> findByUser(@Param("user") User user);

    @Override
    List<Student> findByFirstNameContainingIgnoreCase(String firstName);

    @Override
    List<Student> findByLastName(String lastName);

    @Override
    long count();

    @Override
    void delete(Student student);
}
