create table user
(
    user_id       int auto_increment
        primary key,
    user_name     varchar(255) not null comment '3-16个非空字符',
    user_password varchar(255) not null comment '至少5个非空字符',
    constraint user_name
        unique (user_name)
);