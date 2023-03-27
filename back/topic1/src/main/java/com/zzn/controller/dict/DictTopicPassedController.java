package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictTopicPassed;
import com.zzn.pojo.Result;
import com.zzn.service.DictTopicPassedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/topicPassed")
public class DictTopicPassedController {
    @Autowired
    private DictTopicPassedService service;

    @GetMapping()
    public Result<List<DictTopicPassed>> getMsg(){
        List<DictTopicPassed> list = service.list();

        return new Result<>(HttpStatus.OK.value(), null,list);
    }

    @GetMapping("{id}")
    public Result<DictTopicPassed> getMsg(@PathVariable("id")String id){
        DictTopicPassed dictTopicPassed = service.getById(id);

        return new Result<>(HttpStatus.OK.value(), null,dictTopicPassed);
    }
}
