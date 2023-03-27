package com.zzn.service;

import com.zzn.pojo.task.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzn.pojo.task.TaskWithDict;

import java.util.List;

/**
* @author zzz06
* @description 针对表【task】的数据库操作Service
* @createDate 2022-12-27 11:56:13
*/
public interface TaskService extends IService<Task> {


    /*
     * 查询任务
     * 根据college、year
     * */
    List<TaskWithDict> listByCollegeAndYear(String college,String year);

    /*
     * 查询单个任务
     * 根据 major 、year
     * */
    TaskWithDict getByMajorAndYear(String major,String year);

    /*
     * 查询单个任务
     * 根据 major 、year
     * */
    TaskWithDict getById(Integer id);
}
