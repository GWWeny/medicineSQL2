package org.example.springboot.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("medicine")
public class Medicine {
    @TableId
    public Integer mno;//编号

    public String mname;//名称
    public String mmode;//服用方法
    public String mefficacy;//功效
}
