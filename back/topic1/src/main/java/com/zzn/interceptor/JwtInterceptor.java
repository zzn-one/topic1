package com.zzn.interceptor;

import com.zzn.utils.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zzn
 * @create 2022-12-16 16:06
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String jwt = request.getHeader("Authorization");
        JwtUtil.verify(jwt);
        return true;
    }
}
