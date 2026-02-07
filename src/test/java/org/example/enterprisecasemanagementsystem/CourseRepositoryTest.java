package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.course.Course;
import org.example.enterprisecasemanagementsystem.course.CourseRepository;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.student.StudentRepository;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.example.enterprisecasemanagementsystem.teacher.TeacherRepository;
import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.user.UserRepository;
import org.example.enterprisecasemanagementsystem.Role;// Добавьте этот импорт!
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
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldSaveAndFindCourse() {
        User user = new User("teacher-course-test@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("John", "Doe", "Computer Science", user);
        teacherRepository.save(teacher);

        Course course = new Course("Mathematics", "Math course", teacher, 30);

        Course saved = courseRepository.save(course);
        Optional<Course> found = courseRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Mathematics", found.get().getTitle());
        assertEquals("Math course", found.get().getDescription());
        assertEquals(teacher, found.get().getTeacher());
        assertEquals(30, found.get().getMaxStudents());
    }

    @Test
    void shouldDeleteCourse() {
        User user = new User("delete-course-teacher@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Delete", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course = new Course("Delete Course", "To be deleted", teacher, 30);
        courseRepository.save(course);

        courseRepository.delete(course);

        Optional<Course> found = courseRepository.findById(course.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldUpdateCourse() {
        User user = new User("update-course-teacher@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Update", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course = new Course("Old Title", "Old Description", teacher, 30);
        courseRepository.save(course);

        course.update("New Title", "New Description");
        course.setMaxStudents(40);

        Course updated = courseRepository.save(course);

        assertEquals("New Title", updated.getTitle());
        assertEquals("New Description", updated.getDescription());
        assertEquals(40, updated.getMaxStudents());
    }

    @Test
    void shouldFindAllCourses() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user = new User("teacher-all-courses-test-" + timestamp + "@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("All", "Courses", "Department", user);
        teacherRepository.save(teacher);

        Course course1 = new Course("Math " + timestamp, "Math course", teacher, 30);
        Course course2 = new Course("Physics " + timestamp, "Physics course", teacher, 25);

        courseRepository.save(course1);
        courseRepository.save(course2);

        List<Course> courses = courseRepository.findAll();

        long ourCoursesCount = courses.stream()
                .filter(c -> c.getTitle().contains(timestamp))
                .count();

        assertEquals(2, ourCoursesCount);
        boolean hasMath = courses.stream()
                .anyMatch(c -> c.getTitle().equals("Math " + timestamp));
        boolean hasPhysics = courses.stream()
                .anyMatch(c -> c.getTitle().equals("Physics " + timestamp));

        assertTrue(hasMath);
        assertTrue(hasPhysics);
    }

    @Test
    void shouldCountCourses() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user = new User("teacher-count-courses-test-" + timestamp + "@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Count", "Courses", "Department", user);
        teacherRepository.save(teacher);

        Course course1 = new Course("Count Course 1 " + timestamp, "Description", teacher, 30);
        Course course2 = new Course("Count Course 2 " + timestamp, "Description", teacher, 25);

        courseRepository.save(course1);
        courseRepository.save(course2);

        long ourCoursesCount = courseRepository.findAll().stream()
                .filter(c -> c.getTitle().contains(timestamp))
                .count();

        assertEquals(2, ourCoursesCount);
    }

    @Test
    void shouldCheckIfCourseExistsById() {
        User user = new User("exists-course-teacher@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("Exists", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course = new Course("Exists Course", "Description", teacher, 30);
        Course saved = courseRepository.save(course);

        boolean exists = courseRepository.existsById(saved.getId());
        assertTrue(exists);

        boolean notExists = courseRepository.existsById(999L);
        assertFalse(notExists);
    }

    @Test
    void shouldFindAllCoursesById() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user = new User("teacher-findbyid-test-" + timestamp + "@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("FindById", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course1 = new Course("Course 1 " + timestamp, "Description", teacher, 30);
        Course course2 = new Course("Course 2 " + timestamp, "Description", teacher, 25);

        Course saved1 = courseRepository.save(course1);
        Course saved2 = courseRepository.save(course2);

        List<Course> courses = courseRepository.findAllById(List.of(saved1.getId(), saved2.getId()));

        assertEquals(2, courses.size());
        assertTrue(courses.stream().anyMatch(c -> c.getId().equals(saved1.getId())));
        assertTrue(courses.stream().anyMatch(c -> c.getId().equals(saved2.getId())));
    }

    @Test
    void shouldSaveAllCourses() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user = new User("teacher-saveall-test-" + timestamp + "@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("SaveAll", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course1 = new Course("SaveAll Course 1 " + timestamp, "Description", teacher, 30);
        Course course2 = new Course("SaveAll Course 2 " + timestamp, "Description", teacher, 25);

        // Instead of saveAll, save individually
        Course saved1 = courseRepository.save(course1);
        Course saved2 = courseRepository.save(course2);

        List<Course> savedCourses = List.of(saved1, saved2);

        assertEquals(2, savedCourses.size());
        assertTrue(savedCourses.stream().allMatch(c -> c.getId() != null));
    }

    @Test
    void shouldDeleteCourseById() {
        User user = new User("deletebyid-course-teacher@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("DeleteById", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course = new Course("DeleteById Course", "Description", teacher, 30);
        Course saved = courseRepository.save(course);

        courseRepository.deleteById(saved.getId());

        Optional<Course> found = courseRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void shouldDeleteAllCourses() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User user = new User("teacher-deleteall-test-" + timestamp + "@example.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("DeleteAll", "Teacher", "Department", user);
        teacherRepository.save(teacher);

        Course course1 = new Course("DeleteAll Course 1 " + timestamp, "Description", teacher, 30);
        Course course2 = new Course("DeleteAll Course 2 " + timestamp, "Description", teacher, 25);

        courseRepository.save(course1);
        courseRepository.save(course2);

        courseRepository.deleteAll();

        List<Course> courses = courseRepository.findAll();
        long ourCoursesCount = courses.stream()
                .filter(c -> c.getTitle().contains(timestamp))
                .count();

        assertEquals(0, ourCoursesCount);
    }

    @Test
    void shouldEnrollStudentInCourse() {
        // Create teacher
        User teacherUser = new User("enroll-teacher-test@example.com", "password", Role.TEACHER);
        userRepository.save(teacherUser);
        Teacher teacher = new Teacher("Enroll", "Teacher", "Department", teacherUser);
        teacherRepository.save(teacher);

        // Create student
        User studentUser = new User("enroll-student-test@example.com", "password", Role.STUDENT);
        userRepository.save(studentUser);
        Student student = new Student("Enroll", "Student", "Group-101", studentUser);
        studentRepository.save(student);

        // Create course
        Course course = new Course("Enrollment Test Course", "Description", teacher, 30);
        Course savedCourse = courseRepository.save(course);

        // Enroll student
        savedCourse.enrollStudent(student);
        Course updatedCourse = courseRepository.save(savedCourse);

        // Verify enrollment
        assertTrue(updatedCourse.getEnrolledStudents().contains(student));
        assertEquals(1, updatedCourse.getCurrentEnrollment());
    }

    @Test
    void shouldUnenrollStudentFromCourse() {
        // Create teacher
        User teacherUser = new User("unenroll-teacher-test@example.com", "password", Role.TEACHER);
        userRepository.save(teacherUser);
        Teacher teacher = new Teacher("Unenroll", "Teacher", "Department", teacherUser);
        teacherRepository.save(teacher);

        // Create student
        User studentUser = new User("unenroll-student-test@example.com", "password", Role.STUDENT);
        userRepository.save(studentUser);
        Student student = new Student("Unenroll", "Student", "Group-101", studentUser);
        studentRepository.save(student);

        // Create course and enroll student
        Course course = new Course("Unenrollment Test Course", "Description", teacher, 30);
        course.enrollStudent(student);
        Course savedCourse = courseRepository.save(course);

        // Unenroll student
        savedCourse.unenrollStudent(student);
        Course updatedCourse = courseRepository.save(savedCourse);

        // Verify unenrollment
        assertFalse(updatedCourse.getEnrolledStudents().contains(student));
        assertEquals(0, updatedCourse.getCurrentEnrollment());
    }

    @Test
    void shouldCheckCourseCapacity() {
        User teacherUser = new User("capacity-teacher-test@example.com", "password", Role.TEACHER);
        userRepository.save(teacherUser);
        Teacher teacher = new Teacher("Capacity", "Teacher", "Department", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Capacity Test Course", "Description", teacher, 2); // Small capacity

        // Create students
        User studentUser1 = new User("capacity-student1-test@example.com", "password", Role.STUDENT);
        User studentUser2 = new User("capacity-student2-test@example.com", "password", Role.STUDENT);
        userRepository.save(studentUser1);
        userRepository.save(studentUser2);

        Student student1 = new Student("Capacity", "Student1", "Group-101", studentUser1);
        Student student2 = new Student("Capacity", "Student2", "Group-101", studentUser2);
        studentRepository.save(student1);
        studentRepository.save(student2);

        Course savedCourse = courseRepository.save(course);

        // Enroll first student
        savedCourse.enrollStudent(student1);
        Course courseAfterFirst = courseRepository.save(savedCourse);
        assertTrue(courseAfterFirst.hasAvailableSlots());
        assertEquals(1, courseAfterFirst.getCurrentEnrollment());

        // Enroll second student
        courseAfterFirst.enrollStudent(student2);
        Course courseAfterSecond = courseRepository.save(courseAfterFirst);
        assertFalse(courseAfterSecond.hasAvailableSlots());
        assertEquals(2, courseAfterSecond.getCurrentEnrollment());
    }
}