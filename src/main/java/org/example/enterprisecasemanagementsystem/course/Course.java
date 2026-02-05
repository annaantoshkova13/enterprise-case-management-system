package org.example.enterprisecasemanagementsystem.course;

import jakarta.persistence.*;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.example.enterprisecasemanagementsystem.student.Student;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer maxStudents = 30;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> enrolledStudents = new HashSet<>();

    public Course() {
    }

    public Course(String title, String description, Teacher teacher) {
        this(title, description, teacher, 30);
    }

    public Course(String title, String description, Teacher teacher, Integer maxStudents) {
        this.title = title;
        this.description = description;
        this.teacher = teacher;
        this.maxStudents = maxStudents != null ? maxStudents : 30;
        this.createdAt = LocalDateTime.now();
        validateMaxStudents();
    }

    public void update(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.title = title;
        this.description = description;
    }

    public void enrollStudent(Student student) {
        if (enrolledStudents.size() >= maxStudents) {
            throw new BusinessException("Course is full. Maximum students: " + maxStudents);
        }
        if (enrolledStudents.contains(student)) {
            throw new BusinessException("Student is already enrolled in this course");
        }
        enrolledStudents.add(student);
    }

    public void unenrollStudent(Student student) {
        enrolledStudents.remove(student);
    }

    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void validateMaxStudents() {
        if (maxStudents != null && maxStudents < 1) {
            throw new BusinessException("Max students must be at least 1");
        }
    }

    public void setMaxStudents(Integer maxStudents) {
        if (maxStudents != null && maxStudents < getCurrentEnrollment()) {
            throw new BusinessException(
                    "Cannot set max students to " + maxStudents +
                            " when there are already " + getCurrentEnrollment() + " students enrolled"
            );
        }
        this.maxStudents = maxStudents != null ? maxStudents : 30;
    }

    public Set<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Set<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }

    public boolean hasAvailableSlots() {
        return enrolledStudents.size() < maxStudents;
    }
}
