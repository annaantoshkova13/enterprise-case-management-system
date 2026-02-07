package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.course.*;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.example.enterprisecasemanagementsystem.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CreateCourseUseCase createCourseUseCase;

    @Mock
    private UpdateCourseUseCase updateCourseUseCase;

    @Mock
    private GetCourseUseCase getCourseUseCase;

    @Mock
    private ListCourseUseCase listCourseUseCase;

    @Mock
    private DeleteCourseUseCase deleteCourseUseCase;

    @Mock
    private EnrollStudentUseCase enrollStudentUseCase;

    @Mock
    private UnenrollStudentUseCase unenrollStudentUseCase;

    @InjectMocks
    private CourseController courseController;

    private Course mockCourse;
    private Teacher mockTeacher;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("teacher@example.com", "password", Role.TEACHER);
        mockUser.setId(1L);

        mockTeacher = new Teacher("John", "Doe", "Computer Science", mockUser);
        mockTeacher.setId(100L);

        mockCourse = new Course("Mathematics", "Math course", mockTeacher, 30);
        mockCourse.setId(1L);
    }

    @Test
    void createCourse_ShouldReturnCreatedCourse_WhenValidRequest() {
        CreateCourseRequestDTO requestDTO = new CreateCourseRequestDTO(
                "Mathematics",
                "Basic mathematics course",
                100L,
                30
        );

        when(createCourseUseCase.execute(
                any(String.class),
                any(String.class),
                any(Long.class),
                any(Integer.class)
        )).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.createCourse(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<CourseResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Course created successfully", apiResponse.getMessage());

        CourseResponseDTO courseDTO = apiResponse.getData();
        assertNotNull(courseDTO);
        assertEquals("Mathematics", courseDTO.getTitle());
        assertEquals(1L, courseDTO.getId());
        assertNotNull(courseDTO.getCreatedAt());
        assertNotNull(courseDTO.getTeacher());

        verify(createCourseUseCase).execute(
                "Mathematics",
                "Basic mathematics course",
                100L,
                30
        );
    }

    @Test
    void getCourse_ShouldReturnCourse_WhenCourseExists() {
        when(getCourseUseCase.execute(1L)).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.getCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        CourseResponseDTO courseDTO = response.getBody().getData();
        assertNotNull(courseDTO);
        assertEquals(1L, courseDTO.getId());
        assertEquals("Mathematics", courseDTO.getTitle());

        verify(getCourseUseCase).execute(1L);
    }

    @Test
    void getCourse_ShouldThrowException_WhenCourseNotFound() {
        when(getCourseUseCase.execute(999L))
                .thenThrow(new ResourceNotFoundException("Course", "id", 999L));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> courseController.getCourse(999L)
        );

        assertEquals("Course not found with id: '999'", ex.getMessage());
        verify(getCourseUseCase).execute(999L);
    }

    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        Course course1 = new Course("Math", "Math course", mockTeacher, 30);
        course1.setId(1L);

        Course course2 = new Course("Physics", "Physics course", mockTeacher, 25);
        course2.setId(2L);

        List<Course> courses = Arrays.asList(course1, course2);

        when(listCourseUseCase.execute()).thenReturn(courses);

        ResponseEntity<ApiResponse<List<CourseResponseDTO>>> response =
                courseController.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<CourseResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<CourseResponseDTO> courseDTOs = apiResponse.getData();
        assertNotNull(courseDTOs);
        assertEquals(2, courseDTOs.size());
        assertEquals("Math", courseDTOs.get(0).getTitle());
        assertEquals("Physics", courseDTOs.get(1).getTitle());

        verify(listCourseUseCase).execute();
    }

    @Test
    void getAllCourses_ShouldReturnEmptyList_WhenNoCourses() {
        when(listCourseUseCase.execute()).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<CourseResponseDTO>>> response =
                courseController.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<CourseResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<CourseResponseDTO> courseDTOs = apiResponse.getData();
        assertNotNull(courseDTOs);
        assertTrue(courseDTOs.isEmpty());

        verify(listCourseUseCase).execute();
    }

    @Test
    void deleteCourse_ShouldDeleteSuccessfully() {
        doNothing().when(deleteCourseUseCase).execute(1L);

        ResponseEntity<ApiResponse<Void>> response =
                courseController.deleteCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<Void> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Course deleted successfully", apiResponse.getMessage());
        assertNull(apiResponse.getData());

        verify(deleteCourseUseCase).execute(1L);
    }

    @Test
    void enrollStudent_ShouldEnrollStudentSuccessfully() {
        Student student = new Student("Alice", "Smith", "CS-101", null);
        student.setId(200L);
        mockCourse.getEnrolledStudents().add(student);

        when(enrollStudentUseCase.execute(1L, 200L)).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.enrollStudent(1L, 200L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<CourseResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Student enrolled successfully", apiResponse.getMessage());

        CourseResponseDTO courseDTO = apiResponse.getData();
        assertNotNull(courseDTO);
        assertEquals(1L, courseDTO.getId());

        verify(enrollStudentUseCase).execute(1L, 200L);
    }

    @Test
    void unenrollStudent_ShouldUnenrollStudentSuccessfully() {
        when(unenrollStudentUseCase.execute(1L, 200L)).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.unenrollStudent(1L, 200L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<CourseResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Student unenrolled successfully", apiResponse.getMessage());

        CourseResponseDTO courseDTO = apiResponse.getData();
        assertNotNull(courseDTO);
        assertEquals(1L, courseDTO.getId());

        verify(unenrollStudentUseCase).execute(1L, 200L);
    }

    @Test
    void updateCourse_ShouldUpdateSuccessfully() {
        CreateCourseRequestDTO requestDTO = new CreateCourseRequestDTO(
                "Updated Math",
                "Updated description",
                100L,
                40
        );

        Course updatedCourse = new Course("Updated Math", "Updated description", mockTeacher, 40);
        updatedCourse.setId(1L);

        when(updateCourseUseCase.execute(
                eq(1L),
                any(String.class),
                any(String.class)
        )).thenReturn(updatedCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.updateCourse(1L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<CourseResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Course updated successfully", apiResponse.getMessage());

        CourseResponseDTO courseDTO = apiResponse.getData();
        assertNotNull(courseDTO);
        assertEquals("Updated Math", courseDTO.getTitle());
        assertEquals("Updated description", courseDTO.getDescription());

        verify(updateCourseUseCase).execute(1L, "Updated Math", "Updated description");
    }

    @Test
    void getCourse_ShouldReturnDTOWithEnrolledStudentIds() {
        Student student1 = new Student("Alice", "Smith", "CS-101", null);
        student1.setId(201L);

        Student student2 = new Student("Bob", "Johnson", "CS-101", null);
        student2.setId(202L);

        Set<Student> enrolledStudents = new HashSet<>();
        enrolledStudents.add(student1);
        enrolledStudents.add(student2);
        mockCourse.setEnrolledStudents(enrolledStudents);

        when(getCourseUseCase.execute(1L)).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.getCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CourseResponseDTO dto = response.getBody().getData();
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Mathematics", dto.getTitle());
        assertNotNull(dto.getEnrolledStudentIds());
        assertEquals(2, dto.getEnrolledStudentIds().size());
        assertTrue(dto.getEnrolledStudentIds().contains(201L));
        assertTrue(dto.getEnrolledStudentIds().contains(202L));
        assertNotNull(dto.getTeacher());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void getCourse_ShouldReturnDTOWithEmptyEnrolledStudents_WhenNoStudentsEnrolled() {
        mockCourse.setEnrolledStudents(new HashSet<>());

        when(getCourseUseCase.execute(1L)).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.getCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CourseResponseDTO dto = response.getBody().getData();
        assertNotNull(dto);
        assertNotNull(dto.getEnrolledStudentIds());
        assertTrue(dto.getEnrolledStudentIds().isEmpty());
    }

    @Test
    void getCourse_ShouldReturnDTOWithTeacherInfo() {
        when(getCourseUseCase.execute(1L)).thenReturn(mockCourse);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.getCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CourseResponseDTO dto = response.getBody().getData();
        assertNotNull(dto);
        assertNotNull(dto.getTeacher());
        assertEquals("John", dto.getTeacher().getFirstName());
        assertEquals("Doe", dto.getTeacher().getLastName());
        assertEquals("Computer Science", dto.getTeacher().getDepartment());
    }

    @Test
    void createCourse_ShouldUseDefaultMaxStudents_WhenNotProvided() {
        CreateCourseRequestDTO requestDTO = new CreateCourseRequestDTO(
                "Mathematics",
                "Basic mathematics course",
                100L,
                null
        );

        Course courseWithDefault = new Course("Mathematics", "Basic mathematics course", mockTeacher);
        courseWithDefault.setId(1L);

        when(createCourseUseCase.execute(
                eq("Mathematics"),
                eq("Basic mathematics course"),
                eq(100L),
                eq(null)
        )).thenReturn(courseWithDefault);

        ResponseEntity<ApiResponse<CourseResponseDTO>> response =
                courseController.createCourse(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        CourseResponseDTO courseDTO = response.getBody().getData();
        assertNotNull(courseDTO);
        assertEquals(30, courseDTO.getMaxStudents()); // Дефолтное значение

        verify(createCourseUseCase).execute(
                "Mathematics",
                "Basic mathematics course",
                100L,
                null
        );
    }
}