package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;

import java.util.List;

public class ListTeachersUseCase {

    private final TeacherRepository teacherRepository;

    public ListTeachersUseCase(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> execute() {
        return teacherRepository.findAll();
    }
}
