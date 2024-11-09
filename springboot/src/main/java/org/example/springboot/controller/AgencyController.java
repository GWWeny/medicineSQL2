package org.example.springboot.controller;

import org.example.springboot.common.Solve;
import org.example.springboot.entity.Agency;
import org.example.springboot.service.AgencyService;
import org.springframework.web.bind.annotation.*;
import org.example.springboot.mapper.ClientMapper;
import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/agency")
public class AgencyController {
    @Resource
    private AgencyService agencyService;

    @Resource
    private ClientMapper clientMapper;
    //添加新经办人，信息录入
    @RequestMapping("/add")
    public Solve addAgency(@RequestBody Agency agency){
        if(agency.getAname()==null || agency.getAname().isEmpty()){
            return Solve.error("经办人姓名不能为空");
        }
        if(agency.getAsex()==null || agency.getAsex().isEmpty()){
            return Solve.error("经办人性别不能为空");
        }
        if(agency.getAphone()==null || agency.getAphone().isEmpty()){
            return Solve.error("经办人联系方式不能为空");
        }
        if(!agency.getAsex().equals("男") && !agency.getAsex().equals("女")){
            return Solve.error("经办人性别只能为男或女");
        }
        if(agencyService.save(agency)){
            return Solve.success(agency);
        }else{
            return Solve.error("添加经办人失败");
        }
    }

    //根据编号删除，信息删除
    @DeleteMapping("/delete/{ano}")
    public Solve deleteAgency(@PathVariable Integer ano) {
        if(agencyService.count()==0){
            return Solve.failure("批量删除失败,数据库没有任何数据");
        }
        if(clientMapper.countClientByAgency(ano)>0){
            return Solve.failure("该经办人已有客户，无法删除");
        }else{
            if (agencyService.deleteAgency(ano)) {
                return Solve.success("经办人编号" + ano + "删除成功");
            } else {
                return Solve.error("经办人编号" + ano + "删除失败");
            }
        }
    }

    //批量删除
    @DeleteMapping("/deleteBatch")
    public Solve deleteBatch(@RequestBody List<Integer> anoList) {
        if(agencyService.count()==0){
            return Solve.failure("批量删除失败,数据库没有任何数据");
        }
        for(Integer ano:anoList){
            if(clientMapper.countClientByAgency(ano)>0){
                return Solve.failure("批量删除失败，因经办人"+ano+"存在关联的顾客信息");
            }
        }
        agencyService.deleteAgencyBatch(anoList);
        return Solve.success("批量删除成功");
    }

    //通过编号查询经办人信息
    @GetMapping("/getByAno")
    public Solve getAgencyByAno(Integer ano) {
        Agency agency = agencyService.getAgencyByAno(ano);
        if(agency!=null){
            return Solve.success(agency);
        }else{
            return Solve.failure("未找到该经办人");
        }
    }

    //找寻所有经办人
    @GetMapping("/findAll")
    public Solve findAll() {
        List<Agency> agencies = agencyService.findAll();
        if(agencies==null || agencies.isEmpty()){
            return Solve.error("经办人列表为空");
        }
        return Solve.success(agencies);
    }

    ////自由查询经办人信息
    @GetMapping("/search")
    public Solve searchAgency(@RequestParam(required = false)Integer ano, String aname, String asex, String aphone,String aremark) {
        List<Agency> agencies = agencyService.searchAgency(ano, aname, asex, aphone, aremark);
        if(agencies==null || agencies.isEmpty()){
            return Solve.error("经办人列表为空");
        }
            return Solve.success(agencies);
    }


    //信息修改
    @PutMapping("/update/{ano}")
    public Solve updateAgency(@PathVariable("ano") int ano,@RequestBody Agency agency) {
        agency.setAno(ano);
        Agency updateAgency = agencyService.updateAgency(agency);
        if(updateAgency!=null){
            return Solve.success(updateAgency);
        }else{
            return Solve.failure("未找到经办人信息进行修改");
        }
    }

    @PutMapping("/reorder")
    public Solve reorderagencys(@RequestBody List<Agency> agencys){
        // 遍历经办人列表并更新经办人编号
        for (int i = 0; i < agencys.size(); i++) {
            Agency agency = agencys.get(i);
            // 设置新的经办人编号，假设从 1 开始
            agency.setAno(i + 1);
            // 更新经办人信息
            agencyService.updateById(agency);
        }
        return Solve.success("经办人编号重新排序成功");
    }
}
