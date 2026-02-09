package org.example.enterprisecasemanagementsystem.application.config;

import org.example.enterprisecasemanagementsystem.application.student.*;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentUseCaseConfig {

    @Bean
    public CreateStudentUseCase createStudentUseCase(StudentRepository studentRepository,
                                                     UserRepository userRepository) {
        return new CreateStudentUseCase(studentRepository, userRepository);
    }

    @Bean
    public UpdateStudentGroupUseCase updateStudentGroupUseCase(StudentRepository repository){
        return new UpdateStudentGroupUseCase(repository);
    }

    @Bean
    public DeleteStudentUseCase deleteStudentUseCase(StudentRepository repository){
        return new DeleteStudentUseCase(repository);
    }

    @Bean
    public GetStudentByIdUseCase getStudentByIdUseCase(StudentRepository repository){
        return new GetStudentByIdUseCase(repository);
    }

    @Bean
    public ListStudentsUseCase listStudentsUseCase(StudentRepository repository){
        return new ListStudentsUseCase(repository);
    }
}
