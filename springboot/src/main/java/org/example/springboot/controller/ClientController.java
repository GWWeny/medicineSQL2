package org.example.springboot.controller;


import org.example.springboot.common.Solve;
import org.example.springboot.entity.Client;
import org.example.springboot.service.AgencyService;
import org.example.springboot.service.ClientService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {
    @Resource
    private ClientService clientService;
    @Resource
    private AgencyService agencyService;

    //添加新客户
    @RequestMapping("/add")
    public Solve addClient(@RequestBody Client client) {
        if (clientService.existsByCno(client.getCno())) {
            return Solve.failure("顾客编号已存在");
        }
        if(client.getCname()==null || client.getCname().isEmpty()){
            return Solve.failure("客户姓名不能为空");
        }
        if(client.getCsex()==null || client.getCsex().isEmpty()){
            return Solve.failure("客户性别不能为空");
        }
        if(!client.getCsex().equals("男") && !client.getCsex().equals("女")){
            return Solve.failure("客户性别只能为男或女");
        }
        if(client.getCphone()==null || client.getCphone().isEmpty()){
            return Solve.failure("客户联系方式不能为空");
        }
        if(client.getCaddress()==null || client.getCaddress().isEmpty()){
            return Solve.failure("客户地址不能为空");
        }
        if(client.getCsymptom()==null || client.getCsymptom().isEmpty()){
            return Solve.failure("客户症状不能为空");
        }
        if(client.getCdate()==null || client.getCdate().isEmpty()){
            return Solve.failure("客户日期不能为空");
        }
        if(client.getMno()==null){
            return Solve.failure("客户药品编号不能为空");
        }
        if(client.getAno()==null){
            return Solve.failure("客户经办人不能为空");
        }

        boolean agencyExists=agencyService.existsByAno(client.getAno());
        if(!agencyExists){
            return Solve.failure("经办人编号不存在");
        }
        if(clientService.save(client)){
            return Solve.success(client);
        }else{
            return Solve.failure("添加客户失败");
        }
    }

    //根据编号删除
    @DeleteMapping("/delete/{cno}")
    public Solve deleteClient(@PathVariable Integer cno) {
        if(clientService.deleteClient(cno)){
            return Solve.success("客户"+cno+"已删除成功");
        }else{
            return Solve.failure("删除客户失败");
        }
    }

    //批量删除客户
    @DeleteMapping("/deleteBatch")
    public Solve deleteBatch(@RequestBody List<Integer> cnoList) {
        if(clientService.count()==0){
            return Solve.failure("批量删除失败,数据库没有任何数据");
        }
        clientService.deleteBatch(cnoList);
        return Solve.success("批量删除成功");
    }

    //通过编号找寻顾客
    @GetMapping("/getByCno")
    public Solve getClientByCno(Integer cno) {
        Client client = clientService.getClientByCno(cno);
        if(client!=null){
            return Solve.success(client);
        }else{
            return Solve.failure("客户编号不存在");
        }
    }

    //找寻所有客户
    @GetMapping("/findAll")
    public Solve findAll() {
        List<Client> clients = clientService.findAll();
        if(clients!=null){
            return Solve.success(clients);
        }else{
            return Solve.failure("客户列表为空");
        }
    }


    //自由查询顾客信息
    @GetMapping("/search")
    public Solve searchClients(@RequestParam(required = false)Integer cno, String cname, String csex, String cage, String cphone, String caddress, String csymptom, String cdate, String cremark,Integer mno,Integer ano) {
        List<Client> clients = clientService.searchClients(cno, cname, csex, cage, cphone, caddress, csymptom, cdate, cremark,mno,ano);
        if(clients!=null){
            return Solve.success(clients);
        }else{
            return Solve.failure("未找到符合条件的客户信息");
        }
    }

    //!!!没有插入数据！！！还有删除没看
    //信息修改
    @PutMapping("/update/{cno}")
    public Solve updateClient(@PathVariable("cno") int cno,@RequestBody Client client) {
        client.setCno(cno);
        Client updateClient = clientService.updateClient(client);
        if(updateClient!=null){
            return Solve.success(updateClient);
        }else{
            return Solve.failure("未找到客户进行更新");
        }
    }

    @PutMapping("/reorder")
    public Solve reorderClients(@RequestBody List<Client> clients) {
        // 遍历顾客列表并更新顾客编号
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            // 设置新的顾客编号，假设从 1 开始
            client.setCno(i + 1);
            // 更新顾客信息
            clientService.updateById(client);
        }
        return Solve.success("顾客编号重新排序成功");
    }
}
