package org.star5025.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskPageQueryDTO {
    private int page;

    private int pageSize;

    private Long userId;
    
    private Boolean status; // 添加状态筛选字段
    
    private LocalDateTime dueTime; // 添加截止时间筛选字段
    
    private String orderBy; // 添加排序字段

    public TaskPageQueryDTO(int page, int pageSize, Long userId) {
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
    }
    
    public TaskPageQueryDTO(int page, int pageSize, Long userId, Boolean status) {
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
        this.status = status;
    }
    
    public TaskPageQueryDTO(int page, int pageSize, Long userId, Boolean status, LocalDateTime dueTime) {
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
        this.status = status;
        this.dueTime = dueTime;
    }
    
    public TaskPageQueryDTO(int page, int pageSize, Long userId, Boolean status, LocalDateTime dueTime, String orderBy) {
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
        this.status = status;
        this.dueTime = dueTime;
        this.orderBy = orderBy;
    }
}