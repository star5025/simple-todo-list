package org.star5025.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.star5025.backend.dto.UserDTO;
import org.star5025.backend.dto.UserUpdateDTO;
import org.star5025.backend.dto.ValidationGroup;
import org.star5025.backend.entity.User;
import org.star5025.backend.exception.AuthException;
import org.star5025.backend.properties.JwtProperties;
import org.star5025.backend.result.Result;
import org.star5025.backend.service.UserService;
import org.star5025.backend.utils.JwtUtil;
import org.star5025.backend.utils.Md5Util;
import org.star5025.backend.vo.UserVO;

import javax.validation.Valid;
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
    public Result register(@Validated({ValidationGroup.Group1.class, ValidationGroup.Group2.class, ValidationGroup.Group3.class}) @RequestBody @Valid UserDTO userDTO){
        log.info("用户注册：{}",userDTO);
        try{
            userService.register(userDTO);
            return Result.success();
        }catch (AuthException e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody @Valid UserDTO userDTO){
        //登录时前端并不知道UserId，所以传进来的是UserDTO。
        //登录后UserId被返回给前端，所以知道UserId，后续传进来的是User
        log.info("用户登录：用户名：{}",userDTO.getUserName());
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
        } catch (AuthException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
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
        log.info("根据Id查找用户：{}",user);
        return Result.success(user);
    }

    /**
     * 修改用户信息（用户名或密码）
     * @param userUpdateDTO
     * @return
     */
    @ApiOperation("修改用户信息")
    @PutMapping
    public Result updateUser(@RequestBody UserUpdateDTO userUpdateDTO){
        log.info("修改Id为{}的用户", userUpdateDTO.getUserId());
        
        try {
            User user = userService.getUserById(userUpdateDTO.getUserId());
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 如果要修改密码，需要验证旧密码
            if (userUpdateDTO.getOldPassword() != null && !userUpdateDTO.getOldPassword().isEmpty()) {
                // 验证旧密码是否正确
                String encryptedOldPassword = Md5Util.getMD5String(userUpdateDTO.getOldPassword());
                if (!encryptedOldPassword.equals(user.getUserPassword())) {
                    return Result.error("原密码不正确");
                }
                
                // 设置新密码（需要加密）
                user.setUserPassword(Md5Util.getMD5String(userUpdateDTO.getUserPassword()));
            }
            
            // 如果要修改用户名
            if (userUpdateDTO.getUserName() != null && !userUpdateDTO.getUserName().isEmpty()) {
                user.setUserName(userUpdateDTO.getUserName());
            }
            
            userService.updateUser(user);
            return Result.success();
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败");
        }
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