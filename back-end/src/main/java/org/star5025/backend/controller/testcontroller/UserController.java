package org.star5025.backend.controller.testcontroller;

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
}
