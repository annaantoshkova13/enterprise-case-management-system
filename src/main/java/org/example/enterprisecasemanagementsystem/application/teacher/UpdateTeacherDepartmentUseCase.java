package org.example.enterprisecasemanagementsystem.application.teacher;

import jakarta.transaction.Transactional;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;

public class UpdateTeacherDepartmentUseCase {

    private final TeacherRepository teacherRepository;

    public UpdateTeacherDepartmentUseCase(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public Teacher execute(Long teacherId, String newDepartment) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        teacher.changeDepartment(newDepartment);
        return teacherRepository.save(teacher);
    }
}
