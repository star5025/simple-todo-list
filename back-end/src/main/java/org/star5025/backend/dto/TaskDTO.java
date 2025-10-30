package org.star5025.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDTO {

    private String taskName;

    private Boolean status;

    private LocalDateTime startTime;

    private LocalDateTime remindTime;

    private LocalDateTime dueTime;

    private long userId;
}

