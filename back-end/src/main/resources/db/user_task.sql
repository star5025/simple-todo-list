create table user_task
(
    user_id     int not null,
    task_id     int not null,
    star_status varchar(255) null comment '星标状态',
    primary key (user_id, task_id)
)
    comment '中间表';

