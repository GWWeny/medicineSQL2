package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("client")
public class Client {
    //不加主键的话会有警告，且无法使用selectById,updateById,deleteById等方法
    @TableId
    public Integer cno;//编号

    //@NotBlank(message = "客户姓名不能为空")
    //@Size(max=20,message="客户姓名不能超过20个字符")
    public String cname;//姓名

    //@NotBlank(message = "客户性别不能为空")
    //@Size(max=1,message="客户性别不能超过1个字符")
    public String csex;//性别

    public String cage;//年龄

    public String cphone;//电话
    public String caddress;//地址
    public String csymptom;//症状
    public String cdate;//时间
    public String cremark;//备注

    @TableField(fill= FieldFill.INSERT)
    public Integer mno;//已购药品

    @TableField(fill= FieldFill.INSERT)
    public Integer ano;//经办人


}
