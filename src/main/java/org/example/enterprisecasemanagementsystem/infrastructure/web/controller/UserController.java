package org.example.enterprisecasemanagementsystem.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.application.user.*;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.infrastructure.web.ApiResponse;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateUserRequestDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.UpdateUserRequestDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
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
        try {
            Role role = requestDTO.getRoleAsEnum();

            User user = createUserUseCase.execute(
                    requestDTO.getEmail(),
                    requestDTO.getPassword(),
                    role
            );

            UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(responseDTO, "User created successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(
                            "Invalid role. Must be: ADMIN, STUDENT or TEACHER. Received: " + requestDTO.getRole(),
                            null
                    ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDTO requestDTO) {
        User user = updateUserProfileUseCase.execute(
                id,
                requestDTO.getEmail(),
                requestDTO.getPassword()
        );
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

    // @PutMapping("/{id}/role")
    // public ResponseEntity<ApiResponse<UserResponseDTO>> changeRole(
    //         @PathVariable Long id,
    //         @RequestParam String roleStr) {  // String вместо Role
    //     try {
    //         Role role = Role.valueOf(roleStr.toUpperCase());
    //         User user = changeUserRoleUseCase.execute(id, role);
    //         UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
    //         return ResponseEntity.ok(ApiResponse.success(responseDTO, "Role changed successfully"));
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest()
    //                 .body(ApiResponse.error("Invalid role: " + roleStr, null));
    //     }
    // }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(
            @Valid @RequestBody CreateUserRequestDTO requestDTO) {

        try {
            Role role = requestDTO.getRoleAsEnum();

            if (role == Role.ADMIN) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Cannot register as ADMIN", null));
            }

            User user = createUserUseCase.execute(
                    requestDTO.getEmail(),
                    requestDTO.getPassword(),
                    role
            );
            UserResponseDTO responseDTO = UserResponseDTO.fromEntity(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(responseDTO, "User registered successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(
                            "Invalid role. Must be: STUDENT or TEACHER. Received: " + requestDTO.getRole(),
                            null
                    ));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        }
    }

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<String>> testEndpoint(@RequestBody Map<String, String> request) {
        System.out.println("=== TEST ENDPOINT ===");
        System.out.println("Request received: " + request);
        return ResponseEntity.ok(ApiResponse.success("Test endpoint working", null));
    }
}
