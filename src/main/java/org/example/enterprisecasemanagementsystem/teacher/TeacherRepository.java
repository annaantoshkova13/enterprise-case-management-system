package org.example.enterprisecasemanagementsystem.teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {
    Teacher save(Teacher teacher);
    Optional<Teacher> findById(Long id);
    List<Teacher> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
