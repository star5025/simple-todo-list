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

INSERT INTO simple_todo_list.task (task_id, task_name, status, created_time, due_time, remind_time, user_id) VALUES (1, 'test', 0, '2024-06-01 10:00:00', '2024-06-05 23:59:59', '2024-06-04 09:00:00', 1);

