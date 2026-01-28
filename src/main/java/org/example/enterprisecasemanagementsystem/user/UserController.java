package org.example.enterprisecasemanagementsystem.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody CreateUserRequestDTO requestDTO) {
        User user = createUserUseCase.execute(
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                requestDTO.getRole()
        );
        UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDTO, "User created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDTO requestDTO) {
        User user = updateUserProfileUseCase.execute(id, requestDTO.getEmail());
        UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
        return ResponseEntity.ok(ApiResponse.success(responseDTO, "User updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable Long id) {
        User user = getUserByIdUseCase.execute(id);
        UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<User> users = listUsersUseCase.execute();
        List<UserResponseDTO> responseDTOs = users.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDTOs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserResponseDTO>> changeRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        User user = changeUserRoleUseCase.execute(id, role);
        UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Role changed successfully"));
    }
}
