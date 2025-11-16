package org.star5025.backend.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.star5025.backend.entity.Task;

import java.time.LocalDateTime;
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
    @Insert("insert into task (task_name, status, description, created_time, start_time, remind_time, due_time, user_id, favourite)" +
            "values (#{taskName},#{status},#{description},now(),#{startTime},#{remindTime},#{dueTime},#{userId},#{favourite})")
    void createTask(Task task);

    /**
     * 删除task表中的单个任务
     * @param taskId
     */
    @Delete("delete from task where task_id = #{taskId}")
    void deleteTask(Long taskId);

    /**
     * 批量删除任务
     * @param taskIds
     */
    @Delete("<script>" +
            "DELETE FROM task WHERE task_id IN " +
            "<foreach item='id' collection='taskIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteTasks(@Param("taskIds") List<Long> taskIds);

    /**
     * 根据用户Id分页查询任务
     * @param userId
     * @param status 完成状态，如果为null则不筛选
     * @param dueTime 截止时间，如果为null则不筛选
     * @param orderBy 排序字段
     */
    @Select("<script>" +
            "select task_id as taskId, task_name as taskName, description as description, due_time as dueTime, status, start_time as startTime, remind_time as remindTime, created_time as createdTime, favourite as favourite " +
            "from task " +
            "where user_id = #{userId} " +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='dueTime != null'> AND DATE(due_time) = DATE(#{dueTime}) </if>" +
            "order by " +
            "<choose>" +
            "  <when test='orderBy == \"dueTime\"'>due_time asc</when>" +
            "  <when test='orderBy == \"dueTimeDesc\"'>due_time desc</when>" +
            "  <when test='orderBy == \"startTime\"'>start_time asc</when>" +
            "  <when test='orderBy == \"startTimeDesc\"'>start_time desc</when>" +
            "  <when test='orderBy == \"createdTime\"'>created_time asc</when>" +
            "  <when test='orderBy == \"createdTimeDesc\"'>created_time desc</when>" +
            "  <otherwise>created_time desc, task_id desc</otherwise>" +
            "</choose>" +
            "</script>")
    Page<Task> pageQuery(@Param("userId") Long userId, @Param("status") Boolean status, @Param("dueTime") LocalDateTime dueTime, @Param("orderBy") String orderBy);
    
    /**
     * 根据用户Id分页查询任务（支持收藏筛选）
     * @param userId
     * @param status 完成状态，如果为null则不筛选
     * @param dueTime 截止时间，如果为null则不筛选
     * @param orderBy 排序字段
     * @param favourite 收藏状态，如果为null则不筛选
     */
    @Select("<script>" +
            "select task_id as taskId, task_name as taskName, description as description, due_time as dueTime, status, start_time as startTime, remind_time as remindTime, created_time as createdTime, favourite as favourite " +
            "from task " +
            "where user_id = #{userId} " +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='dueTime != null'> AND DATE(due_time) = DATE(#{dueTime}) </if>" +
            "<if test='favourite != null'> AND favourite = <choose><when test='favourite'>1</when><otherwise>0</otherwise></choose> </if>" +
            "order by " +
            "<choose>" +
            "  <when test='orderBy == \"dueTime\"'>due_time asc</when>" +
            "  <when test='orderBy == \"dueTimeDesc\"'>due_time desc</when>" +
            "  <when test='orderBy == \"startTime\"'>start_time asc</when>" +
            "  <when test='orderBy == \"startTimeDesc\"'>start_time desc</when>" +
            "  <when test='orderBy == \"createdTime\"'>created_time asc</when>" +
            "  <when test='orderBy == \"createdTimeDesc\"'>created_time desc</when>" +
            "  <otherwise>created_time desc, task_id desc</otherwise>" +
            "</choose>" +
            "</script>")
    Page<Task> pageQueryWithFavourite(@Param("userId") Long userId, @Param("status") Boolean status, @Param("dueTime") LocalDateTime dueTime, @Param("orderBy") String orderBy, @Param("favourite") Boolean favourite);

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
    @Update("update task set task_name = #{taskName}, status = #{status}, description = #{description}, start_time = #{startTime}, remind_time = #{remindTime}, due_time = #{dueTime}, favourite = #{favourite} WHERE task_id = #{taskId}")
    void updateTask(Task newTask);
}