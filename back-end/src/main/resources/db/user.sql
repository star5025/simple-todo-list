create table user
(
    user_id       int auto_increment
        primary key,
    user_name     varchar(255) not null,
    user_password varchar(255) not null,
    constraint user_name
        unique (user_name)
);

INSERT INTO simple_todo_list.user (user_id, user_name, user_password) VALUES (1, 'admin', '123456');
