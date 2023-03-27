package com.zzn.utils;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * 功能:封装了 Quartz 2.2 动态添加、修改和删除定时任务时间的方法
 *
 * @weakness addJob没有检查时间
 * @date 2017-07-22
 */
public class QuartzJobUtils {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * 功能： 添加一个定时任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务的类类型  eg:TimedMassJob.class
     * @param cron             时间设置 表达式，参考quartz说明文档
     * @param objects          可变参数需要进行传参的值
     */
    public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> jobClass, String cron, Object... objects) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            //   先判断jobName是否已经存在于jobGroupName
            JobDetail existJobDetail = getJobDetail(jobName, jobGroupName);
            if (existJobDetail != null) {
                //JobDetail已经存在，什么都不用做
            }
            //   先判断triggerName是否已经存在于triggerGroupName
            Trigger existTrigger = getTrigger(triggerName, triggerGroupName);
            if (existTrigger != null) {
                //Trigger已经存在，什么都不用做
            } else {
                //JobDetail和Trigger都为null 新增任务

                // 任务名，任务组，任务执行类
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
                // 触发器  设置dataMap
                if (objects != null) {
                    for (int i = 0; i < objects.length; i++) {
                        //该数据可以通过Job中的JobDataMap dataMap = context.getJobDetail().getJobDataMap();来进行参数传递值
                        jobDetail.getJobDataMap().put("data" + i, objects[i]);
                    }
                }
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组

                triggerBuilder.withIdentity(triggerName, triggerGroupName);

                //触发器 启动时机
                triggerBuilder.startNow();

                // 触发器 设定 周期调度器
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                CronTrigger trigger = (CronTrigger) triggerBuilder.build();
                // 调度容器设置JobDetail和Trigger
                scheduler.scheduleJob(jobDetail, trigger);
                // 启动
                if (!scheduler.isShutdown()) {
                    scheduler.start();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能：修改一个任务的触发时间
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     */
    public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器  
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组  
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定  
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能: 移除一个任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器  
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器  
            scheduler.unscheduleJob(triggerKey);
            // 删除任务  
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能：启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能：关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 获取JobDetail
     * */

    public static JobDetail getJobDetail(String jobName, String jobGroupName) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();

        return scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
    }

    /*
    * 获取trigger
    * */
    public static Trigger getTrigger(String triggerName, String triggerGroupName) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();

        return scheduler.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
    }
}