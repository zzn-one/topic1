package com.zzn.quartz.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzn.pojo.task.Task;
import com.zzn.service.TaskService;
import org.quartz.JobExecutionContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author zzn
 * @create 2022-12-23 22:45
 * 课题任务开始
 */
@Component
public class TopicTaskJobStart extends QuartzJobBean {
    @Resource
    private TaskService taskService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        String taskJson =(String) context.getJobDetail().getJobDataMap().get("task");
        Task task = JSON.parseObject(taskJson, Task.class);
        Integer id = task.getId();


        //修改task的status为1
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Task::getId, id);

        updateWrapper.set(Task::getStatus, 1);

        boolean update = taskService.update(updateWrapper);
        if (update) {
            //把新的task信息存到redis
            LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(Task::getId, id);

            Task newTask = taskService.getOne(queryWrapper);
            String jsonString = JSON.toJSONString(newTask);
            //设置数据过期时间
            Integer duration = newTask.getDuration();

            stringRedisTemplate.opsForValue().set("topicTask:" + id, jsonString, Duration.ofMinutes(duration));
        }
    }
}
