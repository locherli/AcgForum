CREATE DATABASE IF NOT EXISTS db_forum;
USE db_forum;

ALTER DATABASE db_forum CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Table for user information
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
ORDER BY likeNum DESC;