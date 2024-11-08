package org.example.springboot.controller;

import org.example.springboot.common.Solve;
import org.example.springboot.entity.Medicine;
import org.example.springboot.mapper.ClientMapper;
import org.example.springboot.service.MedicineService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    @Resource
    private MedicineService medicineService;
    @Resource
    private ClientMapper clientMapper;
    //添加新药品,信息录入
    @RequestMapping("/add")
    public Solve addMedicine(@RequestBody Medicine medicine){
        //检查数据完整性与有效性
        if(medicine.getMname()==null || medicine.getMname().isEmpty()){
            return Solve.failure("药品名称不能为空");
        }
        if(medicine.getMmode()==null || medicine.getMmode().isEmpty()){
            return Solve.failure("药品使用方式不能为空");
        }
        if(medicine.getMefficacy()==null || medicine.getMefficacy().isEmpty()){
            return Solve.failure("药品功效不能为空");
        }
        if(!medicine.getMefficacy().equals("内服") && !medicine.getMefficacy().equals("外用")){
            return Solve.failure("药品使用方式只能为内服或外用");
        }
        if(medicineService.save(medicine)){
            return Solve.success(medicine);
        }else{
            return Solve.error("添加药品失败");
        }
    }

    //根据编号删除,信息删除
    @DeleteMapping("/delete/{mno}")
    public Solve deleteMedicine(@PathVariable Integer mno) {
        if(medicineService.count()==0){
            return Solve.failure("批量删除失败,数据库没有任何数据");
        }
        if(clientMapper.countClientByMedicine(mno)>0){
            return Solve.failure("该药品已有客户，无法删除");
        }else{
            if(medicineService.deleteMedicine(mno)) {
                //需要修改，返回值没有出来
                //返回204 No Content 表示删除成功
                return Solve.success("药品编号"+mno+"已删除成功");
            }else{

                return Solve.error("药品编号"+mno+"未找到");
            }
        }

    }

    //批量删除,[1,2,3]
    @DeleteMapping("/deleteBatch")
    public Solve deleteBatch(@RequestBody List<Integer> mnoList){
        if(medicineService.count()==0){
            return Solve.failure("批量删除失败,数据库没有任何数据");
        }
        for(Integer mno:mnoList){
            if(clientMapper.countClientByMedicine(mno)>0){
                return Solve.failure("批量删除失败，因药品"+mno+"存在关联的顾客信息");
            }
        }
        medicineService.deleteBatch(mnoList);
        return Solve.success("批量删除成功");
    }

    //通过编号找寻药品
    @GetMapping("/getByMno")
    public Solve getMedicineByMno(Integer mno){
        Medicine medicine = medicineService.getMedicineByMno(mno);
        if(medicine != null){
            return Solve.success(medicine);
        }else{
            return Solve.error("未找到该药品");
        }
    }

    //找寻所有药品,也就是信息浏览
    @GetMapping("/findAll")
    public Solve findAll(){
        List<Medicine> medicines = medicineService.findAll();
        if(medicines==null ||medicines.isEmpty()){
            return Solve.error("药品列表为空");
        }
        return Solve.success(medicines);
    }

    //自由查询药品信息
    @GetMapping("/search")
    public Solve searchMedicines(@RequestParam(required = false)Integer mno, @RequestParam(required = false)String mname, @RequestParam(required = false)String mmode, @RequestParam(required = false)String mefficacy){
        List<Medicine> medicines=medicineService.searchMedicines(mno, mname, mmode, mefficacy);
        if(medicines==null || medicines.isEmpty()){
            return Solve.error("未找到符合条件的药品信息");
        }
        return Solve.success(medicines);
    }

    //信息修改
    @PutMapping("/update/{mno}")
    public Solve updateMedicine(@PathVariable("mno") int mno,@RequestBody Medicine medicine){
        medicine.setMno(mno);
        Medicine updatedMedicine = medicineService.updateMedicine(medicine);
        if(updatedMedicine != null){
            return Solve.success(updatedMedicine);
        }else{
            return Solve.error("未找到药品进行更新");
        }
    }

    //更新排序,需要修改
    @PutMapping("/reorder")
    public Solve reorder(@RequestBody List<Medicine> medicines){
        for(int i=0;i<medicines.size();i++){
            Medicine medicine=medicines.get(i);
            medicine.setMno(i+1);
            medicineService.updateMedicine(medicine);
        }
        return Solve.success("排序成功");
    }
}
