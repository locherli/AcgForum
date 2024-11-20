create database if not exists db_forum;
use db_forum;
create table if not exists userInfo
(
    id          int primary key auto_increment,
    userName    varchar(32) not null,
    email       varchar(32) not null unique,
    phoneNum    int,
    hc_password bigint      not null,
    gender      char(4),
    age         int,
    avatar      char(64),
    index (userName),
    index (email),
    index userInfo (userName, hc_password),
    index userInfo (email, hc_password)
);

create table if not exists ref_self_fans
(
    id     int not null references userinfo (id),
    id_fan int not null references userinfo (id),
    index (id_fan)
);

create table if not exists post
(
    id         int primary key          not null auto_increment,
    authorId   int                      not null references userinfo (id),
    isRoot     boolean     default true not null,
    title      varchar(64) default null,
    date       datetime                 not null,
    likeNum    bigint      default 0,
    commentNum bigint      default 0,
    content    text,
    #     fileLink   varchar(128),
    index (date desc),
    index (likeNum desc)
);

create table if not exists subPost
(
    id         int not null references post (id),
    id_subPost int not null references post (id)
);

#VIEWS

SELECT id, COUNT(id_fan) AS fan_count
FROM ref_self_fans
GROUP BY id
ORDER BY fan_count DESC;


create view mostPopular_users as
SELECT userInfo.*, COUNT(id_fan) AS fan_number
FROM ref_self_fans
         LEFT JOIN userInfo on userInfo.id = ref_self_fans.id
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
    select count(*) into fanNum from ref_self_fans where id = userId;
end;

#Get the popular post within a month.
create procedure getRecentPopularPost(in idx int, in offset int)
begin
    select * from latest_posts where date > now() - interval 30 day limit idx, offset;
end;
