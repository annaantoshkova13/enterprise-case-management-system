package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthenticationIntegrationTest {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateUserWithEncryptedPassword() {
        String email = "auth-test@example.com";
        String plainPassword = "plainPassword123";
        Role role = Role.STUDENT;

        User user = createUserUseCase.execute(email, plainPassword, role);

        System.out.println("Saved password hash: " + user.getPasswordHash());
        System.out.println("Expected to match with: " + plainPassword);

        assertNotNull(user);
        assertEquals(email, user.getEmailString());
        assertEquals(role, user.getRole());

        assertNotEquals(plainPassword, user.getPasswordHash());

        assertTrue(user.getPasswordHash().startsWith("$2a$"));

        boolean matches = passwordEncoder.matches(plainPassword, user.getPasswordHash());
        System.out.println("Password matches: " + matches);
        assertTrue(matches, "Password should match the hash");
    }

    @Test
    void shouldCreateDifferentRoles() {
        User student = createUserUseCase.execute("student@auth.com", "pass", Role.STUDENT);
        User teacher = createUserUseCase.execute("teacher@auth.com", "pass", Role.TEACHER);
        User admin = createUserUseCase.execute("admin@auth.com", "pass", Role.ADMIN);

        assertEquals(Role.STUDENT, student.getRole());
        assertEquals(Role.TEACHER, teacher.getRole());
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    void shouldUpdateUserPassword() {
        User user = createUserUseCase.execute("update-pass@example.com", "oldPassword", Role.STUDENT);

        UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase(
                userRepository, passwordEncoder);

        User updatedUser = updateUserProfileUseCase.execute(
                user.getId(),
                "update-pass@example.com",
                "newPassword123"
        );

        assertTrue(passwordEncoder.matches("newPassword123", updatedUser.getPasswordHash()));
        assertFalse(passwordEncoder.matches("oldPassword", updatedUser.getPasswordHash()));
    }

    @Test
    void shouldUpdateUserEmail() {
        User user = createUserUseCase.execute("old-email@example.com", "password", Role.TEACHER);

        UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase(
                userRepository, passwordEncoder);

        User updatedUser = updateUserProfileUseCase.execute(
                user.getId(),
                "new-email@example.com",
                null
        );

        assertEquals("new-email@example.com", updatedUser.getEmailString());
        assertTrue(passwordEncoder.matches("password", updatedUser.getPasswordHash()));
    }
}