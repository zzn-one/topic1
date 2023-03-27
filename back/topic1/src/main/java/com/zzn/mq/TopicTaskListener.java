package com.zzn.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzn.pojo.task.Task;
import com.zzn.quartz.job.TopicTaskJobEnd;
import com.zzn.quartz.job.TopicTaskJobStart;
import com.zzn.service.TaskService;
import com.zzn.utils.CompareFieldsUtils;
import com.zzn.utils.QuartzCronDateUtils;
import org.quartz.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author zzn
 * @create 2023-02-01 21:34
 */
@Component
public class TopicTaskListener {

    @Resource
    private Scheduler scheduler;

    @Autowired
    private TaskService taskService;


    @RabbitListener(queues = "topic1_queue_task_add")
    public void addTask(Message message) throws SchedulerException {
        int id = Integer.parseInt(new String(message.getBody()));

        //根据id查询出task
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getId, id);

        Task task = taskService.getOne(queryWrapper);

        createTopicTask(task);

    }
    @RabbitListener(queues = "topic1_queue_task_update")
    public void updateTask(Message message) throws SchedulerException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        int id = Integer.parseInt(new String(message.getBody()));

        //根据id查询出task
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getId, id);

        Task task = taskService.getOne(queryWrapper);

        updateTopicTask(task);

    }


    private void createTopicTask(Task task) throws SchedulerException {
        //为task设置定时任务
        String jobName;
        String jobGroupName = "topicTaskGroup";
        String triggerGroupName = "taskTriggerGroup";
        String cron;
        String triggerName;

        String endCron;
        String endTriggerName;
        String endJobName;

        jobName = "taskStart" + task.getId();
        triggerName = "taskTrigger" + task.getId();
        endJobName = "taskEnd" + task.getId();
        endTriggerName = "taskEndTrigger" + task.getId();

        String taskJson = JSON.toJSONString(task);
        cron = QuartzCronDateUtils.getCron(task.getStartTime());

        addTopicStartTask(jobName, jobGroupName, triggerGroupName, cron, triggerName, taskJson);


        Date endDate = new Date(task.getStartTime().getTime() + task.getDuration() * 60 * 1000);
        endCron = QuartzCronDateUtils.getCron(endDate);

        addTopicEndTask(jobGroupName, triggerGroupName, endCron, endTriggerName, endJobName, taskJson);
    }

    private void addTopicEndTask(String jobGroupName, String triggerGroupName, String endCron, String endTriggerName, String endJobName, String taskJson) throws SchedulerException {
        //添加定时任务（结束）
        JobDetail endJobDetail = JobBuilder.newJob(TopicTaskJobEnd.class)
                .withIdentity(endJobName, jobGroupName)
                .usingJobData("task", taskJson)
                .build();


        ScheduleBuilder<CronTrigger> endScheduleBuilder = CronScheduleBuilder.cronSchedule(endCron);
        CronTrigger endTrigger = TriggerBuilder.newTrigger()
                .withIdentity(endTriggerName, triggerGroupName)
                .withSchedule(endScheduleBuilder)
                .build();

        scheduler.scheduleJob(endJobDetail, endTrigger);
    }

    private void addTopicStartTask(String jobName, String jobGroupName, String triggerGroupName, String cron, String triggerName, String taskJson) throws SchedulerException {
        //添加定时任务 (开始)
        JobDetail jobDetail = JobBuilder.newJob(TopicTaskJobStart.class)
                .withIdentity(jobName, jobGroupName)
                .usingJobData("task", taskJson)
                .build();


        ScheduleBuilder<CronTrigger> scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroupName)
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    private void updateTopicTask(Task task) throws SchedulerException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        //为task设置定时任务
        String jobName;
        String jobGroupName = "topicTaskGroup";
        String triggerGroupName = "taskTriggerGroup";
        String cron;
        String triggerName;

        String endCron;
        String endTriggerName;
        String endJobName;

        jobName = "taskStart" + task.getId();
        triggerName = "taskTrigger" + task.getId();
        endJobName = "taskEnd" + task.getId();
        endTriggerName = "taskEndTrigger" + task.getId();

        // 检查是否有该task的任务 有则比对两个task的信息，若不相同 重新设置任务
        boolean exists = scheduler.checkExists(JobKey.jobKey(jobName, jobGroupName));
        if (exists) {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
            String oldTaskJson = (String) jobDetail.getJobDataMap().get("task");
            Task oldTask = JSON.parseObject(oldTaskJson, Task.class);

            boolean compareFields = CompareFieldsUtils.compareFields(oldTask, task, null);
            if (!compareFields) {
                //信息不相同
                //移除开始任务
                scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
                // 移除结束任务
                scheduler.deleteJob(JobKey.jobKey(endJobName, jobGroupName));
            }
        }

        //添加定时任务 (开始)
        String taskJson = JSON.toJSONString(task);
        cron = QuartzCronDateUtils.getCron(task.getStartTime());

        addTopicStartTask(jobName, jobGroupName, triggerGroupName, cron, triggerName, taskJson);

        //添加定时任务（结束）
        Date endDate = new Date(task.getStartTime().getTime() + task.getDuration() * 60 * 1000);
        endCron = QuartzCronDateUtils.getCron(endDate);

        addTopicEndTask(jobGroupName, triggerGroupName, endCron, endTriggerName, endJobName, taskJson);

    }


}
