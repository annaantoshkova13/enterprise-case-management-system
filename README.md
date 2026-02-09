# Enterprise Case Management System

Enterprise Case Management System is a portfolio-grade backend application built with **Java and Spring Boot**.
The project demonstrates my approach to designing **clean, scalable, and maintainable enterprise systems**
using **Clean Architecture** and **Domain-Driven Design (DDD)**.

This is not a tutorial project — it models real-world business logic, enforces domain rules,
and follows production-style backend practices.

> This project reflects how I design and structure backend applications in real-world scenarios,
> with a strong focus on architecture, domain logic, and testability.

---

## ✨ Key Features

### Core Functionality
- User management with roles: **ADMIN, STUDENT, TEACHER**
- Course creation and management with **capacity limits**
- Student enrollment and unenrollment logic
- Teacher and student profile management
- Role-based access control
- Centralized validation and global error handling

### Technical Highlights
- Clean Architecture with strict separation of concerns
- Use-case–driven business logic
- RESTful API with proper HTTP semantics
- Secure password storage using **BCrypt**
- Database schema versioning with **Flyway**
- Comprehensive testing at multiple levels

---

## 🏗 Architecture Overview

The application follows a layered architecture:


This architecture ensures:
- Clear separation of responsibilities
- High testability of business logic
- Independence from frameworks
- Easy extensibility and maintenance

---

## 🛠 Technology Stack

- Java 25
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security
- PostgreSQL
- Flyway
- Maven
- JUnit & Spring Test

---

## 📚 Domain Model

### Main Entities
- **User** – authentication and role management
- **Student** – student profile and enrollments
- **Teacher** – teacher profile and course ownership
- **Course** – course data, capacity, and enrollments

### Relationships
- One-to-One: User ↔ Student / Teacher
- Many-to-One: Course → Teacher
- Many-to-Many: Course ↔ Student

All relationships are validated through domain logic.

---

## 🔌 API Overview

Base URL:
