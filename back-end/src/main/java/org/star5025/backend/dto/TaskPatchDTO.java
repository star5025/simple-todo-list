package org.star5025.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskPatchDTO {
    private String taskName;

    private Boolean status;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime remindTime;

    private LocalDateTime dueTime;
    
    private Boolean favourite;
}