create database if not exists db_forum;
use db_forum;

#实体，包含用户基本信息
CREATE TABLE IF NOT EXISTS user_info
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    userName    VARCHAR(32) NOT NULL,
    email       VARCHAR(32) NOT NULL UNIQUE,
    phoneNum    INT,
    hc_password BIGINT      NOT NULL,
    gender      boolean,
    age         INT,
    avatar      varchar(50) default 'defaultAvatar.jpg',
    INDEX (userName),
    INDEX (email),
    INDEX user_name_hc_password (userName, hc_password),
    INDEX email_hc_password (email, hc_password)
);

#用户间的关注关系
create table if not exists ref_self_fan
(
    id     int not null references user_info (id),
    id_fan int not null references user_info (id),
    index (id_fan),
    index (id),
    unique index (id, id_fan)
);

#实体，包含帖子/评论的基本信息
create table if not exists post
(
    id         int primary key          not null auto_increment,
    authorId   int                      not null references user_info (id),
    isRoot     boolean     default true not null,
    root       int,
    title      varchar(64) default null,
    date       date                     not null,
    likeNum    bigint      default 0,
    commentNum bigint      default 0,
    content    text,
    forum      int         default null references forum (id),
    index (root),
    index (date desc),
    index (likeNum desc)
);

#方便检索的标签
create table if not exists tag
(
    id       int         not null primary key auto_increment,
    #近义词的id
    synonym  int references tag (id),
    tag_name varchar(32) not null unique,
    index (tag_name)
);

#用户的标签
create table if not exists user_tag
(
    id  int not null references user_info (id),
    tag int not null references tag (id),
    index (id)
);
#帖子的标签
create table if not exists post_tag
(
    id  int not null references post (id),
    tag int not null references tag (id),
    index (id)
);
#该用户收藏的帖子
create table if not exists user_collection
(
    user int not null references user_info (id),
    post int not null references post (id),
    index (user),
    unique index (user, post)
);
#帖子点赞过的用户
create table if not exists user_likedPost
(
    user int not null references user_info (id),
    post int not null references post (id),
    index (post),
    unique index (user, post)
);
#论坛的基本信息
create table if not exists forum
(
    id           int         not null primary key auto_increment,
    name         varchar(16) not null,
    introduction TEXT,
    owner        int references user_info (id),
    postNum      int         default 0,
    userNum      int         default 0,
    icon         varchar(50) default 'defaultIcon.jpg'
);

#该论坛有哪些人关注了
create table if not exists forum_member
(
    forum int not null references forum (id),
    user  int not null references user_info (id),
    index (forum),
    index (user)
);


#VIEWS


create view mostPopular_users as
SELECT user_info.*, COUNT(id_fan) AS fan_number
FROM ref_self_fan
         LEFT JOIN user_info on user_info.id = ref_self_fan.id
GROUP BY id
ORDER BY fan_number DESC;

create view latest_posts as
select *
from post
where isRoot = true
order by date desc, id desc;

create view mostLiked_posts as
select *
from post
where isRoot = true
order by likeNum desc;

create view mostLiked_post_week as
select *
from post
where date > now() - interval 7 day
  and isRoot = true
order by likeNum desc;

create view mostLiked_post_month as
select *
from post
where date > now() - interval 1 month
  and isRoot = true
order by likeNum desc;

