package org.star5025.backend.service;

import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 查询所有用户，测试用
     * @return
     */
    public List<User> getAllUserTest();

    /**
     * 用户注册
     * @param userDTO
     */
    void register(UserDTO userDTO);

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    User login(UserDTO userDTO);
}
