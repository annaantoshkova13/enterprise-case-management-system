package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @Mock
    private GetUserByIdUseCase getUserByIdUseCase;

    @Mock
    private ListUsersUseCase listUsersUseCase;

    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @Mock
    private ChangeUserRoleUseCase changeUserRoleUseCase;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("test@example.com", "encodedPassword", Role.STUDENT);
        mockUser.setId(1L);
        mockUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidRequest() {
        // Arrange
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
                "test@example.com",
                "password123",
                "STUDENT"
        );

        when(createUserUseCase.execute(
                any(String.class),
                any(String.class),
                any(Role.class)
        )).thenReturn(mockUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.createUser(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<UserResponseDTO> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("User created successfully", apiResponse.getMessage());

        UserResponseDTO userDTO = apiResponse.getData();
        assertNotNull(userDTO);
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals(Role.STUDENT, userDTO.getRole());

        verify(createUserUseCase).execute(
                "test@example.com",
                "password123",
                Role.STUDENT
        );
    }

    @Test
    void createUser_ShouldReturnError_WhenInvalidRole() {
        // Arrange
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
                "test@example.com",
                "password123",
                "INVALID_ROLE"
        );

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.createUser(requestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<UserResponseDTO> apiResponse = response.getBody();
        assertFalse(apiResponse.isSuccess());
        assertTrue(apiResponse.getMessage().contains("Invalid role"));

        verify(createUserUseCase, never()).execute(any(), any(), any());
    }

    @Test
    void updateUser_ShouldUpdateSuccessfully_WhenValidRequest() {
        // Arrange
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(
                "updated@example.com",
                "newPassword123"
        );

        User updatedUser = new User("updated@example.com", "newEncodedPassword", Role.STUDENT);
        updatedUser.setId(1L);

        when(updateUserProfileUseCase.execute(
                eq(1L),
                any(String.class),
                any(String.class)
        )).thenReturn(updatedUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.updateUser(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User updated successfully", response.getBody().getMessage());

        verify(updateUserProfileUseCase).execute(1L, "updated@example.com", "newPassword123");
    }

    @Test
    void updateUser_ShouldUpdateOnlyEmail_WhenPasswordIsNull() {
        // Arrange
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(
                "updated@example.com",
                null
        );

        when(updateUserProfileUseCase.execute(
                eq(1L),
                any(String.class),
                eq(null)
        )).thenReturn(mockUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.updateUser(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());

        verify(updateUserProfileUseCase).execute(1L, "updated@example.com", null);
    }

    @Test
    void updateUser_ShouldUpdateOnlyPassword_WhenEmailIsNull() {
        // Arrange
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(
                null,
                "newPassword123"
        );

        when(updateUserProfileUseCase.execute(
                eq(1L),
                eq(null),
                any(String.class)
        )).thenReturn(mockUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.updateUser(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());

        verify(updateUserProfileUseCase).execute(1L, null, "newPassword123");
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(getUserByIdUseCase.execute(1L)).thenReturn(mockUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.getUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        UserResponseDTO userDTO = response.getBody().getData();
        assertNotNull(userDTO);
        assertEquals("test@example.com", userDTO.getEmail());

        verify(getUserByIdUseCase).execute(1L);
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(getUserByIdUseCase.execute(999L))
                .thenThrow(new ResourceNotFoundException("User", "id", 999L));

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userController.getUser(999L)
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(getUserByIdUseCase).execute(999L);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User("user1@example.com", "pass1", Role.STUDENT);
        user1.setId(1L);

        User user2 = new User("user2@example.com", "pass2", Role.TEACHER);
        user2.setId(2L);

        List<User> users = Arrays.asList(user1, user2);

        when(listUsersUseCase.execute()).thenReturn(users);

        // Act
        ResponseEntity<ApiResponse<List<UserResponseDTO>>> response =
                userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<UserResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<UserResponseDTO> userDTOs = apiResponse.getData();
        assertNotNull(userDTOs);
        assertEquals(2, userDTOs.size());

        verify(listUsersUseCase).execute();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        // Arrange
        when(listUsersUseCase.execute()).thenReturn(List.of());

        // Act
        ResponseEntity<ApiResponse<List<UserResponseDTO>>> response =
                userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<List<UserResponseDTO>> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());

        List<UserResponseDTO> userDTOs = apiResponse.getData();
        assertNotNull(userDTOs);
        assertTrue(userDTOs.isEmpty());
    }

    @Test
    void deleteUser_ShouldDeleteSuccessfully() {
        // Arrange
        doNothing().when(deleteUserUseCase).execute(1L);

        // Act
        ResponseEntity<ApiResponse<Void>> response =
                userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiResponse<Void> apiResponse = response.getBody();
        assertTrue(apiResponse.isSuccess());
        assertEquals("User deleted successfully", apiResponse.getMessage());
        assertNull(apiResponse.getData());

        verify(deleteUserUseCase).execute(1L);
    }

    @Test
    void register_ShouldRegisterStudentSuccessfully() {
        // Arrange
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
                "student@example.com",
                "password123",
                "STUDENT"
        );

        when(createUserUseCase.execute(
                any(String.class),
                any(String.class),
                eq(Role.STUDENT)
        )).thenReturn(mockUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.register(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User registered successfully", response.getBody().getMessage());

        verify(createUserUseCase).execute(
                "student@example.com",
                "password123",
                Role.STUDENT
        );
    }

    @Test
    void register_ShouldRegisterTeacherSuccessfully() {
        // Arrange
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
                "teacher@example.com",
                "password123",
                "TEACHER"
        );

        User teacherUser = new User("teacher@example.com", "encodedPassword", Role.TEACHER);
        teacherUser.setId(2L);

        when(createUserUseCase.execute(
                any(String.class),
                any(String.class),
                eq(Role.TEACHER)
        )).thenReturn(teacherUser);

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.register(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());

        verify(createUserUseCase).execute(
                "teacher@example.com",
                "password123",
                Role.TEACHER
        );
    }

    @Test
    void register_ShouldReturnError_WhenTryingToRegisterAsAdmin() {
        // Arrange
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
                "admin@example.com",
                "password123",
                "ADMIN"
        );

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.register(requestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Cannot register as ADMIN"));

        verify(createUserUseCase, never()).execute(any(), any(), any());
    }

    @Test
    void register_ShouldReturnError_WhenEmailAlreadyExists() {
        // Arrange
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO(
                "existing@example.com",
                "password123",
                "STUDENT"
        );

        when(createUserUseCase.execute(
                any(String.class),
                any(String.class),
                any(Role.class)
        )).thenThrow(new BusinessException("User with email existing@example.com already exists"));

        // Act
        ResponseEntity<ApiResponse<UserResponseDTO>> response =
                userController.register(requestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());

        verify(createUserUseCase).execute(
                "existing@example.com",
                "password123",
                Role.STUDENT
        );
    }

    @Test
    void testEndpoint_ShouldReturnSuccessResponse() {
        Map<String, String> request = Map.of("key", "value");

        ResponseEntity<ApiResponse<String>> response =
                userController.testEndpoint(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Test endpoint working", response.getBody().getData());
    }
}
