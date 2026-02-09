package org.example.enterprisecasemanagementsystem.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.infrastructure.web.ApiResponse;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateStudentRequestDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.StudentResponseDTO;
import org.example.enterprisecasemanagementsystem.application.student.*;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final UpdateStudentGroupUseCase updateStudentGroupUseCase;
    private final GetStudentByIdUseCase getStudentByIdUseCase;
    private final ListStudentsUseCase listStudentsUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody CreateStudentRequestDTO requestDTO) {
        Student student = createStudentUseCase.execute(
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getGroupName(),
                requestDTO.getUserId()  // Теперь передается Long, а не User
        );
        StudentResponseDTO responseDTO = StudentResponseDTO.fromEntity(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDTO, "Student created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudent(@PathVariable Long id) {
        Student student = getStudentByIdUseCase.execute(id);
        StudentResponseDTO responseDTO = StudentResponseDTO.fromEntity(student);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<Student> students = listStudentsUseCase.execute();
        List<StudentResponseDTO> responseDTOs = students.stream()
                .map(StudentResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDTOs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        deleteStudentUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Student deleted successfully"));
    }
}
