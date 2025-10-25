package org.star5025.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.star5025.backend.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 查询所有用户，测试用
     * @return
     */
    @Select("select * from user")
    List<User> getAll();

    /**
     * 用户注册
     * @param user
     */
    @Insert("insert into user (user_name, user_password) values (#{userName}, #{userPassword})")
    void register(User user);

    /**
     * 根据用户名查找用户
     * @param userName
     * @return
     */
    @Select("select * from user where user_name = #{userName}")
    User getByUserName(String userName);

    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    @Select("select * from user where user_id = #{userId}")
    User getByUserId(Long userId);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Update("update user set user_name = #{userName}, user_password = #{userPassword} where user_id = #{userId}")
    void update(User user);
}
