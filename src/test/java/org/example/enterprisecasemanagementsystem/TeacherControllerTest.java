package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.teacher.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private CreateTeacherUseCase createTeacherUseCase;

    @Mock
    private UpdateTeacherDepartmentUseCase updateTeacherDepartmentUseCase;

    @Mock
    private GetTeacherByIdUseCase getTeacherByIdUseCase;

    @Mock
    private ListTeachersUseCase listTeachersUseCase;

    @Mock
    private DeleteTeacherUseCase deleteTeacherUseCase;

    @InjectMocks
    private TeacherController teacherController;

    private User mockUser;
    private Teacher mockTeacher;

    @BeforeEach
    void setUp() {
        mockUser = new User("teacher@example.com", "password", Role.TEACHER);
        mockUser.setId(1L);

        mockTeacher = new Teacher("John", "Doe", "Computer Science", mockUser);
        mockTeacher.setId(100L);
    }

    @Test
    void shouldCreateTeacher_WhenValidRequest() {
        // Given
        CreateTeacherRequestDTO requestDTO = new CreateTeacherRequestDTO(
                "John",
                "Doe",
                "Computer Science",
                1L
        );

        when(createTeacherUseCase.execute(
                any(String.class),
                any(String.class),
                any(String.class),
                any(Long.class)
        )).thenReturn(mockTeacher);

        ResponseEntity<ApiResponse<TeacherResponseDTO>> response =
                teacherController.createTeacher(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<TeacherResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Teacher created successfully", apiResponse.getMessage());

        TeacherResponseDTO teacherDTO = apiResponse.getData();
        assertNotNull(teacherDTO);
        assertEquals(100L, teacherDTO.getId());
        assertEquals("John", teacherDTO.getFirstName());
        assertEquals("Doe", teacherDTO.getLastName());
        assertEquals("Computer Science", teacherDTO.getDepartment());
        assertNotNull(teacherDTO.getUser());

        verify(createTeacherUseCase).execute(
                "John",
                "Doe",
                "Computer Science",
                1L
        );
    }

    @Test
    void shouldGetTeacher_WhenTeacherExists() {
        when(getTeacherByIdUseCase.execute(100L)).thenReturn(mockTeacher);

        ResponseEntity<ApiResponse<TeacherResponseDTO>> response =
                teacherController.getTeacher(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<TeacherResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        TeacherResponseDTO teacherDTO = apiResponse.getData();
        assertNotNull(teacherDTO);
        assertEquals(100L, teacherDTO.getId());
        assertEquals("John", teacherDTO.getFirstName());

        verify(getTeacherByIdUseCase).execute(100L);
    }

    @Test
    void shouldGetAllTeachers() {
        Teacher teacher1 = new Teacher("John", "Doe", "Computer Science", mockUser);
        teacher1.setId(100L);

        Teacher teacher2 = new Teacher("Jane", "Smith", "Mathematics", mockUser);
        teacher2.setId(101L);

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(listTeachersUseCase.execute()).thenReturn(teachers);

        ResponseEntity<ApiResponse<List<TeacherResponseDTO>>> response =
                teacherController.getAllTeachers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<TeacherResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<TeacherResponseDTO> teacherDTOs = apiResponse.getData();
        assertNotNull(teacherDTOs);
        assertEquals(2, teacherDTOs.size());
        assertEquals("John", teacherDTOs.get(0).getFirstName());
        assertEquals("Jane", teacherDTOs.get(1).getFirstName());

        verify(listTeachersUseCase).execute();
    }

    @Test
    void shouldDeleteTeacher() {
        doNothing().when(deleteTeacherUseCase).execute(100L);

        ResponseEntity<ApiResponse<Void>> response =
                teacherController.deleteTeacher(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<Void> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("Teacher deleted successfully", apiResponse.getMessage());
        assertNull(apiResponse.getData());

        verify(deleteTeacherUseCase).execute(100L);
    }

    @Test
    void shouldReturnEmptyList_WhenNoTeachers() {
        when(listTeachersUseCase.execute()).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<TeacherResponseDTO>>> response =
                teacherController.getAllTeachers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<TeacherResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<TeacherResponseDTO> teacherDTOs = apiResponse.getData();
        assertNotNull(teacherDTOs);
        assertTrue(teacherDTOs.isEmpty());

        verify(listTeachersUseCase).execute();
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenTeacherNotFound() {
        when(getTeacherByIdUseCase.execute(999L))
                .thenThrow(new ResourceNotFoundException("Teacher", "id", 999L));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> teacherController.getTeacher(999L)
        );

        assertEquals("Teacher not found with id: '999'", ex.getMessage());
        verify(getTeacherByIdUseCase).execute(999L);
    }
}
