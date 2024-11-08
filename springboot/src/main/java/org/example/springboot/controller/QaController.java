package org.example.springboot.controller;

//练习
import org.example.springboot.mapper.QaMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.example.springboot.entity.Qa;
import javax.annotation.Resource;
import java.util.List;

//json格式返回前台
@RestController
public class QaController {
    //初始化注册工具
    @Resource
    QaMapper qaMapper;

    @GetMapping("/qa")
    public List<Qa> qaGet()
    {
        return qaMapper.findAll();
    }
    @PostMapping("/qa")
    public String qaPost(@RequestBody Qa qa)
    {
        qaMapper.add(qa);
        return "success";
    }
}
