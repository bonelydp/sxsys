package com.example.service;

import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.User;

public interface UserService {
    //根据用户名寻找用户
    User findByUserName(String username);

    //进行注册
    void register(String username, String password);


    //根据用户名寻找密码
    String findByPassword(String username);

    //更新
    void update(User user);

    //获取全部用户信息
    PageBean<User> list(Integer pageNum,Integer pageSize);

    void delete(Integer id);
}
