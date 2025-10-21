package org.star5025.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}
