package org.star5025.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.entity.User;
import org.star5025.backend.exception.UserLoginException;
import org.star5025.backend.mapper.UserMapper;
import org.star5025.backend.service.UserService;

import javax.security.auth.login.LoginException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询所有用户，测试用
     * @return
     */
    @Override
    public List<User> getAllUserTest() {
        return userMapper.getAllUsers();
    }

    /**
     * 用户注册
     * @param userDTO
     */
    @Override
    public void register(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        userMapper.register(user);
    }

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @Override
    public User login(UserDTO userDTO) {
        String userName = userDTO.getUserName();
        String userPassword = userDTO.getUserPassword();
        // 根据员工用户名查找
        User user = userMapper.getByUserName(userName);
        if(user==null){
            throw new UserLoginException("User not found");
        }

        if (!userPassword.equals(user.getUserPassword())) {
            throw new UserLoginException("Password not match");
        }
        return user;
    }

    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Long userId) {
        return userMapper.getByUserId(userId);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    /**
     * 删除/注销用户
     * @param user
     * @return
     */
    @Override
    public void deleteUser(User user) {
        userMapper.delete(user);
    }


}
