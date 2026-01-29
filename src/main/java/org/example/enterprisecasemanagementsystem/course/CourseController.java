package org.example.enterprisecasemanagementsystem.course;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enterprisecasemanagementsystem.*;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CreateCourseUseCase createCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final GetCourseUseCase getCourseUseCase;
    private final ListCourseUseCase listCourseUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;
    private final EnrollStudentUseCase enrollStudentUseCase;
    private final UnenrollStudentUseCase unenrollStudentUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(
            @Valid @RequestBody CreateCourseRequestDTO requestDTO) {
        Course course = createCourseUseCase.execute(
                requestDTO.getTitle(),
                requestDTO.getDescription(),
                requestDTO.getTeacherId(),
                requestDTO.getMaxStudents()
        );
        CourseResponseDTO responseDTO = convertToDTO(course);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDTO, "Course created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseRequestDTO requestDTO) {
        Course course = updateCourseUseCase.execute(
                id,
                requestDTO.getTitle(),
                requestDTO.getDescription()
        );
        CourseResponseDTO responseDTO = convertToDTO(course);
        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Course updated successfully"));
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> enrollStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        Course course = enrollStudentUseCase.execute(courseId, studentId);
        CourseResponseDTO responseDTO = convertToDTO(course);
        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Student enrolled successfully"));
    }

    @DeleteMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> unenrollStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        Course course = unenrollStudentUseCase.execute(courseId, studentId);
        CourseResponseDTO responseDTO = convertToDTO(course);
        return ResponseEntity.ok(ApiResponse.success(responseDTO, "Student unenrolled successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourse(@PathVariable Long id) {
        Course course = getCourseUseCase.execute(id);
        CourseResponseDTO responseDTO = convertToDTO(course);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {
        List<Course> courses = listCourseUseCase.execute();
        List<CourseResponseDTO> responseDTOs = courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDTOs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        deleteCourseUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Course deleted successfully"));
    }

    private CourseResponseDTO convertToDTO(Course course) {
        TeacherResponseDTO teacherDTO = TeacherResponseDTO.fromEntity(course.getTeacher());

        Set<Long> enrolledStudentIds = course.getEnrolledStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet());

        return new CourseResponseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCreatedAt(),
                course.getMaxStudents(),
                teacherDTO,
                enrolledStudentIds
        );
    }
}
