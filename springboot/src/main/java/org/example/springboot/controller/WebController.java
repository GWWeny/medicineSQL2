package org.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import org.example.springboot.common.Solve;
import org.example.springboot.exception.serviceException;
import org.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.springboot.entity.User;
import javax.annotation.Resource;

@RestController
@RequestMapping()
public class WebController {
    @Resource
    private UserService userService;

    @RequestMapping("/")
    public Solve index() {
        return Solve.success("success");
    }

    @PostMapping("/login")
    public Solve login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())){
            throw new serviceException("用户名或密码不能为空");
        }
        user = userService.login(user);
        return Solve.success(user);
    }

    @PostMapping("/register")
    public Solve register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getRole())){
            throw new serviceException("用户名或密码不能为空");
        }
        user = userService.register(user);
        return Solve.success(user);
    }
}
