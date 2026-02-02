package org.example.enterprisecasemanagementsystem.teacher;

import jakarta.transaction.Transactional;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;

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
