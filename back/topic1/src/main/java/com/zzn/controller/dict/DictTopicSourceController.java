package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictTopicSource;
import com.zzn.pojo.Result;
import com.zzn.service.DictTopicSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/topicSource")
public class DictTopicSourceController {
    @Autowired
    private DictTopicSourceService service;

    @GetMapping()
    public Result<List<DictTopicSource>> getMsg(){
        List<DictTopicSource> list = service.list();

        return new Result<>(HttpStatus.OK.value(), null,list);
    }

    @GetMapping("{id}")
    public Result<DictTopicSource> getMsg(@PathVariable("id")String id){

        DictTopicSource topicSource = service.getById(id);

        return new Result<>(HttpStatus.OK.value(), null,topicSource);
    }
}
