package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.springboot.entity.Qa;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QaMapper {
    @Select("select * from qa")
    List<Qa> findAll();
    @Update("insert into qa(`id`,`username`,`password`)values(#{id},#{username},#{password})")
    @Transactional
    void add(Qa qa);
}
