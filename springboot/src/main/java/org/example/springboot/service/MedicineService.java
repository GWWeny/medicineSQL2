package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot.entity.Medicine;
import org.example.springboot.mapper.ClientMapper;
import org.example.springboot.mapper.MedicineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicineService extends ServiceImpl<MedicineMapper, Medicine>{
    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private ClientMapper clientMapper;

    //根据mno获得单个Medicine对象
    private Medicine getMedicine(Integer mno){
        return medicineMapper.selectMedicineByMno(mno);
    }

    //获得所有medicine对象
    public List<Medicine> findAll(){
        return medicineMapper.findAllMedicine();
    }

    //对外提供根据mno获取medicine方法
    public Medicine getMedicineByMno(Integer mno){
        return getMedicine(mno);
    }

    //根据mno删除对象，并返回是否删除成功
    @Transactional
    public boolean deleteMedicine(Integer mno)
    {
        try{
            medicineMapper.delete(mno);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    //根据编号批量删除
    @Transactional
    public void deleteBatch(List<Integer> mnoList){
        medicineMapper.deleteBatch(mnoList);
    }

    //更新Medicine信息并返回更新后的对象，更新失败返回null
    @Transactional
    public Medicine updateMedicine(Medicine medicine)
    {
        Medicine existingMedicine=medicineMapper.selectMedicineByMno(medicine.getMno());
        if(existingMedicine !=null){
            if(medicine.getMno()!=null){
                existingMedicine.setMno(medicine.getMno());
            }
            if(medicine.getMname()!=null){
                existingMedicine.setMname(medicine.getMname());
            }
            if(medicine.getMmode()!=null){
                existingMedicine.setMmode(medicine.getMmode());
            }
            if(medicine.getMefficacy()!=null){
                existingMedicine.setMefficacy(medicine.getMefficacy());
            }
            int rowsUpdated=medicineMapper.update(existingMedicine);
            return rowsUpdated > 0 ? existingMedicine: null;
        }
        return null;
    }

    //根据多个条件搜索Medicine对象
    public List<Medicine> searchMedicines(Integer mno, String mname, String mmode, String mefficacy){
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
        // 根据传入的参数添加查询条件
        if (mno != null ) {
            queryWrapper.eq("mno", mno);
        }
        if (mname != null && !mname.isEmpty()) {
            queryWrapper.like("mname", mname); // 使用like匹配名称
        }
        if (mmode != null && !mmode.isEmpty()) {
            queryWrapper.like("mmode", mmode);
        }
        if (mefficacy != null && !mefficacy.isEmpty()) {
            queryWrapper.like("mefficacy", mefficacy); // 使用like匹配功效
        }
        // 返回结果
        return medicineMapper.selectList(queryWrapper);
    }

    public boolean existsByMno(Integer mno) {
        return medicineMapper.countByMno(mno) > 0;
    }
}
