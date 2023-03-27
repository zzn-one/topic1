package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictTopicType;
import com.zzn.pojo.Result;
import com.zzn.service.DictTopicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/topicType")
public class DictTopicTypeController {
    @Autowired
    private DictTopicTypeService service;

    @GetMapping()
    public Result<List<DictTopicType>> getMsg(){
        List<DictTopicType> list = service.list();

        return new Result<>(HttpStatus.OK.value(), null,list);
    }

    @GetMapping("{id}")
    public Result<DictTopicType> getMsg(@PathVariable("id")String id){
        DictTopicType dictTopicType = service.getById(id);

        return new Result<>(HttpStatus.OK.value(), null,dictTopicType);
    }
}
