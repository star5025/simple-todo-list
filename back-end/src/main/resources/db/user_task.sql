create table user_task
(
    user_id     varchar(255) not null,
    task_id     varchar(255) not null,
    star_status tinyint(1)   null comment '星标状态',
    primary key (user_id, task_id)
)
    comment '中间表';

