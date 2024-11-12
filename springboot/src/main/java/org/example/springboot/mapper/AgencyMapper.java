package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.springboot.entity.Agency;

import java.util.List;

@Mapper
public interface AgencyMapper extends BaseMapper<Agency> {
    //MyBatis-Plus 会自动生成update,delete,insert,select等方法，只需要在接口中声明方法名，MyBatis-Plus会自动生成实现方法，

    @Select("select * from agency where ano = #{ano}")
    Agency selectAgencyByAno(Integer ano);

    @Delete("<script>" +
            "DELETE FROM agency WHERE ano IN " +
            "<foreach item='ano' collection='list' open='(' separator=',' close=')'>" +
            "#{ano}" +
            "</foreach>" +
            "</script>")
    void deleteAgencyBatch(@Param("list") List<Integer> anoList);

    @Delete("delete from agency where ano = #{ano}")
    void delete(Integer ano);

    @Update("update agency set aname = #{aname}, asex = #{asex}, aphone = #{aphone}, aremark = #{aremark}WHERE ano = #{ano}")
    int update(Agency agency);

    @Select("SELECT COUNT(*) FROM medicine WHERE ano = #{ano}")
    int countByMno(Integer ano);
}
