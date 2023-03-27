package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictCollege;
import com.zzn.pojo.Result;
import com.zzn.service.DictCollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/college")
public class DictCollegeController  {
    @Autowired
    private DictCollegeService dictCollegeService;

    @GetMapping()
    public Result<List<DictCollege>> getCollege(){
        List<DictCollege> collegeList = dictCollegeService.list();

        return new Result<>(HttpStatus.OK.value(), null,collegeList);
    }

    @GetMapping("{id}")
    public Result<DictCollege> getCollege(@PathVariable("id")String id){
        DictCollege dictCollege = dictCollegeService.getById(id);

        return new Result<>(HttpStatus.OK.value(), null,dictCollege);
    }
}
