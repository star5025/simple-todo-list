create table task
(
    task_id      int auto_increment
        primary key,
    task_name    varchar(255) not null,
    status       tinyint(1)   not null comment '任务状态（未完成，已完成）',
    created_time datetime     not null,
    start_time   datetime     null,
    due_time     datetime     null,
    remind_time  datetime     null,
    user_id      int          null comment '拥有该任务的用户Id'
);

