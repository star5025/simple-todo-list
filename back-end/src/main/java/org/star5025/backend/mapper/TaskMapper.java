package org.star5025.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.star5025.backend.entity.Task;
import java.util.List;

@Mapper
public interface TaskMapper {

    /**
     * 查询所有任务，测试用
     * @return
     */
    @Select("select * from task")
    List<Task> getAll();

    /**
     * 新增任务
     * @param task
     */
    @Insert("insert into task (task_name, status, created_time, start_time, remind_time, due_time, user_id) values (#{taskName},#{status},#{createdTime},#{startTime},#{remindTime},#{dueTime},#{userId})")
    void createTask(Task task);
}
