create table user
(
    user_id       int auto_increment
        primary key,
    user_name     varchar(255) not null,
    user_password varchar(255) not null,
    constraint user_name
        unique (user_name)
);