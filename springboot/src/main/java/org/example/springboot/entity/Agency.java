package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//是 Lombok 库中的一个注解，用于自动生成 Java 类的常规方法，
// 包括 getter、setter、toString、equals 和 hashCode 方法。
// 使用这个注解可以减少样板代码，提高开发效率。
@Data
@TableName("agency")
public class Agency {
    @TableId
    public Integer ano;//编号

    public String aname;//姓名
    public String asex;//性别
    public String aphone;//电话
    public String aremark;//备注
}
