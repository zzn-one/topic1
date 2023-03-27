package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictYear;
import com.zzn.pojo.Result;
import com.zzn.service.DictYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzn
 * @create 2022-12-27 10:31
 */
@RestController
@RequestMapping("/dict/year")
public class DictYearController {
    @Autowired
    private DictYearService dictYearService;
    @GetMapping()
    public Result<DictYear> getYear(){
        DictYear dictYear = dictYearService.getById(1);
        return new Result<>(HttpStatus.OK.value(), null, dictYear);
    }
}
