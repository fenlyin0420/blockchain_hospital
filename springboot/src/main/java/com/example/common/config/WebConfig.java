package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 先不用拦截器，便于测试api -- LuHao :)     

    // 加自定义拦截器JwtInterceptor，设置拦截规则
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(jwtInterceptor).addPathPatterns("/**") //所有界面都要拦截
    //             .excludePathPatterns("/")     //exclude：排除  排除下面这些路径：不能拦截登录，注册等等
    //             .excludePathPatterns("/login")
    //             .excludePathPatterns("/register")
    //             .excludePathPatterns("/files/**")
    //             .excludePathPatterns("/referal/b/**")
    //             .excludePathPatterns("/referal/test");
    // }
}