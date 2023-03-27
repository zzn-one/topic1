package com.zzn.quartz.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzn.pojo.task.Task;
import com.zzn.service.TaskService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zzn
 * @create 2022-12-24 13:44
 * 课题任务结束
 */
@Component
public class TopicTaskJobEnd extends QuartzJobBean {
    @Resource
    private TaskService taskService;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        //设置status为3

        String taskJson =(String) context.getJobDetail().getJobDataMap().get("task");
        Task task = JSON.parseObject(taskJson, Task.class);
        Integer id = task.getId();

        //修改task的status为3
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Task::getId, id);

        updateWrapper.set(Task::getStatus, 3);

        taskService.update(updateWrapper);
    }
}
