package com.zzn.controller.dict;

import com.zzn.pojo.dict.DictTaskStatus;
import com.zzn.pojo.Result;
import com.zzn.service.DictTaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zzn
 * @create 2022-12-19 19:38
 */
@RestController
@RequestMapping("/dict/taskStatus")
public class DictTaskStatusController {
    @Autowired
    private DictTaskStatusService dictTaskStatusService;

    @GetMapping()
    public Result<List<DictTaskStatus>> getTaskList() {
        List<DictTaskStatus> statusList = dictTaskStatusService.list();
        return new Result<>(HttpStatus.OK.value(), null, statusList);
    }


    @GetMapping("{status}")
    public Result<DictTaskStatus> getTaskList(@PathVariable("status") String status) {
        DictTaskStatus dictTaskStatus = dictTaskStatusService.getById(status);
        return new Result<>(HttpStatus.OK.value(), null, dictTaskStatus);
    }
}
