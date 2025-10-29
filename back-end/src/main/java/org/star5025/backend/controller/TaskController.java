package org.star5025.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.dto.TaskPageQueryDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.result.PageResult;
import org.star5025.backend.result.Result;
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
    @ApiOperation("新增任务接口")
    @PostMapping
    public Result createTask(@RequestBody TaskDTO taskDTO) {
        taskService.createTask(taskDTO);
        return Result.success();
    }

    /**
     * 删除单个任务
     * @param taskId
     */
    @ApiOperation("删除单个任务接口")
    @DeleteMapping("/{taskId}")
    public Result deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return Result.success();
    }


    /**
     * 根据用户Id进行分页查询
     * @param taskPageQueryDTO
     * @return
     */
    @GetMapping("/pageQuery")
    @ApiOperation("根据用户Id分页查询")
    public Result<PageResult> pageQuery(TaskPageQueryDTO taskPageQueryDTO) {
        log.info("进行分页查询，查询的用户Id为：{}",taskPageQueryDTO.getUserId());
        PageResult page = taskService.pageQuery(taskPageQueryDTO);
        return Result.success(page);

    }
}
