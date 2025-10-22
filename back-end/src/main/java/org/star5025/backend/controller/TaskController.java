package org.star5025.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.service.TaskService;

import java.util.List;

@Slf4j
@Api(tags = "任务相关接口")
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation("全部任务查询接口")
    @GetMapping("/all")
    public List<Task> getAllTaskTest() {
        return taskService.getAllTaskTest();
    }

    /**
     * 新增任务
     * @param taskDTO
     */
    @ApiOperation("新增任务")
    @PostMapping
    public String createTask(@RequestBody TaskDTO taskDTO) {
        taskService.createTask(taskDTO);
        return "success";
    }
}
