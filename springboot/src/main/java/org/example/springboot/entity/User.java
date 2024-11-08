package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;//编号

    private String username;//用户名
    private String password;//密码
    private String name;//姓名
    private String phone;//电话
    private String email;//邮箱
    private String address;//地址
    private String role;//角色

    @TableField(exist = false)
    //表示数据并不存在
    private String token;//密钥

}
