package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.springboot.entity.Medicine;
import org.example.springboot.entity.User;

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username=#{username}")
    User selectUserByUsername(String username);

    @Select("select * from user where id=#{id}")
    User selectUserById(Integer id);

    @Update("update user set username = #{username}, password = #{password}, name = #{name} ,phone=#{phone},email=#{eamil},address=#{address},token=#{token} WHERE id = #{id}")
    int update(User user);
}
