package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.springboot.entity.Medicine;
import org.example.springboot.entity.User;

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username=#{username}")
    User selectUserByUsername(String username);

    @Update("update user set username = #{username}, password = #{password}, name = #{name} ,phone=#{phone},email=#{eamil},address=#{address} WHERE id = #{id}")
    int update(User user);
}
