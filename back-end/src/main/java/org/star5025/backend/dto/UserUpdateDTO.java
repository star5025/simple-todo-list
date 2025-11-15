package org.star5025.backend.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Long userId;
    private String userName;
    private String oldPassword;
    private String userPassword;
}