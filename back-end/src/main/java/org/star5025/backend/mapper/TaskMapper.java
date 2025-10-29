package org.star5025.backend.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.star5025.backend.entity.Task;
import org.star5025.backend.vo.TaskVO;

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
    @Insert("insert into task (task_name, status, created_time, start_time, remind_time, due_time, user_id)" +
            "values (#{taskName},#{status},#{createdTime},#{startTime},#{remindTime},#{dueTime},#{userId})")
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
     * @return
     */
    @Select("select task_id, task_name, due_time, status from task where user_id = #{userId}")
    Page<Task> pageQuery(Long userId);
}
