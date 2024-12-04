create database if not exists db_forum;
use db_forum;

#实体，记录文件存放的路径
create table if not exists resource
(
    owner int not null, #The owner can be the id of post's picture or id of user's/forum's avatar.
    url varchar(128) not null
);

#实体，包含用户基本信息
CREATE TABLE IF NOT EXISTS user_info
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    userName    VARCHAR(32) NOT NULL,
    email       VARCHAR(32) NOT NULL UNIQUE,
    phoneNum    INT,
    hc_password BIGINT      NOT NULL,
    gender      CHAR(4),
    age         INT,
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
    index (id_fan),index (id)
);

#实体，包含帖子/评论的基本信息
create table if not exists post
(
    id         int primary key          not null auto_increment,
    authorId   int                      not null references user_info (id),
    isRoot     boolean     default true not null,
    title      varchar(64) default null,
    date       datetime                 not null,
    likeNum    bigint      default 0,
    commentNum bigint      default 0,
    content    text,
    index (date desc),
    index (likeNum desc)
);
#关系，记录哪些帖子是该帖子的评论
create table if not exists subPost
(
    id         int not null references post (id),
    id_subPost int not null references post (id),
    index (id),index (id_subPost)
);
#用以个性化推荐的标签
create table if not exists tag
(
    id       int         not null primary key auto_increment,
    #近义词的id
    synonym  int references tag (id),
    tag_name varchar(32) not null unique,
    index (tag_name)
);

#用户感兴趣的标签
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
create table if not exists user_collection(
    user int not null references user_info(id),
    post int not null references post(id),
    index (user)
);
#帖子点赞过的用户
create table if not exists user_likedPost(
    user int not null references user_info(id),
    post int not null references post(id),
    index (post)
);
#论坛的基本信息
create table if not exists forum(
    id int not null primary key auto_increment,
    name varchar(16) not null,
    introduction TEXT ,
    owner int references user_info(id)
);
#论坛内包含了哪些帖子
create table if not exists forum_post(
    forum int not null references forum(id),
    post int not null references post(id),
    index (forum)
);
#该论坛有哪些人关注了
create table if not exists forum_member(
    forum int not null references forum(id),
    user int not null references user_info(id),
    index (forum), index (user)
);




#VIEWS

SELECT id, COUNT(id_fan) AS fan_count
FROM ref_self_fan
GROUP BY id
ORDER BY fan_count DESC;


create view mostPopular_users as
SELECT user_info.*, COUNT(id_fan) AS fan_number
FROM ref_self_fan
         LEFT JOIN user_info on user_info.id = ref_self_fan.id
GROUP BY id
ORDER BY fan_number DESC;

create view Latest_posts as
select *
from post
order by date;

create view mostLiked_posts as
select *
from post
order by likeNum desc;

#PROCEDURES
create procedure getPosts(in idx int, in offset int)
begin
    select * from latest_posts limit idx, offset;
end;

#calculate the number of fans.
create procedure calculateFanNum(in userId int, out fanNum int)
begin
    select count(*) into fanNum from ref_self_fan where id = userId;
end;

#Get the popular post within a month.
create procedure getRecentPopularPost(in idx int, in offset int)
begin
    select * from latest_posts where date > now() - interval 30 day limit idx, offset;
end;
