package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;

public class DeleteTeacherUseCase {

    private final TeacherRepository teacherRepository;

    public DeleteTeacherUseCase(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public void execute(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
