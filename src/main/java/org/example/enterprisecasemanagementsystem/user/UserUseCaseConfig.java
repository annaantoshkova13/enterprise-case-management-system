package org.example.enterprisecasemanagementsystem.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository repository,
            PasswordEncoder passwordEncoder) {
        return new CreateUserUseCase(repository, passwordEncoder);
    }

    @Bean
    public UpdateUserProfileUseCase updateUserUseCase(
            UserRepository repository,
            PasswordEncoder passwordEncoder) {
        return new UpdateUserProfileUseCase(repository, passwordEncoder);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository repository) {
        return new DeleteUserUseCase(repository);
    }

    @Bean
    public ListUsersUseCase listUsersUseCase(UserRepository repository) {
        return new ListUsersUseCase(repository);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(UserRepository repository) {
        return new GetUserByIdUseCase(repository);
    }

    @Bean
    public ChangeUserRoleUseCase changeUserRoleUseCase(UserRepository repository) {
        return new ChangeUserRoleUseCase(repository);
    }
}
