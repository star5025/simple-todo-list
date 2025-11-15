package org.star5025.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.star5025.backend.context.BaseContext;
import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.dto.TaskPageQueryDTO;
import org.star5025.backend.dto.TaskPatchDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.result.PageResult;
import org.star5025.backend.result.Result;
import org.star5025.backend.service.TaskService;
import org.star5025.backend.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Api(tags = "任务相关接口")
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;

//    @ApiOperation("全部任务查询接口")
//    @GetMapping("/all")
//    public List<Task> getAllTaskTest() {
//        return taskService.getAllTaskTest();
//    }

    /**
     * 新增任务
     * @param taskDTO
     */
    @ApiOperation("新增任务接口")
    @PostMapping
    public Result createTask(@RequestBody TaskDTO taskDTO) {
        // 从线程上下文中获取当前用户ID，而不是依赖前端传递
        Long userId = BaseContext.getCurrentId();

        log.info("新增任务：当前用户id：{} 任务名：{}", userId, taskDTO.getTaskName());

        taskDTO.setUserId(userId); // 设置当前用户ID

        taskService.createTask(taskDTO);

        return Result.success();
    }

    /**
     * 批量删除任务
     * @param taskIds 任务ID列表
     */
    @ApiOperation("批量删除任务接口")
    @DeleteMapping("/batch/delete")
    public Result deleteTasks(@RequestBody List<Long> taskIds) {
        log.info("批量删除任务，任务Ids: {}", taskIds);
        taskService.deleteTasks(taskIds);
        return Result.success();
    }

    /**
     * 删除单个任务
     * @param taskId
     */
    @ApiOperation("删除单个任务接口")
    @DeleteMapping("/{taskId}")
    public Result deleteTask(@PathVariable Long taskId) {
        log.info("删除任务Id为{}的任务", taskId);
        taskService.deleteTask(taskId);
        // 不再减少用户任务计数，因为创建过的待办总数量是只增不减的
        return Result.success();
    }

    /**
     * 更新任务
     * @param taskId
     * @param taskPatchDTO
     */
    @ApiOperation("更新任务接口")
    @PatchMapping("/{taskId}")
    public Result updateTask(@PathVariable Long taskId, @RequestBody TaskPatchDTO taskPatchDTO) {
        log.info("更新任务Id为{}的任务", taskId);

        taskService.updateTask(taskId, taskPatchDTO);

        return Result.success();
    }


    /**
     * 根据用户Id进行分页查询
     * @param page 页码
     * @param pageSize 每页大小
     * @param status 完成状态
     * @param dueTime 截止时间
     * @param orderBy 排序字段
     * @return
     */
    @ApiOperation("根据用户Id分页查询接口")
    @GetMapping("/pageQuery")
    public Result<PageResult> pageQuery(
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "pageSize", required = false) int pageSize,
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "dueTime", required = false) LocalDateTime dueTime,
            @RequestParam(value = "orderBy", required = false) String orderBy) {
        // 从线程上下文中获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("收到分页查询请求: page={}, pageSize={}, userId={}, status={}, dueTime={}, orderBy={}", page, pageSize, userId, status, dueTime, orderBy);
        log.info("进行分页查询，查询的用户Id为：{}", userId);
        TaskPageQueryDTO taskPageQueryDTO = new TaskPageQueryDTO(page, pageSize, userId, status, dueTime, orderBy);
        PageResult pageResult = taskService.pageQuery(taskPageQueryDTO);
        log.info("分页查询结果: 总数={}, 当前页返回数量={}", pageResult.getTotal(), pageResult.getRecords().size());
        return Result.success(pageResult);
    }

    /**
     *根据任务Id查询任务
     * @param taskId
     * @return
     */
    @GetMapping("/{taskId}")
    @ApiOperation("根据任务Id查询任务")
    public Result<Task> getTaskById(@PathVariable Long taskId) {
        log.info("正在查询Id为{}的任务",taskId);
        Task task = taskService.getTaskById(taskId);
        return Result.success(task);
    }
}