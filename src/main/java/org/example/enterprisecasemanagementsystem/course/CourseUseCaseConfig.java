package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.EnrollStudentUseCase;
import org.example.enterprisecasemanagementsystem.UnenrollStudentUseCase;
import org.example.enterprisecasemanagementsystem.student.StudentRepository;
import org.example.enterprisecasemanagementsystem.teacher.TeacherRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseUseCaseConfig {

    @Bean
    public CreateCourseUseCase createCourseUseCase(CourseRepository repository, TeacherRepository teacherRepository){
        return new CreateCourseUseCase(repository, teacherRepository);
    }

    @Bean
    public UpdateCourseUseCase updateCourseUseCase(CourseRepository repository){
        return new UpdateCourseUseCase(repository);
    }

    @Bean
    public GetCourseUseCase getCourseUseCase(CourseRepository repository){
        return new GetCourseUseCase(repository);
    }

    @Bean
    public ListCourseUseCase listCourseUseCase(CourseRepository repository){
        return new ListCourseUseCase(repository);
    }

    @Bean
    public DeleteCourseUseCase deleteCourseUseCase(CourseRepository repository){
        return new DeleteCourseUseCase(repository);
    }

    @Bean
    public EnrollStudentUseCase enrollStudentUseCase(
            CourseRepository courseRepository,
            StudentRepository studentRepository) {
        return new EnrollStudentUseCase(courseRepository, studentRepository);
    }

    @Bean
    public UnenrollStudentUseCase unenrollStudentUseCase(
            CourseRepository courseRepository,
            StudentRepository studentRepository) {
        return new UnenrollStudentUseCase(courseRepository, studentRepository);
    }
}
