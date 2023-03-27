package com.zzn.mapper;

import com.zzn.pojo.task.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzn.pojo.task.TaskWithDict;

import java.util.List;

/**
* @author zzz06
* @description 针对表【task】的数据库操作Mapper
* @createDate 2022-12-27 11:56:13
* @Entity com.zzn.pojo.task.Task
*/
public interface TaskMapper extends BaseMapper<Task> {


    /*
     * 查询当前任务
     * 根据college、year
     * */
    List<TaskWithDict> listByCollegeAndYear(String college,String year);

    /*
     * 查询单个任务
     * 根据 major 、year
     * */
    TaskWithDict getByMajorAndYear(String major,String year);

    TaskWithDict getById(Integer id);
}




