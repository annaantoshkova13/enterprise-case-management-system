package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.student.StudentRepository;
import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    // Используем длинные пароли для всех тестов
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_PASSWORD_ALT = "password456";
    private static final String TEST_PASSWORD_ALT2 = "password789";

    @Test
    void shouldSaveAndFindStudent() {
        User user = new User("student@example.com", TEST_PASSWORD, Role.STUDENT);
        userRepository.save(user);

        Student student = new Student("Alice", "Smith", "CS-101", user);

        Student saved = studentRepository.save(student);
        Optional<Student> found = studentRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getFirstName());
        assertEquals("Smith", found.get().getLastName());
        assertEquals("CS-101", found.get().getGroupName());
        assertEquals(user, found.get().getUser());
    }

    @Test
    void shouldFindByGroupName() {
        User user1 = new User("student1-group-test@example.com", TEST_PASSWORD, Role.STUDENT);
        User user2 = new User("student2-group-test@example.com", TEST_PASSWORD_ALT, Role.STUDENT);
        User user3 = new User("student3-group-test@example.com", TEST_PASSWORD_ALT2, Role.STUDENT);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Student student1 = new Student("Alice", "Smith", "CS-101", user1);
        Student student2 = new Student("Bob", "Johnson", "CS-101", user2);
        Student student3 = new Student("Charlie", "Brown", "CS-102", user3);

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        List<Student> cs101Students = studentRepository.findByGroupName("CS-101");

        assertEquals(2, cs101Students.size());
        assertTrue(cs101Students.stream().allMatch(s -> s.getGroupName().equals("CS-101")));
    }

    @Test
    void shouldFindByUser() {
        User user = new User("unique@example.com", TEST_PASSWORD, Role.STUDENT);
        userRepository.save(user);

        Student student = new Student("John", "Doe", "CS-101", user);
        studentRepository.save(student);

        Optional<Student> found = studentRepository.findByUser(user);

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals(user, found.get().getUser());
    }

    @Test
    void shouldReturnEmpty_WhenUserHasNoStudent() {
        User user = new User("nostudent@example.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(user);

        Optional<Student> found = studentRepository.findByUser(user);

        assertFalse(found.isPresent());
    }

    @Test
    void shouldDeleteStudent() {
        User user = new User("delete@example.com", TEST_PASSWORD, Role.STUDENT);
        userRepository.save(user);

        Student student = new Student("Delete", "Me", "CS-101", user);
        studentRepository.save(student);

        studentRepository.delete(student);

        Optional<Student> found = studentRepository.findById(student.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldUpdateStudentGroup() {
        User user = new User("update@example.com", TEST_PASSWORD, Role.STUDENT);
        userRepository.save(user);

        Student student = new Student("Update", "Group", "CS-101", user);
        studentRepository.save(student);

        student.setGroupName("CS-102");
        Student updated = studentRepository.save(student);

        assertEquals("CS-102", updated.getGroupName());
    }

    @Test
    void shouldFindAllStudents() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user1 = new User("user1-all-test-" + timestamp + "@example.com", TEST_PASSWORD, Role.STUDENT);
        User user2 = new User("user2-all-test-" + timestamp + "@example.com", TEST_PASSWORD_ALT, Role.STUDENT);
        userRepository.save(user1);
        userRepository.save(user2);

        Student student1 = new Student("Alice", "Smith", "CS-101", user1);
        Student student2 = new Student("Bob", "Johnson", "CS-102", user2);

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<Student> students = studentRepository.findAll();

        long ourStudentsCount = students.stream()
                .filter(s -> s.getUser().getEmail().toString().contains("all-test-" + timestamp))
                .count();

        assertEquals(2, ourStudentsCount);
        boolean hasAlice = students.stream()
                .anyMatch(s -> s.getFirstName().equals("Alice") &&
                        s.getUser().getEmail().toString().contains("all-test-" + timestamp));
        boolean hasBob = students.stream()
                .anyMatch(s -> s.getFirstName().equals("Bob") &&
                        s.getUser().getEmail().toString().contains("all-test-" + timestamp));

        assertTrue(hasAlice);
        assertTrue(hasBob);
    }

    @Test
    void shouldCountStudents() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user1 = new User("user1-count-test-" + timestamp + "@example.com", TEST_PASSWORD, Role.STUDENT);
        User user2 = new User("user2-count-test-" + timestamp + "@example.com", TEST_PASSWORD_ALT, Role.STUDENT);
        userRepository.save(user1);
        userRepository.save(user2);

        Student student1 = new Student("Alice", "Smith", "CS-101", user1);
        Student student2 = new Student("Bob", "Johnson", "CS-102", user2);

        studentRepository.save(student1);
        studentRepository.save(student2);

        long ourStudentsCount = studentRepository.findAll().stream()
                .filter(s -> s.getUser().getEmail().toString().contains("count-test-" + timestamp))
                .count();

        assertEquals(2, ourStudentsCount);
    }

    @Test
    void shouldFindByFirstNameContaining() {
        User user1 = new User("search1-test@example.com", TEST_PASSWORD, Role.STUDENT);
        User user2 = new User("search2-test@example.com", TEST_PASSWORD_ALT, Role.STUDENT);
        User user3 = new User("search3-test@example.com", TEST_PASSWORD_ALT2, Role.STUDENT);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Student student1 = new Student("Alice", "Smith", "CS-101", user1);
        Student student2 = new Student("Alison", "Jones", "CS-101", user2);
        Student student3 = new Student("Bob", "Johnson", "CS-102", user3);

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        List<Student> aliStudents = studentRepository.findByFirstNameContainingIgnoreCase("Ali");

        assertEquals(2, aliStudents.size());
        assertTrue(aliStudents.stream().allMatch(s -> s.getFirstName().toLowerCase().contains("ali")));
    }

    @Test
    void shouldFindByLastName() {
        User user1 = new User("lastname1-test@example.com", TEST_PASSWORD, Role.STUDENT);
        User user2 = new User("lastname2-test@example.com", TEST_PASSWORD_ALT, Role.STUDENT);
        User user3 = new User("lastname3-test@example.com", TEST_PASSWORD_ALT2, Role.STUDENT);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Student student1 = new Student("Alice", "Smith", "CS-101", user1);
        Student student2 = new Student("Bob", "Smith", "CS-102", user2);
        Student student3 = new Student("Charlie", "Johnson", "CS-101", user3);

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        List<Student> smithStudents = studentRepository.findByLastName("Smith");

        assertEquals(2, smithStudents.size());
        assertTrue(smithStudents.stream().allMatch(s -> s.getLastName().equals("Smith")));
    }
}