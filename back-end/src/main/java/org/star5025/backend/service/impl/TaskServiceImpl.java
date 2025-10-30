package org.star5025.backend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.star5025.backend.dto.TaskDTO;
import org.star5025.backend.dto.TaskPageQueryDTO;
import org.star5025.backend.dto.TaskPatchDTO;
import org.star5025.backend.entity.Task;
import org.star5025.backend.mapper.TaskMapper;
import org.star5025.backend.result.PageResult;
import org.star5025.backend.service.TaskService;
import org.star5025.backend.vo.TaskVO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 根据用户id分页查询任务
     * @param taskPageQueryDTO
     */
    @Override
    public PageResult pageQuery(TaskPageQueryDTO taskPageQueryDTO) {
        PageHelper.startPage(taskPageQueryDTO.getPage(), taskPageQueryDTO.getPageSize());

        Page<Task> taskPage = taskMapper.pageQuery(taskPageQueryDTO.getUserId());

        // 2. 将Task转换为TaskVO，同时计算timeLeft
        List<TaskVO> taskVOList = taskPage.getResult().stream().map(task -> {
            // 2.1 获取数据库中的dueTime
            LocalDateTime dueTime = task.getDueTime();
            // 2.2 获取当前本地时间
            LocalDateTime now = LocalDateTime.now();
            // 2.3 计算天数差（ ChronoUnit.DAYS.between(到期时间, 结束时间) ）
            long daysLeft = ChronoUnit.DAYS.between( now,dueTime);

            if (daysLeft < 0) {
                daysLeft = -1;
                //如果任务已经过期了则写成-1，提示前端这个任务已经到期
            }

            // 2.5 封装到TaskVO
            return TaskVO.builder()
                    .taskId(task.getTaskId())
                    .taskName(task.getTaskName())
                    .timeLeft(String.valueOf(daysLeft)) // 转换为String类型（因为VO中timeLeft是String）
                    .status(task.getStatus())
                    .build();
        }).collect(Collectors.toList());

        // 3. 构建分页结果返回
        return new PageResult(taskPage.getTotal(), taskVOList);
    }

    /**
     * 更新任务
     * @param taskId
     * @param taskPatchDTO
     */
    @Override
    public void updateTask(Long taskId, TaskPatchDTO taskPatchDTO) {
        Task newTask = taskMapper.getTaskById(taskId);

        // 复制非null属性，忽略null属性
        BeanUtils.copyProperties(taskPatchDTO, newTask, getNullPropertyNames(taskPatchDTO));

        taskMapper.updateTask(newTask);
    }

    /**
     *根据任务Id查询任务
     * @param taskId
     * @return
     */
    @Override
    public Task getTaskById(Long taskId) {
        Task task = taskMapper.getTaskById(taskId);
        return task;
    }

    /**
     * 获取对象中所有为null的属性名 (用于更新任务)
     * @param source 源对象
     * @return 为null的属性名数组
     */
    private String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }
}
