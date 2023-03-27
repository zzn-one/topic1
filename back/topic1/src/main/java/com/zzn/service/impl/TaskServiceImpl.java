package com.zzn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzn.pojo.task.Task;
import com.zzn.pojo.task.TaskWithDict;
import com.zzn.service.TaskService;
import com.zzn.mapper.TaskMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author zzz06
* @description 针对表【task】的数据库操作Service实现
* @createDate 2022-12-27 11:56:13
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService{

    @Resource
    private TaskMapper taskMapper;


    @Override
    public List<TaskWithDict> listByCollegeAndYear(String college, String year) {
        return taskMapper.listByCollegeAndYear(college, year);
    }

    @Override
    public TaskWithDict getByMajorAndYear(String major, String year) {
        return taskMapper.getByMajorAndYear(major, year);
    }

    @Override
    public TaskWithDict getById(Integer id) {
        return taskMapper.getById(id);
    }
}




