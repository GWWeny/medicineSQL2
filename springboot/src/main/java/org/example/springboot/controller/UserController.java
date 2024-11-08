package org.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.springboot.common.Solve;
import org.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.springboot.entity.User;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public Solve addUser(@RequestBody User user) {
        User existingUser = userService.getByUsername(user.getUsername());
        if(existingUser!=null){
            return Solve.failure("用户名已存在");
        }
        userService.save(user);
        return Solve.success("添加成功");
    }

    @DeleteMapping("/deleteUserById/{id}")
    public Solve deleteUserById(@PathVariable Integer id) {
        if(userService.removeById(id)){
            return Solve.success("id编号"+id+"删除成功");
        }
        return Solve.error("id编号"+id+"删除失败");
    }

    @DeleteMapping("/deleteUserBatch")
    public Solve deleteUserBatch(@RequestBody List<Integer> idList) {
        if(userService.count()==0){
            return Solve.failure("批量删除失败,数据库没有任何数据");
        }
        //@RequestBody接收参数时，如果发送的请求体为空，并不会被赋予空列表，而是null
        if(idList==null || idList.isEmpty()){
            return Solve.failure("批量删除失败,表中为空");
        }else if(userService.removeBatchByIds(idList)){
            return Solve.success("批量删除成功");
        }else{
            return Solve.error("批量删除失败");
        }
    }

    @GetMapping("/getAllUser")
    public Solve getAllUser(){
        List<User> users=userService.list(new QueryWrapper<User>().orderByDesc("id"));
        if(users.isEmpty()){
            return Solve.error("暂无用户");
        }
        return Solve.success(users);
    }

    @GetMapping("/getUserById/{id}")
    public Solve getUserById(@PathVariable Integer id){
        User user=userService.getById(id);
        if(user!=null){
            return Solve.success(user);
        }else{
            return Solve.error("暂无用户");
        }
    }

    @GetMapping("/getUserByPages")
    public Solve getUserByPages(@RequestParam int pageNum,
                                 @RequestParam int pageSize,
                                 @RequestParam(required = false) String username,
                                 @RequestParam(required = false) String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");

        IPage<User> userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        System.out.println(userPage);

        return Solve.success(userPage);
    }
}
