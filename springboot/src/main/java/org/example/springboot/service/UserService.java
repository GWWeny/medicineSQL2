package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot.entity.User;
import org.example.springboot.exception.serviceException;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Autowired
    private UserMapper userMapper;

    //相对与selectUserByUsername来说，QueryWrapper灵活性更高，便于拓展和维护
    private User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    //调用
    public User getByUsername(String username){
        return userMapper.selectUserByUsername(username);
    }

    public User login(User user){
        User exisitingtUser = getUserByUsername(user.getUsername());
        if(exisitingtUser==null){
            throw new serviceException("用户名不存在");
        }
        if(!exisitingtUser.getPassword().equals(user.getPassword())){
            throw new serviceException("密码错误");
        }
        String token = TokenUtils.createToken(String.valueOf(exisitingtUser.getId()));
        exisitingtUser.setToken(token);
        return exisitingtUser;
    }

    public User register(User user){
        User exisitingtUser = getUserByUsername(user.getUsername());
        if(exisitingtUser!=null){
            throw new serviceException("用户名已存在");
        }
        userMapper.insert(user);
        return user;
    }

    @Transactional
    public User updateUser(User user)
    {
        User existingUser=userMapper.selectUserByUsername(user.getUsername());
        if(existingUser !=null){
            if(user.getUsername()!=null){
                existingUser.setUsername(user.getUsername());
            }
            if(user.getPassword()!=null){
                existingUser.setPassword(user.getPassword());
            }

            int rowsUpdated=userMapper.update(existingUser);
            return rowsUpdated > 0 ? existingUser: null;
        }
        return null;
    }
}
