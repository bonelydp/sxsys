package com.example.controller;


import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        User u = userService.findByUserName(username);

        if(u== null){
            //注册
            userService.register(username,password);
            return Result.success();
        }else{
            return Result.error("用户名已经被占用");
        }


    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        System.out.println("username "+username);
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //该用户是否存在
        if(loginUser == null){
            return Result.error("用户名错误");
        }
        //判断密码是否正确
        if(username.equals("adminnn")){
            if(userService.findByPassword(username).equals(password)){
                Map<String,Object> claims = new HashMap<>();
                claims.put("id",loginUser.getId());
                claims.put("username",loginUser.getUsername());
                String token = JwtUtil.genToken(claims);
                System.out.println("loginToken"+token);
                return Result.adminsuccess(token);
            }
        }else{
            if(userService.findByPassword(username).equals(password)){
                Map<String,Object> claims = new HashMap<>();
                claims.put("id",loginUser.getId());
                claims.put("username",loginUser.getUsername());
                String token = JwtUtil.genToken(claims);
                System.out.println("loginToken"+token);
                return Result.success(token);
            }
        }
        return Result.error("密码错误！");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token){
        System.out.println("token->"+token);
        //根据用户名查询用户
//        Map<String,Object> map = JwtUtil.parseToken(token);
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        System.out.println("username->"+username);
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<PageBean<User>> list(Integer pageNum,Integer pageSize){
        PageBean<User> pb = userService.list(pageNum,pageSize);
        return Result.success(pb);
    }

    @DeleteMapping("/delete{id}")
    public Result delete(@RequestParam Integer id){
        userService.delete(id);
        return Result.success();
    }

}
