create table user
(
    user_id       int auto_increment
        primary key,
    user_name     varchar(255) not null,
    user_password varchar(255) not null,
    starred_task  varchar(255) null comment '星标代办（逻辑外键或否）'
);

