package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictTutorTitle;
import com.zzn.pojo.Result;
import com.zzn.service.DictTutorTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/tutorTitle")
public class DictTutorTitleController {
    @Autowired
    private DictTutorTitleService service;

    @GetMapping()
    public Result<List<DictTutorTitle>> getMsg( ){

        List<DictTutorTitle> list = service.list();

        return new Result<>(HttpStatus.OK.value(), null,list);
    }

    @GetMapping("{id}")
    public Result<DictTutorTitle> getMsg(@PathVariable("id")String id){

        DictTutorTitle dictTutorTitle = service.getById(id);

        return new Result<>(HttpStatus.OK.value(), null,dictTutorTitle);
    }
}
