package org.star5025.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private long taskId;

    private String taskName;

    private boolean status;

    private LocalDateTime createdTime;

    private LocalDateTime startTime;

    private LocalDateTime remindTime;

    private LocalDateTime dueTime;

    private long userId;
}
