package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.example.enterprisecasemanagementsystem.teacher.TeacherRepository;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_PASSWORD_ALT = "password456";
    private static final String TEST_PASSWORD_ALT2 = "password789";

    @Test
    void shouldSaveAndFindTeacher() {
        User user = new User("teacher@example.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("John", "Doe", "Computer Science", user);

        Teacher saved = teacherRepository.save(teacher);
        Optional<Teacher> found = teacherRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
        assertEquals("Computer Science", found.get().getDepartment());
        assertEquals(user, found.get().getUser());
    }

    @Test
    void shouldFindByUser() {
        User user = new User("unique-teacher-test@example.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Jane", "Doe", "Physics", user);
        teacherRepository.save(teacher);

        Optional<Teacher> found = teacherRepository.findByUser(user);

        assertTrue(found.isPresent());
        assertEquals("Jane", found.get().getFirstName());
        assertEquals(user, found.get().getUser());
    }

    @Test
    void shouldReturnEmpty_WhenUserHasNoTeacher() {
        User user = new User("noteacher@example.com", TEST_PASSWORD, Role.STUDENT);
        userRepository.save(user);

        Optional<Teacher> found = teacherRepository.findByUser(user);

        assertFalse(found.isPresent());
    }

    @Test
    void shouldDeleteTeacher() {
        User user = new User("delete-teacher@example.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Delete", "Me", "Chemistry", user);
        teacherRepository.save(teacher);

        teacherRepository.delete(teacher);

        Optional<Teacher> found = teacherRepository.findById(teacher.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldUpdateTeacherDepartment() {
        User user = new User("update-teacher@example.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Update", "Department", "Biology", user);
        teacherRepository.save(teacher);

        teacher.setDepartment("Advanced Biology");
        Teacher updated = teacherRepository.save(teacher);

        assertEquals("Advanced Biology", updated.getDepartment());
    }

    @Test
    void shouldFindAllTeachers() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user1 = new User("user1-teacher-all-test-" + timestamp + "@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("user2-teacher-all-test-" + timestamp + "@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        userRepository.save(user1);
        userRepository.save(user2);

        Teacher teacher1 = new Teacher("Alice", "Smith", "Computer Science", user1);
        Teacher teacher2 = new Teacher("Bob", "Johnson", "Mathematics", user2);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);

        List<Teacher> teachers = teacherRepository.findAll();

        long ourTeachersCount = teachers.stream()
                .filter(t -> t.getUser().getEmail().toString().contains("teacher-all-test-" + timestamp))
                .count();

        assertEquals(2, ourTeachersCount);
        boolean hasAlice = teachers.stream()
                .anyMatch(t -> t.getFirstName().equals("Alice") &&
                        t.getUser().getEmail().toString().contains("teacher-all-test-" + timestamp));
        boolean hasBob = teachers.stream()
                .anyMatch(t -> t.getFirstName().equals("Bob") &&
                        t.getUser().getEmail().toString().contains("teacher-all-test-" + timestamp));

        assertTrue(hasAlice);
        assertTrue(hasBob);
    }

    @Test
    void shouldCountTeachers() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user1 = new User("user1-teacher-count-test-" + timestamp + "@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("user2-teacher-count-test-" + timestamp + "@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        userRepository.save(user1);
        userRepository.save(user2);

        Teacher teacher1 = new Teacher("Alice", "Smith", "Computer Science", user1);
        Teacher teacher2 = new Teacher("Bob", "Johnson", "Mathematics", user2);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);

        long ourTeachersCount = teacherRepository.findAll().stream()
                .filter(t -> t.getUser().getEmail().toString().contains("teacher-count-test-" + timestamp))
                .count();

        assertEquals(2, ourTeachersCount);
    }

    @Test
    void shouldFindByFirstNameContaining() {
        String uniqueId = "search-" + System.currentTimeMillis();

        User user1 = new User("search1-" + uniqueId + "@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("search2-" + uniqueId + "@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        User user3 = new User("search3-" + uniqueId + "@example.com", TEST_PASSWORD_ALT2, Role.TEACHER);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        Teacher teacher1 = new Teacher("John-" + uniqueId, "Smith", "Physics", user1);
        Teacher teacher2 = new Teacher("Johnny-" + uniqueId, "Johnson", "Chemistry", user2);
        Teacher teacher3 = new Teacher("Robert-" + uniqueId, "Brown", "Biology", user3);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        List<Teacher> allJohnTeachers = teacherRepository.findByFirstNameContainingIgnoreCase("John");

        List<Teacher> johnTeachers = allJohnTeachers.stream()
                .filter(t -> t.getFirstName().contains(uniqueId))
                .collect(Collectors.toList());

        assertEquals(2, johnTeachers.size());
        assertTrue(johnTeachers.stream().allMatch(t -> t.getFirstName().toLowerCase().contains("john")));
    }

    @Test
    void shouldFindByLastName() {
        String uniqueId = "lastname-" + System.currentTimeMillis();

        User user1 = new User("lastname1-" + uniqueId + "@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("lastname2-" + uniqueId + "@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        User user3 = new User("lastname3-" + uniqueId + "@example.com", TEST_PASSWORD_ALT2, Role.TEACHER);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        Teacher teacher1 = new Teacher("Alice", "Smith-" + uniqueId, "Physics", user1);
        Teacher teacher2 = new Teacher("Bob", "Smith-" + uniqueId, "Chemistry", user2);
        Teacher teacher3 = new Teacher("Charlie", "Johnson-" + uniqueId, "Biology", user3);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        List<Teacher> allSmithTeachers = teacherRepository.findByLastName("Smith-" + uniqueId);

        assertEquals(2, allSmithTeachers.size());
        assertTrue(allSmithTeachers.stream().allMatch(t -> t.getLastName().equals("Smith-" + uniqueId)));
    }

    @Test
    void shouldFindByDepartment() {
        String uniqueId = "dept-" + System.currentTimeMillis();

        User user1 = new User("dept1-" + uniqueId + "@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("dept2-" + uniqueId + "@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        User user3 = new User("dept3-" + uniqueId + "@example.com", TEST_PASSWORD_ALT2, Role.TEACHER);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        Teacher teacher1 = new Teacher("Alice", "Smith", "Computer Science " + uniqueId, user1);
        Teacher teacher2 = new Teacher("Bob", "Johnson", "Computer Science " + uniqueId, user2);
        Teacher teacher3 = new Teacher("Charlie", "Brown", "Mathematics " + uniqueId, user3);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        List<Teacher> csTeachers = teacherRepository.findByDepartment("Computer Science " + uniqueId);

        assertEquals(2, csTeachers.size());
        assertTrue(csTeachers.stream().allMatch(t -> t.getDepartment().equals("Computer Science " + uniqueId)));
    }

    @Test
    void shouldFindByDepartmentContaining() {
        String uniqueId = "deptcont-" + System.currentTimeMillis();

        User user1 = new User("deptcont1-" + uniqueId + "@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("deptcont2-" + uniqueId + "@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        User user3 = new User("deptcont3-" + uniqueId + "@example.com", TEST_PASSWORD_ALT2, Role.TEACHER);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        Teacher teacher1 = new Teacher("Alice", "Smith", "Computer Science " + uniqueId, user1);
        Teacher teacher2 = new Teacher("Bob", "Johnson", "Computer Engineering " + uniqueId, user2);
        Teacher teacher3 = new Teacher("Charlie", "Brown", "Mathematics " + uniqueId, user3);

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        List<Teacher> allComputerTeachers = teacherRepository.findByDepartmentContainingIgnoreCase("Computer");

        List<Teacher> computerTeachers = allComputerTeachers.stream()
                .filter(t -> t.getDepartment().contains(uniqueId))
                .collect(Collectors.toList());

        assertEquals(2, computerTeachers.size());
        assertTrue(computerTeachers.stream().allMatch(t -> t.getDepartment().toLowerCase().contains("computer")));
    }


    @Test
    void shouldCheckIfTeacherExistsById() {
        User user = new User("exists-teacher@example.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Exists", "Teacher", "Test Department", user);
        Teacher saved = teacherRepository.save(teacher);

        boolean exists = teacherRepository.existsById(saved.getId());
        assertTrue(exists);

        boolean notExists = teacherRepository.existsById(999L);
        assertFalse(notExists);
    }

    @Test
    void shouldFindAllById() {
        User user1 = new User("id1-teacher-test@example.com", TEST_PASSWORD, Role.TEACHER);
        User user2 = new User("id2-teacher-test@example.com", TEST_PASSWORD_ALT, Role.TEACHER);
        userRepository.save(user1);
        userRepository.save(user2);

        Teacher teacher1 = new Teacher("Teacher1", "Test", "Dept1", user1);
        Teacher teacher2 = new Teacher("Teacher2", "Test", "Dept2", user2);

        Teacher saved1 = teacherRepository.save(teacher1);
        Teacher saved2 = teacherRepository.save(teacher2);

        List<Teacher> teachers = teacherRepository.findAllById(List.of(saved1.getId(), saved2.getId()));

        assertEquals(2, teachers.size());
        assertTrue(teachers.stream().anyMatch(t -> t.getId().equals(saved1.getId())));
        assertTrue(teachers.stream().anyMatch(t -> t.getId().equals(saved2.getId())));
    }
}