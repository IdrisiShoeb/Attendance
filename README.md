
# College Attendance Management System
This is a web-based College Attendance Management System built using Spring Boot and Spring Security. The system allows Admin users to mark attendance for Student users, while students can view their attendance history and track their attendance percentage for a specific month. The system is designed with role-based access control, where the Admin has higher privileges to manage attendance, and Students can only view their attendance details.

## Features

### Admin Features:
- Mark attendance for students for each lecture.
- View attendance records of all students.
- Track attendance statistics for all students.
- Generate monthly attendance reports.
### Student Features:
- View attendance percentage for a specific month.
- View historical attendance records.
- Dashboard showing student-specific data (attendance percentage, history, etc.).


## Tech Stack

**Backend:** 
- ***Spring Boot*** for REST API and business logic.
- ***Spring Security*** for role-based authentication and authorization.
- ***Spring Data JPA*** for database interaction.
- ***Thymeleaf*** for rendering views (if needed).
- ***MySQL*** (for production) for storing user and attendance data.

**Frontend:** 
- ***HTML/CSS*** for basic page rendering.
- ***JavaScript*** for dynamic content and interactivity.

**Security:** 
- ***Spring Security*** for securing endpoints and implementing role-based access.
## Application Configuration

Update the application.properties file to configure your database connection.

```bash
    spring.application.name=full
    spring.datasource.url=jdbc:mysql://localhost:3306/college
    spring.datasource.username=root
    spring.datasource.password=root
    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```


## Testing

**1.** Import the data provided in this repository to your database (use MySql workbench) .

**2.** Run the project in IntelliJ Idea .


## License

This project is licensed under the MIT License - see the [LICENSE](https://choosealicense.com/licenses/mit/) file for details.[MIT

