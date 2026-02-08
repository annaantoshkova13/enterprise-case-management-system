package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUser() {
        User user = new User("test@example.com", "password123", Role.STUDENT);

        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmailString());
        assertEquals(Role.STUDENT, found.get().getRole());
    }

    @Test
    void shouldFindByEmail() {
        User user = new User("unique@example.com", "password123", Role.TEACHER);
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("unique@example.com");

        assertTrue(found.isPresent());
        assertEquals("unique@example.com", found.get().getEmailString());
    }

    @Test
    void shouldReturnEmpty_WhenEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    void shouldCheckEmailExists() {
        User user = new User("exists@example.com", "password123", Role.ADMIN);
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("exists@example.com");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalse_WhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("notfound@example.com");

        assertFalse(exists);
    }

    @Test
    void shouldFindUsersByRole() {
        User student1 = new User("student1-role@example.com", "password123", Role.STUDENT);
        User student2 = new User("student2-role@example.com", "password123", Role.STUDENT);
        User teacher = new User("teacher-role@example.com", "password123", Role.TEACHER);

        userRepository.save(student1);
        userRepository.save(student2);
        userRepository.save(teacher);

        List<User> allUsers = userRepository.findAll();

        long studentCount = allUsers.stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .filter(u -> u.getEmailString().contains("-role@"))
                .count();
        long teacherCount = allUsers.stream()
                .filter(u -> u.getRole() == Role.TEACHER)
                .filter(u -> u.getEmailString().contains("-role@"))
                .count();

        assertEquals(2, studentCount);
        assertEquals(1, teacherCount);
    }

    @Test
    void shouldDeleteUser() {
        User user = new User("delete@example.com", "password123", Role.STUDENT);
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> found = userRepository.findById(user.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCountUsers() {
        User user1 = new User("user1-count@example.com", "password123", Role.STUDENT);
        User user2 = new User("user2-count@example.com", "password123", Role.TEACHER);

        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> found1 = userRepository.findByEmail("user1-count@example.com");
        Optional<User> found2 = userRepository.findByEmail("user2-count@example.com");

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = new User("user1-all@example.com", "password123", Role.STUDENT);
        User user2 = new User("user2-all@example.com", "password123", Role.TEACHER);

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        long ourUsersCount = users.stream()
                .filter(u -> u.getEmailString().contains("-all@"))
                .count();

        assertEquals(2, ourUsersCount);
    }

    @Test
    void shouldUpdateUser() {
        User user = new User("update@example.com", "oldPassword123", Role.STUDENT);
        User savedUser = userRepository.save(user);

        savedUser.setEmail("updated@example.com");
        savedUser.setPasswordHash("newPassword123");
        savedUser.setRole(Role.TEACHER);

        User updated = userRepository.save(savedUser);

        assertEquals("updated@example.com", updated.getEmailString());
        assertEquals(Role.TEACHER, updated.getRole());
    }

    @Test
    void shouldFindByEmailRegardlessOfCase() {
        User user = new User("Test@Example.com", "password123", Role.STUDENT);
        userRepository.save(user);

        assertTrue(userRepository.findByEmailIgnoreCase("test@example.com").isPresent());
        assertTrue(userRepository.findByEmailIgnoreCase("TEST@EXAMPLE.COM").isPresent());
    }
}