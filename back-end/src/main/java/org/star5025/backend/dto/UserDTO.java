package org.star5025.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String userName;

    private String userPassword;

}
