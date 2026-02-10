# Enterprise Case Management System

**Enterprise Case Management System** is a portfolio-grade backend application built with **Java and Spring Boot**.  
It demonstrates how I design **clean, scalable, and maintainable enterprise systems** using **Clean Architecture** and **Domain-Driven Design (DDD)**.

A production-style backend application with real-world business logic, role-based access control, and comprehensive testing.

---

## ✨ Key Features

### Core Functionality
- User management with roles: **ADMIN, STUDENT, TEACHER**  
- Course creation and management with **capacity limits**  
- Student enrollment and unenrollment logic  
- Teacher and student profile management  
- Role-based access control (**RBAC**)  
- Centralized validation and global error handling  

### Technical Highlights
- **Clean Architecture** with strict separation of concerns  
- **Use-case–driven** business logic  
- **RESTful API** with proper HTTP semantics  
- **Secure password storage** using BCrypt  
- Database schema versioning with **Flyway**  
- Comprehensive **unit and integration testing**  

---

## 🏗 Architecture Overview

The application follows a layered architecture:

- **Controller Layer** – handles HTTP requests and responses  
- **Service Layer** – contains business logic (use-cases)  
- **Repository Layer** – database access using JPA/Hibernate  
- **Domain Layer** – core entities, value objects, and domain rules  

This architecture ensures:

- Clear separation of responsibilities  
- High testability of business logic  
- Independence from frameworks  
- Easy extensibility and maintenance  

---

## 🛠 Technology Stack

- Java 17+  
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
- **One-to-One:** `User ↔ Student / Teacher`  
- **Many-to-One:** `Course → Teacher`  
- **Many-to-Many:** `Course ↔ Student`  

> All relationships are validated through domain logic.

---

## 🔌 API Overview

### Base URL
- **Local Development:** `http://localhost:8080/api`  
- **Production (example):** `https://your-domain.com/api`  

### Example Endpoints

| Resource   | Endpoint                  | Method | Description                        |
|-----------|---------------------------|--------|------------------------------------|
| Users     | `/users`                  | GET    | List all users                     |
| Users     | `/users/{id}`             | GET    | Get user by ID                     |
| Users     | `/users`                  | POST   | Create a new user                  |
| Courses   | `/courses`                | GET    | List all courses                   |
| Courses   | `/courses/{id}`           | GET    | Get course by ID                   |
| Courses   | `/courses`                | POST   | Create a course (Admin/Teacher)   |
| Students  | `/students/{id}/enroll`   | POST   | Enroll in a course                 |
| Students  | `/students/{id}/unenroll` | POST   | Unenroll from a course             |

---

## 🔒 Security

- Passwords are stored securely using BCrypt  
- Role-based access control (RBAC) ensures proper authorization  
- Input validation and global exception handling prevent invalid requests  

---


## 🧪 Testing

- Unit Tests: Service and domain logic  
- Integration Tests: Repository and API endpoints  
- Test Coverage: Ensures correctness of business rules and validations  

---

🚀 Getting Started

# Clone the repository
git clone https://github.com/annaantoshkova13/enterprise-case-management-system.git

# Go into the project directory
cd enterprise-case-management-system

# Build the project
mvn clean install

# Run the project locally
mvn spring-boot:run

# Access API
http://localhost:8080/api

---

## 🐳 Docker Support

### Using Docker
```bash
# Build Docker image
docker build -t enterprise-case-management-system .

# Run container
docker run -p 8080:8080 --name ecms-container enterprise-case-management-system
 version: '3.8'

services:
  db:
    image: postgres:15
    container_name: ecms-postgres
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: 12345
    ports:
      - "5432:5432"
    volumes:
      - db_data:/var/lib/postgresql/data

  app:
    image: enterprise-case-management-system
    container_name: ecms-app
    depends_on:
      - db
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/mydb
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: 12345

volumes:
  db_data:



