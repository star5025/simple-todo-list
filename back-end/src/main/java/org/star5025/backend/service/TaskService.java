package org.star5025.backend.service;

import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.dto.TaskPageQueryDTO;
import org.star5025.backend.dto.TaskPatchDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.result.PageResult;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    /**
     * 查询所有任务，测试用
     * @return
     */
    List<Task> getAllTaskTest();

    /**
     * 新增任务
     * @param taskDTO
     */
    void createTask(TaskDTO taskDTO);

    /**
     * 删除单个任务
     * @param taskId
     */
    void deleteTask(Long taskId);

    /**
     * 批量删除任务
     * @param taskIds
     */
    void deleteTasks(List<Long> taskIds);

    /**
     * 根据用户Id进行分页查询
     * @param taskPageQueryDTO
     * @return
     */
    PageResult pageQuery(TaskPageQueryDTO taskPageQueryDTO);
    
    /**
     * 根据用户Id进行分页查询
     * @param page 页码
     * @param pageSize 每页大小
     * @param userId 用户ID
     * @param status 完成状态
     * @param dueTime 截止时间
     * @param orderBy 排序字段
     * @param favourite 收藏状态
     * @return
     */
    PageResult pageQuery(int page, int pageSize, Long userId, Boolean status, LocalDateTime dueTime, String orderBy, Boolean favourite);

    /**
     * 更新任务
     * @param taskId
     * @param taskPatchDTO
     */
    void updateTask(Long taskId, TaskPatchDTO taskPatchDTO);

    /**
     *根据任务Id查询任务
     * @param taskId
     * @return
     */
    Task getTaskById(Long taskId);
}