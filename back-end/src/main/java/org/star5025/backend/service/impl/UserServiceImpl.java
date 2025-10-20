package org.star5025.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.star5025.backend.entity.User;
import org.star5025.backend.mapper.UserMapper;
import org.star5025.backend.service.UserService;

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
        return userMapper.getAll();
    }
}
