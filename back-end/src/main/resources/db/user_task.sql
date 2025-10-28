create table user_task
(
    user_id     varchar(255) not null,
    task_id     varchar(255) not null,
    star_status tinyint(1)   null comment '星标状态',
    primary key (user_id, task_id),
    foreign key (user_id) references user(user_id) on delete cascade,
    foreign key (task_id) references task(task_id) on delete cascade
)
    comment '中间表';

