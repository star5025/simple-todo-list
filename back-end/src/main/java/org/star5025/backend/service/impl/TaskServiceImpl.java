package org.star5025.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.mapper.TaskMapper;
import org.star5025.backend.service.TaskService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 查询所有任务，测试用
     * @return
     */
    @Override
    public List<Task> getAllTaskTest() {
        return taskMapper.getAll();
    }

    /**
     * 新增任务
     * @param taskDTO
     */
    @Override
    public void createTask(TaskDTO taskDTO) {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        taskMapper.createTask(task);
    }

    /**
     * 删除单个任务
     * @param taskId
     */
    @Override
    public void deleteTask(Long taskId) {
        taskMapper.deleteTask(taskId);
    }
}
