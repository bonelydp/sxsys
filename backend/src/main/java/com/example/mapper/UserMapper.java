package com.example.mapper;

import com.example.pojo.Article;
import com.example.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User finByUserName(String username);

    @Insert("insert into user(username,password,create_time,update_time,permission) values(#{username},#{password},now(),now(),'admin')")
    void add(String username, String password);

    @Select("select password from user where username = #{username} ")
    String finByPassword(String username);

    //修改用户信息
    @Update("update user set e_mail=#{email},update_time=#{updateTime} where id=#{id}")
    void update(User user);

    @Select("select * from user")
    List<User> list();

    @Delete("delete from user where id=#{id}")
    void delete(Integer id);
}
