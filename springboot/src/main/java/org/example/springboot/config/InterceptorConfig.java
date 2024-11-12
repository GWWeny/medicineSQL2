/*package org.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    //将会拦截所有接口验证，除去login和register

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")//拦截路径
                .excludePathPatterns("/login", "/register");//公开路径
    }

    //通过Bean注解，容器自动创建并管理这个拦截器
    @Bean
    public org.example.springboot.config.JwtInterCeptor jwtInterceptor() {
        return new org.example.springboot.config.JwtInterCeptor();
    }
}*/
