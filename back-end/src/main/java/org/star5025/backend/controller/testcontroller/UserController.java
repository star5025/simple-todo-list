package org.star5025.backend.controller.testcontroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.star5025.backend.entity.User;
import org.star5025.backend.service.UserService;

import java.util.List;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/admin/user")
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
}
