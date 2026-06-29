package com.example.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.mapper.UserMapper;
import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        User u = userMapper.finByUserName(username);
        return u;
    }

    @Override
    public void register(String username, String password) {
        //加密

        //添加
        userMapper.add(username,password);
    }

    @Override
    public String findByPassword(String username) {
        return userMapper.finByPassword(username);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        System.out.println("idImpl->"+id);
        System.out.println("user->"+user);
        userMapper.update(user);
    }

    @Override
    public PageBean<User> list(Integer pageNum, Integer pageSize) {
        PageBean<User> pb = new PageBean<>();
        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<User> us = userMapper.list();
        //Page中提供了方法，可以获取PageHelper分页查询后得到的《总记录条数》和《当前页数据》
        Page<User> p = (Page<User>) us;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }
}
