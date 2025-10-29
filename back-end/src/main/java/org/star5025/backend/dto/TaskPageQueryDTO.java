package org.star5025.backend.dto;

import lombok.Data;

@Data
public class TaskPageQueryDTO {
    private int page;

    private int pageSize;

    private Long userId;

}
