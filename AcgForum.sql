create database if not exists db_forum;
use db_forum;
create table if not exists userInfo(
    id int primary key auto_increment,
    userName varchar(32) not null,
    email varchar(32) not null unique,
    phoneNum int,
    hc_password bigint not null,
    gender char(4),
    age int,
    avatar char(64)
);

create table if not exists ref_self_fans(
    id int primary key not null,
    id_fan int not null
);

create table if not exists ref_self_subscribe(
    id int primary key not null,
    id_subscribe int not null
);


create table if not exists post(
    id int primary key not null auto_increment,
    date datetime not null ,
    likeNum bigint default 0,
    commentNum bigint default 0,
    fileLink varchar(128),
    content text not null
);

create table if not exists subPosts(
    id int primary key not null ,
    id_subPost int not null ,
    foreign key (id) references post(id)
);