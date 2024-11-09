package org.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import org.example.springboot.common.Solve;
import org.example.springboot.config.AuthAccess;
import org.example.springboot.exception.serviceException;
import org.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.example.springboot.entity.User;
import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping()
public class WebController {
    @Resource
    private UserService userService;

    @AuthAccess
    @RequestMapping("/")
    public Solve index() {
        return Solve.success("success");
    }

    @AuthAccess
    @PostMapping("/login")
    public Solve login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())){
            return Solve.failure("用户名或密码不能为空");
        }
        user = userService.login(user);
        return Solve.success(user);
    }

    @AuthAccess
    @PostMapping("/register")
    public Solve register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getRole())){
            return Solve.failure("用户名或密码或身份不能为空");
        }
        user = userService.register(user);
        return Solve.success(user);
    }
}
