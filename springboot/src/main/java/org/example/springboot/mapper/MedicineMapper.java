package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.springboot.entity.Medicine;

import java.util.List;

@Mapper
public interface MedicineMapper extends BaseMapper<Medicine> {
    @Select("select * from medicine where mno = #{mno}")
    Medicine selectMedicineByMno(Integer mno);

    @Delete("<script>" +
            "DELETE FROM medicine WHERE mno IN " +
            "<foreach item='mno' collection='list' open='(' separator=',' close=')'>" +
            "#{mno}" +
            "</foreach>" +
            "</script>")
    void deleteBatch(@Param("list") List<Integer> mnoList);

    @Delete("delete from medicine where mno = #{mno}")
    void delete(Integer mno);

    @Update("update medicine set mname = #{mname}, mmode = #{mmode}, mefficacy = #{mefficacy} WHERE mno = #{mno}")
    int update(Medicine medicine);
}
