package com.example.config;

import com.example.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//注册拦截器
@Configuration
public class  WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Value("${app.cors.allowed-origin-patterns:http://localhost:8080}")
    private String[] allowedOriginPatterns;

    @Value("${app.upload-dir:/app/files}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register","/files/**","/sequence","/runBlast","/contour/selectAll","/teamintroduction/selectAll",
                "/runMafft","/treeImage","/iqtree","/notice/**","/teamintroduction/**","/contour1/selectAll",
                "/sampleinfo/year","/sampleinfo/host","/sampleinfo/province_infectionRate",
                "/sampleinfo/details","/sampleinfo/infectionRate","/sampleinfo/province-infection-count",
                "/sampleinfo/host_details","/sampleinfo/city-infection-count",
                "/sampleinfo/all-infection-count","/sampleinfo/sample-host-province-count","/pageview/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOriginPatterns)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
