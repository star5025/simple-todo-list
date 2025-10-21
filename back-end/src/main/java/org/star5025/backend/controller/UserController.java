package org.star5025.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.entity.User;
import org.star5025.backend.service.UserService;

import java.util.List;

@Slf4j
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户，测试用
     * @return
     */
    @ApiOperation("全部用户查询接口")
    @GetMapping("/all")
    public List<User> getAllUserTest(){
        return userService.getAllUserTest();
    }

    /**
     * 用户注册
     * @param userDTO
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO){
        log.info("用户注册：{}",userDTO);
        userService.register(userDTO);
        return "success";
    }
}
