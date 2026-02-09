package org.example.enterprisecasemanagementsystem.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.infrastructure.web.ApiResponse;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateTeacherRequestDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.TeacherResponseDTO;
import org.example.enterprisecasemanagementsystem.application.teacher.*;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final CreateTeacherUseCase createTeacherUseCase;
    private final UpdateTeacherDepartmentUseCase updateTeacherDepartmentUseCase;
    private final GetTeacherByIdUseCase getTeacherByIdUseCase;
    private final ListTeachersUseCase listTeachersUseCase;
    private final DeleteTeacherUseCase deleteTeacherUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> createTeacher(
            @Valid @RequestBody CreateTeacherRequestDTO requestDTO) {
        Teacher teacher = createTeacherUseCase.execute(
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getDepartment(),
                requestDTO.getUserId()  // Теперь передается Long, а не User
        );
        TeacherResponseDTO responseDTO = TeacherResponseDTO.fromEntity(teacher);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDTO, "Teacher created successfully"));
    }

//    @PutMapping("/{id}/department")
//    public ResponseEntity<ApiResponse<TeacherResponseDTO>> updateDepartment(
//            @PathVariable Long id,
//            @RequestParam String department) {
//        Teacher teacher = updateTeacherDepartmentUseCase.execute(id, department);
//        TeacherResponseDTO responseDTO = TeacherResponseDTO.fromEntity(teacher);
//        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Department updated successfully"));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponseDTO>> getTeacher(@PathVariable Long id) {
        Teacher teacher = getTeacherByIdUseCase.execute(id);
        TeacherResponseDTO responseDTO = TeacherResponseDTO.fromEntity(teacher);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponseDTO>>> getAllTeachers() {
        List<Teacher> teachers = listTeachersUseCase.execute();
        List<TeacherResponseDTO> responseDTOs = teachers.stream()
                .map(TeacherResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDTOs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id) {
        deleteTeacherUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Teacher deleted successfully"));
    }
}
