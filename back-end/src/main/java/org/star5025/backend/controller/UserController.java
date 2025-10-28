package org.star5025.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.entity.User;
import org.star5025.backend.properties.JwtProperties;
import org.star5025.backend.result.Result;
import org.star5025.backend.service.UserService;
import org.star5025.backend.utils.JwtUtil;
import org.star5025.backend.vo.UserVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

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

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserDTO userDTO){
        //登录时前端并不知道UserId，所以传进来的是UserDTO。
        //登录后UserId被返回给前端，所以知道UserId，后续传进来的是User
        log.info("用户登录：用户名：{}",userDTO.getUserName());
//        userService.login(userDTO);
//        return "success";
        try{
            User user = userService.login(userDTO);
            //登录成功后，生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getSecretKey(),
                    jwtProperties.getTtl(),
                    claims);

            UserVO userVO = UserVO.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .token(token)
                    .build();

            return Result.success(userVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //不出意外的话是不会执行到这的
        return Result.error("出错了");
    }

    /**
     * 用户登出
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result logout(){
        //用户登出是在前端的localhost直接把token什么的给删掉，所以后端实际上没有服务类代码，直接返回成功信息即可
        return Result.success();
    }

    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    @ApiOperation("根据用户Id查找用户")
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @ApiOperation("修改用户信息")
    @PutMapping
    public Result updateUser(@RequestBody User user){
        log.info("修改Id为{}的用户",user.getUserId());
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 删除/注销用户
     * @param user
     * @return
     */
    @ApiOperation("删除用户")
    @DeleteMapping
    public Result deleteUser(User user){
        log.info("删除Id为{}的用户",user.getUserId());
        userService.deleteUser(user);
        return Result.success();
    }
}