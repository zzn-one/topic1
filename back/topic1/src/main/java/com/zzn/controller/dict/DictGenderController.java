package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictGender;
import com.zzn.pojo.Result;
import com.zzn.service.DictGenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/gender")
public class DictGenderController {
    @Autowired
    private DictGenderService dictGenderService;

    @GetMapping()
    public Result<List<DictGender>> getMsg(){
        List<DictGender> list = dictGenderService.list();

        return new Result<>(HttpStatus.OK.value(), null,list);
    }



    @GetMapping("{id}")
    public Result<DictGender> getMsg(@PathVariable("id")String id){

        DictGender dictGender = dictGenderService.getById(id);

        return new Result<>(HttpStatus.OK.value(), null,dictGender);
    }
}
