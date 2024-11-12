package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot.mapper.ClientMapper;
import org.example.springboot.mapper.MedicineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.springboot.entity.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService extends ServiceImpl<ClientMapper, Client> {
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private MedicineMapper medicineMapper;

    //根据cno获得单个Client对象
    private Client getClient(Integer cno){
        return clientMapper.selectClientByCno(cno);
    }
    //获得所有Client对象
    public List<Client> findAll(){
        return clientMapper.selectList(null);
    }

    //对外提供根据cno获得Client对象的方法
    public Client getClientByCno(Integer cno){
        return getClient(cno);
    }

    //根据cno删除对象，并返回是否成功
    @Transactional
    public boolean deleteClient(Integer cno){
        try{
            clientMapper.delete(cno);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    //根据编号批量删除
    @Transactional
    public void deleteBatch(List<Integer> cnoList){
        clientMapper.deleteBatch(cnoList);
    }

    //更新Client信息并返回更新后的对象，更新失败返回null
    @Transactional
    public Client updateClient(Client client){
        Client existingClient=clientMapper.selectClientByCno(client.getCno());
        if(existingClient !=null){

            if(client.getCno()!=null){
                existingClient.setCno(client.getCno());
            }
            if(client.getCname()!=null){
                existingClient.setCname(client.getCname());
            }
            if(client.getCsex()!=null){
                existingClient.setCsex(client.getCsex());
            }
            if(client.getCage()!=null){
                existingClient.setCage(client.getCage());
            }
            if(client.getCphone()!=null){
                existingClient.setCphone(client.getCphone());
            }
            if(client.getCaddress()!=null){
                existingClient.setCaddress(client.getCaddress());
            }
            if(client.getCsymptom()!=null){
                existingClient.setCsymptom(client.getCsymptom());
            }
            if(client.getCdate()!=null){
                existingClient.setCdate(client.getCdate());
            }
            if(client.getCremark()!=null){
                existingClient.setCremark(client.getCremark());
            }
            int rowsUpdated=clientMapper.update(existingClient);
            return rowsUpdated > 0 ? existingClient: null;
        }
        return null;
    }

    //根据多个条件搜索Client对象
    public List<Client> searchClients(Integer cno, String cname, String csex, String cage, String cphone, String caddress, String csymptom, String cdate, String cremark){
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        // 根据传入的参数添加查询条件
        // 返回结果
        if(cno != null){
            queryWrapper.eq("cno", cno);
        }
        if (cname != null && !cname.isEmpty()) {
            queryWrapper.like("cname", cname); // 使用like匹配名称
        }
        if (csex != null && !csex.isEmpty()) {
            queryWrapper.eq("csex", csex);
        }
        if (cage != null && !cage.isEmpty()) {
            queryWrapper.eq("cage", cage);
        }
        if (cphone != null && !cphone.isEmpty()) {
            queryWrapper.eq("cphone", cphone);
        }
        if (caddress != null && !caddress.isEmpty()) {
            queryWrapper.like("caddress", caddress);
        }
        if (csymptom != null && !csymptom.isEmpty()) {
            queryWrapper.like("csymptom", csymptom);
        }
        if (cdate != null && !cdate.isEmpty()) {
            queryWrapper.eq("cdate", cdate);
        }
        if (cremark != null && !cremark.isEmpty()) {
            queryWrapper.like("cremark", cremark);
        }
        return clientMapper.selectList(queryWrapper);
    }

    public boolean existsByCno(Integer cno) {
        return clientMapper.countByMno(cno) > 0;
    }
}
