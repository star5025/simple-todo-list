package org.star5025.backend.vo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskVO {
    //然后这里可以看到以天数为单位的倒计时、完成状态和任务名

    private Long taskId;

    private String taskName;

    //这个是剩余时间
    private String timeLeft;

    private boolean status;
}
