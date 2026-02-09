package org.example.enterprisecasemanagementsystem.application.config;

import org.example.enterprisecasemanagementsystem.application.teacher.*;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeacherUseCaseConfig {

    @Bean
    public CreateTeacherUseCase createTeacherUseCase(TeacherRepository teacherRepository,
                                                     UserRepository userRepository) {
        return new CreateTeacherUseCase(teacherRepository, userRepository);
    }

    @Bean
    public UpdateTeacherDepartmentUseCase updateTeacherDepartmentUseCase(TeacherRepository repository) {
        return new UpdateTeacherDepartmentUseCase(repository);
    }

    @Bean
    public DeleteTeacherUseCase deleteTeacherUseCase(TeacherRepository repository) {
        return new DeleteTeacherUseCase(repository);
    }

    @Bean
    public GetTeacherByIdUseCase getTeacherByIdUseCase(TeacherRepository repository) {
        return new GetTeacherByIdUseCase(repository);
    }

    @Bean
    public ListTeachersUseCase listTeachersUseCase(TeacherRepository repository) {
        return new ListTeachersUseCase(repository);
    }
}
