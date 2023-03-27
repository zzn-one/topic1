package com.zzn.config;

import com.zzn.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzn
 * @create 2022-12-16 4:08
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /*
     * 开启跨域
     * */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOriginPatterns("*")
//                // 设置允许的请求方式
//                .allowedMethods("*")
//                // 设置允许的header属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
//    }

    /*
     * 拦截器
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathsList = new ArrayList<>();
        excludePathsList.add("/*/login/**");
        excludePathsList.add("/dict/**");


        registry.addInterceptor(new JwtInterceptor())
                .excludePathPatterns(excludePathsList)
                .addPathPatterns("/**");
    }
}
