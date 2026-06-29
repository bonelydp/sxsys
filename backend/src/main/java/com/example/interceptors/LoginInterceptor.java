package com.example.interceptors;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.HashMap;
import java.util.Map;


//拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println("DEBUG URI: [" + uri + "] startsWithFiles: " + uri.startsWith("/files/"));
        // 跳过文件访问路径的认证
        if (uri.startsWith("/files/")) {
            return true;
        }
        System.out.println("request"+request);
        //令牌验证
        String token = request.getHeader("Authorization");
        System.out.println("拦截器");
        System.out.println("token+"+token);
        //验证token
        try{
            //验证token
            Map<String,Object> claims = JwtUtil.parseToken(token);
            System.out.println("claims->"+claims);
            //把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            //放行
            return true;
        }catch(Exception e){
            System.out.println("令牌验证没通过");
//            //http响应状态码为401
//            response.setStatus(401);
//            //不放行
//            return false;

            // 判断是否是令牌过期异常
            if (e instanceof TokenExpiredException) {
                // 如果是令牌过期，返回自定义的错误信息
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
                response.setContentType("application/json;charset=UTF-8");

                // 构造返回的JSON数据
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "令牌已过期，请重新登录");

                // 将错误信息写入响应体
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            } else {
                // 其他异常，返回通用的未授权信息
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
                response.setContentType("application/json;charset=UTF-8");

                // 构造返回的JSON数据
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("code", 401);
                errorResponse.put("message", "令牌无效或验证失败");

                // 将错误信息写入响应体
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
