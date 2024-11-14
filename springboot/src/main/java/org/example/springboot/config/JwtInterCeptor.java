package org.example.springboot.config;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.example.springboot.entity.User;
import org.example.springboot.exception.serviceException;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.utils.TokenUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterCeptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    //，请求处理前调用，预处理请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头或请求参数中获取token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        System.out.println("接收到的 token：" + token);

        // 如果请求的方法不需要验证Token，则跳过
        if (handler instanceof HandlerMethod) {
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                return true; // 如果有 AuthAccess 注解，跳过Token验证
            }
        }

        // 判断Token是否为空，若为空则抛出未授权异常
        if (StrUtil.isBlank(token)) {
            throw new serviceException("401", "请先登录");
        }

        Claims claims;
        try {
            claims = TokenUtils.parseToken(token);
        } catch (ExpiredJwtException e) {
            throw new serviceException("401", "Token已过期，请重新登录");
        } catch (Exception e) {
            throw new serviceException("401", "Token无效，请重新登录");
        }

        // 获取用户id并验证
        String userId = claims.getSubject();
        if (StrUtil.isBlank(userId)) {
            throw new serviceException("401", "Token中无用户信息");
        }

        // 根据userId查询用户，确保用户存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new serviceException("401", "用户不存在，请重新登录");
        }

        // 检查Token是否已过期
        if (TokenUtils.isTokenExpired(token)) {
            throw new serviceException("401", "Token已过期，请重新登录");
        }

        // 角色验证，确保是管理员
        if (!isAdmin(user)) {
            throw new serviceException("403", "权限不足");
        }

        return true;
    }

    // 检查用户是否是管理员
    private boolean isAdmin(User user) {
        return "admin".equals(user.getRole());
    }
}
