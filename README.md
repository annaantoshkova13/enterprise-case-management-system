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

### Base URL
- **Local Development:** `http://localhost:8080/api`
- **Production (example):** `https://your-domain.com/api`

### Example Endpoints

| Resource   | Endpoint                     | Method | Description                        |
|-----------|------------------------------|--------|------------------------------------|
| Users     | `/users`                     | GET    | List all users                     |
| Users     | `/users/{id}`                | GET    | Get user by ID                     |
| Users     | `/users`                     | POST   | Create a new user                  |
| Courses   | `/courses`                   | GET    | List all courses                   |
| Courses   | `/courses/{id}`              | GET    | Get course by ID                   |
| Courses   | `/courses`                   | POST   | Create a course (Admin/Teacher)   |
| Students  | `/students/{id}/enroll`      | POST   | Enroll in a course                 |
| Students  | `/students/{id}/unenroll`    | POST   | Unenroll from a course             |

> All endpoints follow REST principles and enforce **role-based access control**.

---

## 🔒 Security
- Passwords are stored securely using **BCrypt**
- **Role-based access control (RBAC)** ensures proper authorization
- Input validation and global exception handling prevent invalid requests

---

## 🧪 Testing
- **Unit Tests:** Service and domain logic
- **Integration Tests:** Repository and API endpoints
- **Test Coverage:** Ensures correctness of business rules and validations
