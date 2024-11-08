package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.User;

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username=#{username}")
    User selectUserByUsername(String username);
}
