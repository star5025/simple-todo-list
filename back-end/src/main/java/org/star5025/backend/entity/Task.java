package org.star5025.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long taskId;

    private String taskName;

    private Boolean status;

    private String description;

    private LocalDateTime createdTime;

    private LocalDateTime startTime;

    private LocalDateTime remindTime;

    private LocalDateTime dueTime;

    private Long userId;
}