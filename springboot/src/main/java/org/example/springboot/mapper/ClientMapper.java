package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.springboot.entity.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientMapper extends BaseMapper<Client> {
    @Select("select * from client where cno = #{cno}")
    Client selectClientByCno(Integer cno);

    @Select("select count(*) from client where ano=#{ano}")
    int countClientByAgency(Integer ano);

    @Select("select count(*) from client where mno = #{mno}")
    int countClientByMedicine(Integer mno);

    @Delete("<script>" +
            "DELETE FROM client WHERE cno IN " +
            "<foreach item='cno' collection='list' open='(' separator=',' close=')'>" +
            "#{cno}" +
            "</foreach>" +
            "</script>")
    void deleteBatch(@Param("list") List<Integer> cnoList);

    @Delete("delete from client where cno = #{cno}")
    void delete(Integer cno);

    @Update("update client set cname = #{cname}, csex = #{csex}, cage = #{cage}, cphone = #{cphone},caddress = #{caddress}, csymptom = #{csymptom}, cdate = #{cdate}, cremark = #{cremark},mno=#{mno},ano=#{ano} WHERE cno = #{cno}")
    int update(Client client);

    @Select("SELECT COUNT(*) FROM client WHERE cno = #{cno}")
    int countByCno(Integer cno);

}
