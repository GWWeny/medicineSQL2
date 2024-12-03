package org.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.springboot.common.Solve;
import org.example.springboot.entity.Medicine;
import org.example.springboot.mapper.ClientMapper;
import org.example.springboot.service.MedicineService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
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
        //如果药品编号已存在,就报错
        if (medicineService.existsByMno(medicine.getMno())) {return Solve.failure("药品编号已存在");}
        if(medicine.getMname()==null || medicine.getMname().isEmpty()){return Solve.failure("药品名称不能为空");}
        if(medicine.getMmode()==null || medicine.getMmode().isEmpty()){return Solve.failure("药品使用方式不能为空");}
        if(medicine.getMefficacy()==null || medicine.getMefficacy().isEmpty()){return Solve.failure("药品功效不能为空");}
        if(!medicine.getMmode().equals("内服") && !medicine.getMmode().equals("外用")){return Solve.failure("药品使用方式只能为内服或外用");}
        if(medicineService.save(medicine)){
            return Solve.success(medicine);
            //Solve是一个封装类，由此传回数据code信息，msg信息，data数据
        }else{
            return Solve.failure("添加药品失败");
        }
    }

    //根据编号删除,信息删除
    @DeleteMapping("/delete/{mno}")
    public Solve deleteMedicine(@PathVariable Integer mno) {
        if(medicineService.count()==0){return Solve.failure("批量删除失败,数据库没有任何数据");}
        if(clientMapper.countClientByMedicine(mno)>0){
            return Solve.failure("该药品已有客户，无法删除");
            //这步是我写在后端的判断药品能不能删除的限制条件之一，在删除之前，
            // 他会先去顾客表里查找有没有哪个顾客与这个药品有关联，如果有的话，就返回删除错误，下面的批量删除也是一样的逻辑。
        }else{
            if(medicineService.deleteMedicine(mno)) {
                //需要修改，返回值没有出来（注释：这个是我测试接口时写的一个注意项）
                //返回204 No Content 表示删除成功（这是我第一版的返回数据，但只返回这个，我不是很喜欢，就换了）
                return Solve.success("药品编号"+mno+"已删除成功");
            }else{

                return Solve.failure("药品编号"+mno+"未找到");
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
                //查询顾客表里有没有这个药品编号
                return Solve.failure("批量删除失败，因药品"+mno+"存在关联的顾客信息");
            }
        }
        medicineService.deleteBatch(mnoList);//调用批量删除的服务类方法
        return Solve.success("批量删除成功");
    }

    //通过编号找寻药品
    @GetMapping("/getByMno")
    public Solve getMedicineByMno(Integer mno){
        Medicine medicine = medicineService.getMedicineByMno(mno);//通过编号获取数据
        if(medicine != null){
            return Solve.success(medicine);
        }else{
            return Solve.failure("未找到该药品");
        }
    }

    //找寻所有药品,也就是信息浏览
    @GetMapping("/findAll")
    public Solve findAll(){
        List<Medicine> medicines = medicineService.list(new QueryWrapper<Medicine>().orderByDesc("mno"));
        return Solve.success(medicines);
    }


    //自由查询药品信息
    @GetMapping("/search")
    public Solve searchMedicines(@RequestParam(required = false)Integer mno, @RequestParam(required = false)String mname, @RequestParam(required = false)String mmode, @RequestParam(required = false)String mefficacy){
        //接收这些数据，不一定全部有，可以是空
        List<Medicine> medicines=medicineService.searchMedicines(mno, mname, mmode, mefficacy);
        if(medicines==null || medicines.isEmpty()){
            return Solve.failure("未找到符合条件的药品信息");
        }else{
            return Solve.success(medicines);
        }

    }

    //信息修改
    @PutMapping("/update/{mno}")
    public Solve updateMedicine(@PathVariable("mno") int mno,@RequestBody Medicine medicine){
        //通过 @PathVariable 获取路径参数 mno，通过 @RequestBody 获取请求体中的 Medicine 对象。
        medicine.setMno(mno);//将路径参数 mno 设置到 Medicine 对象中。
        Medicine updatedMedicine = medicineService.updateMedicine(medicine);
        if(updatedMedicine != null){
            return Solve.success(updatedMedicine);
        }else{
            return Solve.failure("未找到药品进行更新");
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
