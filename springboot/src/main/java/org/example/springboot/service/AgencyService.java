package org.example.springboot.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot.entity.Agency;
import org.example.springboot.mapper.AgencyMapper;
import org.example.springboot.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgencyService extends ServiceImpl<AgencyMapper, Agency> {
    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private ClientMapper clientMapper;

    //根据ano获得单个Agency对象
    private Agency getAgency(Integer ano){
        return agencyMapper.selectAgencyByAno(ano);
    }

    //获得所有Agency对象
    public List<Agency> findAll(){
        return agencyMapper.selectList(null);
    }

    //对外提供mno获取Agency方法
    public Agency getAgencyByAno(Integer ano){
        return getAgency(ano);
    }

    //根据ano 删除 Agency对象，并返回是否成功
    @Transactional
    public boolean deleteAgency(Integer ano){
        try{
            agencyMapper.delete(ano);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    //根据编号批量删除
    @Transactional
    public void deleteAgencyBatch(List<Integer> anoList){
        /*for(Integer ano:anoList){
            if(clientMapper.countClientByAgency(ano)>0){
                throw new RuntimeException("批量删除失败，因经办人"+ano+"存在关联的顾客信息");
            }
        }*/
        agencyMapper.deleteAgencyBatch(anoList);
    }

    //更新Agency信息并返回更新后的对象，失败返回null
    @Transactional
    public Agency updateAgency(Agency agency){
        Agency existingAgency=agencyMapper.selectAgencyByAno(agency.getAno());
        if(existingAgency !=null){
            if(agency.getAno()!=null){
                existingAgency.setAno(agency.getAno());
            }
            if(agency.getAname()!=null){
                existingAgency.setAname(agency.getAname());
            }
            if(agency.getAsex()!=null){
                existingAgency.setAsex(agency.getAsex());
            }
            if(agency.getAphone()!=null){
               existingAgency.setAphone(agency.getAphone());
            }
            if(agency.getAremark()!=null){
                existingAgency.setAremark(agency.getAremark());
            }
            int rowsUpdated = agencyMapper.update(existingAgency);
            return rowsUpdated > 0 ? existingAgency: null;
        }
        return null;
    }

    //根据多个条件搜索Agency对象
    public List<Agency> searchAgency(Integer ano, String aname, String asex, String aphone,String aremark){
        QueryWrapper<Agency> queryWrapper = new QueryWrapper<>();
        // 根据传入的参数添加查询条件
        if(ano!=null){
            queryWrapper.eq("ano", ano);
        }
        if(aname!=null && !aname.isEmpty()){
            queryWrapper.like("aname", aname);
        }
        if(asex!=null && !asex.isEmpty()){
            queryWrapper.eq("asex", asex);
        }
        if(aphone!=null && !aphone.isEmpty()){
            queryWrapper.eq("aphone", aphone);
        }
        if(aremark!=null && !aremark.isEmpty()){
            queryWrapper.like("aremark", aremark);
        }
        // 返回结果
        return agencyMapper.selectList(queryWrapper);
    }

    public boolean existsByAno(Integer ano){
        Agency agency = agencyMapper.selectAgencyByAno(ano);
        return agency != null;
    }
}
