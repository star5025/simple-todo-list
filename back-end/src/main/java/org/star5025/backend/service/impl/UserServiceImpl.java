package org.star5025.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.entity.User;
import org.star5025.backend.enumeration.AuthErrorType;
import org.star5025.backend.exception.AuthException;
import org.star5025.backend.mapper.UserMapper;
import org.star5025.backend.service.UserService;
import org.star5025.backend.utils.Md5Util;

import java.time.LocalDateTime;
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
        // 对用户密码进行加密后再传入数据库，使用工具类的加密算法
        user.setUserPassword(Md5Util.getMD5String(user.getUserPassword()));
        // 设置创建时间
        user.setCreateTime(LocalDateTime.now());
        // 初始化任务计数为0
        user.setTaskCount(0);
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
        // 对用户输入的密码进行加密，以对数据库中已加密的密码进行匹配。使用工具类方法加密。
        String userPassword = Md5Util.getMD5String(userDTO.getUserPassword());
        // 根据用户名查找
        User user = userMapper.getByUserName(userName);
        if(user==null){
            throw new AuthException(AuthErrorType.USERNAME_NOT_FOUND ,"User not found");

            //是否需要跳转到注册？
            //TODO 好问题，我也不知到要不要跳转到注册

        }

        if (!userPassword.equals(user.getUserPassword())) {
            throw new AuthException(AuthErrorType.PASSWORD_INCORRECT ,"Password not match");
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
    
    /**
     * 增加用户任务计数
     * @param userId
     * @param count
     */
    @Override
    public void incrementTaskCount(Long userId, int count) {
        userMapper.incrementTaskCount(userId, count);
    }
    
    /**
     * 验证用户密码
     * @param userId 用户ID
     * @param password 密码（明文）
     * @return 是否匹配
     */
    @Override
    public boolean verifyPassword(Long userId, String password) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }
        String encryptedPassword = Md5Util.getMD5String(password);
        return encryptedPassword.equals(user.getUserPassword());
    }
}