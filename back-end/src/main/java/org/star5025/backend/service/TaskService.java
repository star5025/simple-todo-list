package org.star5025.backend.service;

import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.dto.TaskPageQueryDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.result.PageResult;

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
     * 根据用户Id进行分页查询
     * @param taskPageQueryDTO
     * @return
     */
    PageResult pageQuery(TaskPageQueryDTO taskPageQueryDTO);
}
