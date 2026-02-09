package org.example.enterprisecasemanagementsystem.infrastructure.web.controller;

import org.example.enterprisecasemanagementsystem.application.student.*;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.web.ApiResponse;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateStudentRequestDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.StudentResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private CreateStudentUseCase createStudentUseCase;

    @Mock
    private UpdateStudentGroupUseCase updateStudentGroupUseCase;

    @Mock
    private GetStudentByIdUseCase getStudentByIdUseCase;

    @Mock
    private ListStudentsUseCase listStudentsUseCase;

    @Mock
    private DeleteStudentUseCase deleteStudentUseCase;

    @InjectMocks
    private StudentController studentController;

    private User mockUser;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        mockUser = new User("student@example.com", "password", Role.STUDENT);
        mockUser.setId(1L);

        mockStudent = new Student("Alice", "Smith", "CS-101", mockUser);
        mockStudent.setId(100L);
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent_WhenValidRequest() {
        CreateStudentRequestDTO requestDTO = new CreateStudentRequestDTO(
                "Alice",
                "Smith",
                "CS-101",
                1L
        );

        when(createStudentUseCase.execute(
                any(String.class),
                any(String.class),
                any(String.class),
                any(Long.class)
        )).thenReturn(mockStudent);

        ResponseEntity<ApiResponse<StudentResponseDTO>> response =
                studentController.createStudent(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<StudentResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Student created successfully", apiResponse.getMessage());

        StudentResponseDTO studentDTO = apiResponse.getData();
        assertNotNull(studentDTO);
        assertEquals(100L, studentDTO.getId());
        assertEquals("Alice", studentDTO.getFirstName());
        assertEquals("Smith", studentDTO.getLastName());
        assertEquals("CS-101", studentDTO.getGroupName());
        assertNotNull(studentDTO.getUser());

        verify(createStudentUseCase).execute(
                "Alice",
                "Smith",
                "CS-101",
                1L
        );
    }

    @Test
    void getStudent_ShouldReturnStudent_WhenStudentExists() {
        when(getStudentByIdUseCase.execute(100L)).thenReturn(mockStudent);

        ResponseEntity<ApiResponse<StudentResponseDTO>> response =
                studentController.getStudent(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<StudentResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        StudentResponseDTO studentDTO = apiResponse.getData();
        assertNotNull(studentDTO);
        assertEquals(100L, studentDTO.getId());
        assertEquals("Alice", studentDTO.getFirstName());
        assertEquals("Smith", studentDTO.getLastName());

        verify(getStudentByIdUseCase).execute(100L);
    }

    @Test
    void getStudent_ShouldThrowException_WhenStudentNotFound() {
        when(getStudentByIdUseCase.execute(999L))
                .thenThrow(new ResourceNotFoundException("Student", "id", 999L));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> studentController.getStudent(999L)
        );

        assertEquals("Student not found with id: '999'", ex.getMessage());
        verify(getStudentByIdUseCase).execute(999L);
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        Student student1 = new Student("Alice", "Smith", "CS-101", mockUser);
        student1.setId(100L);

        Student student2 = new Student("Bob", "Johnson", "CS-102", mockUser);
        student2.setId(101L);

        List<Student> students = Arrays.asList(student1, student2);

        when(listStudentsUseCase.execute()).thenReturn(students);

        ResponseEntity<ApiResponse<List<StudentResponseDTO>>> response =
                studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<StudentResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<StudentResponseDTO> studentDTOs = apiResponse.getData();
        assertNotNull(studentDTOs);
        assertEquals(2, studentDTOs.size());
        assertEquals("Alice", studentDTOs.get(0).getFirstName());
        assertEquals("Bob", studentDTOs.get(1).getFirstName());

        verify(listStudentsUseCase).execute();
    }

    @Test
    void getAllStudents_ShouldReturnEmptyList_WhenNoStudents() {
        when(listStudentsUseCase.execute()).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<StudentResponseDTO>>> response =
                studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<StudentResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<StudentResponseDTO> studentDTOs = apiResponse.getData();
        assertNotNull(studentDTOs);
        assertTrue(studentDTOs.isEmpty());

        verify(listStudentsUseCase).execute();
    }

    @Test
    void deleteStudent_ShouldDeleteSuccessfully() {
        doNothing().when(deleteStudentUseCase).execute(100L);

        ResponseEntity<ApiResponse<Void>> response =
                studentController.deleteStudent(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<Void> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Student deleted successfully", apiResponse.getMessage());
        assertNull(apiResponse.getData());

        verify(deleteStudentUseCase).execute(100L);
    }

    @Test
    void getStudent_ShouldReturnDTOWithUserInfo() {
        when(getStudentByIdUseCase.execute(100L)).thenReturn(mockStudent);

        ResponseEntity<ApiResponse<StudentResponseDTO>> response =
                studentController.getStudent(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        StudentResponseDTO studentDTO = response.getBody().getData();
        assertNotNull(studentDTO);
        assertNotNull(studentDTO.getUser());
        assertEquals("student@example.com", studentDTO.getUser().getEmail());
        assertEquals(Role.STUDENT, studentDTO.getUser().getRole());
    }

    @Test
    void createStudent_ShouldThrowException_WhenUserNotFound() {
        CreateStudentRequestDTO requestDTO = new CreateStudentRequestDTO(
                "Alice",
                "Smith",
                "CS-101",
                999L
        );

        when(createStudentUseCase.execute(
                any(String.class),
                any(String.class),
                any(String.class),
                any(Long.class)
        )).thenThrow(new ResourceNotFoundException("User", "id", 999L));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> studentController.createStudent(requestDTO)
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(createStudentUseCase).execute(
                "Alice",
                "Smith",
                "CS-101",
                999L
        );
    }
}
