package org.star5025.backend.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
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
    @Insert("insert into task (task_name, status, description, created_time, start_time, remind_time, due_time, user_id)" +
            "values (#{taskName},#{status},#{description},now(),#{startTime},#{remindTime},#{dueTime},#{userId})")
    void createTask(Task task);

    /**
     * 删除task表中的单个任务
     * @param taskId
     */
    @Delete("delete from task where task_id = #{taskId}")
    void deleteTask(Long taskId);

    /**
     * 根据用户Id分页查询任务
     * @param userId
     */
    @Select("select task_id as taskId, task_name as taskName, description as description, due_time as dueTime, status, start_time as startTime, remind_time as remindTime, created_time as createdTime from task where user_id = #{userId} order by created_time desc")
    Page<Task> pageQuery(Long userId);

    /**
     * 根据任务Id查询单个任务
     * @param taskId
     */
    @Select("select * from task where task_id = #{taskId}")
    Task getTaskById(Long taskId);

    /**
     * 更新任务
     * @param newTask
     */
    @Update("update task set task_name = #{taskName}, status = #{status}, description = #{description}, start_time = #{startTime}, remind_time = #{remindTime}, due_time = #{dueTime} WHERE task_id = #{taskId}")
    void updateTask(Task newTask);
}