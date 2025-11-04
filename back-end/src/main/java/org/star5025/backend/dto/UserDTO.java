package org.star5025.backend.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {

    @NotBlank(message = "用户名不能为空" , groups = ValidationGroup.Group1.class)
    @Size(min = 3, max = 16, message = "用户名长度必须在3-16之间", groups = ValidationGroup.Group2.class)
    @Pattern(regexp = "^\\S+$", message = "用户名不能包含空格",  groups = ValidationGroup.Group3.class)
    private String userName;

    @NotBlank(message = "密码不能为空",  groups = ValidationGroup.Group1.class)
    @Size(min = 5, message = "密码最小长度为5",  groups = ValidationGroup.Group2.class)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "密码只能包含字母、数字和下划线",   groups = ValidationGroup.Group3.class)
    private String userPassword;

}
