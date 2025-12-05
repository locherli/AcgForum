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

### Citations

**File:** Backend/pom.xml (L5-16)

```text
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>com.tencent.polaris</groupId>
    <artifactId>Backend</artifactId>
    <version>0.0.1</version>
    <name>acgForum</name>
    <description>acgForum</description>
```

**File:** Backend/pom.xml (L30-32)

```text
    <properties>
        <java.version>17</java.version>
    </properties>
```

**File:** Backend/pom.xml (L42-46)

```text
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>3.0.4</version>
        </dependency>
```

**File:** Backend/pom.xml (L54-58)

```text
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>compile</scope>
```

**File:** Backend/pom.xml (L70-87)

```text
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.12.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.12.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-commons</artifactId>
            <version>1.12.0</version>
            <scope>test</scope>
        </dependency>
```

**File:** Backend/pom.xml (L103-108)

```text
        <!-- Sa-Token 权限认证，在线文档：https://sa-token.cc -->
        <dependency>
            <groupId>cn.dev33</groupId>
            <artifactId>sa-token-spring-boot3-starter</artifactId>
            <version>1.44.0</version>
        </dependency>
```

**File:** Backend/pom.xml (L110-114)

```text
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.1.0</version>
        </dependency>
```

**File:** Backend/pom.xml (L143-150)

```text
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
```

**File:** AcgForum.sql (L1-4)

```sql
CREATE DATABASE IF NOT EXISTS db_forum;
USE db_forum;

ALTER DATABASE db_forum CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**File:** AcgForum.sql (L7-105)

```sql
CREATE TABLE IF NOT EXISTS user_info
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    userName    VARCHAR(32) NOT NULL,
    email       VARCHAR(32) NOT NULL UNIQUE,
    phoneNum    VARCHAR(20),
    hc_password BIGINT NOT NULL,
    gender      BOOLEAN,
    age         INT,
    self_intro  TEXT DEFAULT '暂无简介',
    avatar      VARCHAR(50)
    INDEX (userName),
    INDEX (email),
    INDEX user_name_hc_password (userName, hc_password),
    INDEX email_hc_password (email, hc_password)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS forum
(
    id           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(16) NOT NULL,
    introduction TEXT,
    owner        INT,
    postNum      INT DEFAULT 0,
    userNum      INT DEFAULT 0,
    icon         VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tag
(
    id       INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    synonym  INT,
    tag_name VARCHAR(32) NOT NULL UNIQUE,
    INDEX (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ref_self_fan
(
    id     INT NOT NULL,
    id_fan INT NOT NULL,
    INDEX (id_fan),
    INDEX (id),
    UNIQUE INDEX (id, id_fan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS post
(
    id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    authorId   INT NOT NULL,
    isRoot     BOOLEAN DEFAULT TRUE NOT NULL,
    root       INT,
    title      VARCHAR(64) DEFAULT NULL,
    date       DATETIME NOT NULL,
    likeNum    BIGINT DEFAULT 0,
    commentNum BIGINT DEFAULT 0,
    content    TEXT,
    forum      INT DEFAULT NULL,
    INDEX (root),
    INDEX (date),
    INDEX (likeNum)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_tag
(
    id  INT NOT NULL,
    tag INT NOT NULL,
    INDEX (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS post_tag
(
    id  INT NOT NULL,
    tag INT NOT NULL,
    INDEX (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_collection
(
    user INT NOT NULL,
    post INT NOT NULL,
    INDEX (user),
    UNIQUE INDEX (user, post)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_likedPost
(
    user INT NOT NULL,
    post INT NOT NULL,
    INDEX (post),
    UNIQUE INDEX (user, post)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS forum_member
(
    forum INT NOT NULL,
    user  INT NOT NULL,
    INDEX (forum),
    INDEX (user)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**File:** AcgForum.sql (L107-139)

```sql
-- Views
CREATE VIEW mostPopular_users AS
SELECT user_info.*, COUNT(id_fan) AS fan_number
FROM ref_self_fan
LEFT JOIN user_info ON user_info.id = ref_self_fan.id
GROUP BY user_info.id
ORDER BY fan_number DESC;

CREATE VIEW latest_posts AS
SELECT *
FROM post
WHERE isRoot = TRUE
ORDER BY date DESC, id DESC;

CREATE VIEW mostLiked_posts AS
SELECT *
FROM post
WHERE isRoot = TRUE
ORDER BY likeNum DESC;

CREATE VIEW mostLiked_post_week AS
SELECT *
FROM post
WHERE date > NOW() - INTERVAL 7 DAY
  AND isRoot = TRUE
ORDER BY likeNum DESC;

CREATE VIEW mostLiked_post_month AS
SELECT *
FROM post
WHERE date > NOW() - INTERVAL 1 MONTH
  AND isRoot = TRUE
```
