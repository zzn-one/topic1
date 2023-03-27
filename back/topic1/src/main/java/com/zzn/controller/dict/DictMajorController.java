package com.zzn.controller.dict;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzn.pojo.dict.DictMajor;
import com.zzn.pojo.Result;
import com.zzn.service.DictMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/dict/major")
public class DictMajorController {
    @Autowired
    private DictMajorService service;

    @GetMapping("/college/{college}")
    public Result<List<DictMajor>> getMsg(@PathVariable("college") String college) {

        LambdaQueryWrapper<DictMajor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictMajor::getCollege, college);

        List<DictMajor> list = service.list(wrapper);

        return new Result<>(HttpStatus.OK.value(), null, list);
    }

    @GetMapping("{major}")
    public Result<DictMajor> getByMajor(@PathVariable("major") String major) {
        LambdaQueryWrapper<DictMajor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictMajor::getMajor, major);

        DictMajor dictMajor = service.getOne(wrapper);

        if (dictMajor == null) {
            return new Result<>(HttpStatus.OK.value(), "请输入正确的专业代码", null);
        } else {
            return new Result<>(HttpStatus.OK.value(), null, dictMajor);
        }

    }

    @GetMapping()
    public Result<List<DictMajor>> getMsg() {

        List<DictMajor> list = service.list();

        return new Result<>(HttpStatus.OK.value(), null, list);
    }

}
