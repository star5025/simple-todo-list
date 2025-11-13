package org.star5025.backend.service;

import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 查询所有用户，测试用
     * @return
     */
    List<User> getAllUserTest();

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

    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    User getUserById(Long userId);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    void updateUser(User user);

    /**
     * 删除/注销用户
     * @param user
     * @return
     */
    void deleteUser(User user);
    
    /**
     * 增加用户任务计数
     * @param userId
     * @param count
     */
    void incrementTaskCount(Long userId, int count);
}