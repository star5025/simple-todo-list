create table task
(
    task_id      int auto_increment
        primary key,
    task_name    varchar(255) not null,
    status       varchar(255) null comment '任务状态（未完成，已完成）',
    created_time varchar(255) null,
    due_time     varchar(255) null,
    remind_time  varchar(255) null,
    user_id      varchar(255) null comment '拥有该任务的用户Id'
);

