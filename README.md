# AcgForum - ACG (Anime, Comics, Games) Forum Application

AcgForum is a three-tier web application built for ACG content discussion, featuring a Spring Boot backend, MySQL database, and Vue.js frontend [1](#0-0) .

## Technology Stack

- **Backend**: Spring Boot 3.4.0 with Java 17 [2](#0-1) 
- **Database**: MySQL with UTF-8MB4 character set [3](#0-2) 
- **ORM**: MyBatis Spring Boot Starter 3.0.4 [4](#0-3) 
- **Authentication**: Sa-Token 1.44.0 [5](#0-4) 
- **Frontend**: Vue.js (located in `ui/` directory)

## QUICK START

### Prerequisites

- Java 17 or higher
- Maven 3.8.8 or higher
- MySQL 5.7 or higher
- Node.js (for frontend)

### 1. Database Setup

Execute the SQL script to create the database and tables:

```bash
mysql -u root -p < AcgForum.sql
```

This creates:

- Database `db_forum` with UTF-8MB4 encoding [3](#0-2) 
- Tables: `user_info`, `forum`, `post`, `tag`, and relationship tables [6](#0-5) 
- Pre-defined views for popular posts and users [7](#0-6) 

### 2. Backend Deployment

Navigate to the Backend directory and build:

```bash
cd Backend
mvn clean package
```

Run the application:

```bash
java -jar target/acgforum-0.0.1.jar
```

The backend will start on the default port (8080) with embedded Tomcat [8](#0-7) .

### 3. Frontend Deployment

copy all the files and directeries into server and alter nginx config.

### 4. API Documentation

Once the backend is running, access the interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```

This is powered by Springdoc OpenAPI 2.1.0 [9](#0-8) .

## Key Features

- **User Management**: Registration, login, profile management with avatar upload
- **Forum System**: Create and manage forums with categories
- **Post System**: Hierarchical posts and comments with rich content support
- **Social Features**: Follow users, like posts, bookmark content
- **Tagging System**: Organize content with tags
- **File Upload**: UUID-based file storage for avatars and images

## Project Structure

```
AcgForum/
├── Backend/                 # Spring Boot application
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── pom.xml             # Maven configuration
│   └── uploads/            # File upload directory
├── ui/                     # Vue.js frontend
└── AcgForum.sql           # Database schema
```

## Notes

- The application uses Lombok for code generation [10](#0-9) 
- Database connection supports both local and remote MySQL instances
- File uploads are stored with UUID filenames to prevent collisions
- The project includes comprehensive test coverage with JUnit 5 [11](#0-10) 

Wiki pages you might want to explore:

- [Overview (locherli/AcgForum)](/wiki/locherli/AcgForum#1)
