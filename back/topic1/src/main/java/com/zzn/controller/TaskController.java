package com.zzn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzn.config.RabbitMqConfig;
import com.zzn.pojo.Result;
import com.zzn.pojo.task.Task;
import com.zzn.pojo.task.TaskWithDict;
import com.zzn.service.TaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zzn
 * @create 2022-12-19 15:22
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /*
     * 新增
     * */
    @PostMapping()
    public Result<Boolean> addTask(@RequestBody Task task) {
        Result<Boolean> result;

        //新增

        // 新增前
        // 检查相同major、year的task是否已经结束
        result = checkBeforeAdd(task);

        if (result.getData()) {
            boolean save = taskService.save(task);
            if (save) {

                result = new Result<>(HttpStatus.OK.value(), "成功创建并发布任务！", true);
                //发送消息，创建任务
                String id = task.getId().toString();
                //发送task的id到mq
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_TASK_ADD + "." + id, id);

            } else {
                result = new Result<>(HttpStatus.OK.value(), "创建任务失败！", false);
            }
        }

        return result;
    }

    /*
     * 新增task前的检查
     * 检查
     * 1.相同major、year的task是否已经结束
     * */
    private Result<Boolean> checkBeforeAdd(Task task) {

        //相同major、year的task是否已经结束
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Task::getMajor, task.getMajor())
                .eq(Task::getYear, task.getYear())
                .eq(Task::getStatus, 0)
                .or()
                .eq(Task::getMajor, task.getMajor())
                .eq(Task::getYear, task.getYear())
                .eq(Task::getStatus, 1);

        Task taskDb = taskService.getOne(queryWrapper);

        Result<Boolean> result;
        if (taskDb != null) {
            //相同major、year的task未结束 阻止新增
            result = new Result<>(HttpStatus.OK.value(), "该专业的上一个任务尚未结束，无法新增该专业的任务！", false);
        } else {
            //通过检查，允许新增
            result = new Result<>(HttpStatus.OK.value(), null, true);
        }

        return result;
    }


    /*
     * 修改
     * */
    @PutMapping()
    public Result<Boolean> updateTask(@RequestBody Task task) {

        Result<Boolean> result;

        boolean update = taskService.updateById(task);
        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "任务信息已经更新！", true);
            //发送消息，重置任务
            String id = task.getId().toString();
            //发送task的id到
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_TASK_UPDATE + "." + id, id);

        } else {
            result = new Result<>(HttpStatus.OK.value(), "任务信息更新失败！", false);
        }

        return result;
    }


    /*
     *取消任务 根据id
     * */
    @PutMapping("{id}")
    public Result<Boolean> updateStatus(@PathVariable("id") Integer id) {
        Result<Boolean> result;
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Task::getId, id);

        updateWrapper.set(Task::getStatus, 2);

        boolean update = taskService.update(updateWrapper);

        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "任务状态已修改！", true);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "任务状态修改失败！", false);
        }

        return result;
    }


    /*
     *获取任务列表
     * */
    @GetMapping("{college}/{year}")
    public Result<List<TaskWithDict>> getAll(@PathVariable("college") String college, @PathVariable("year") String year) {

        List<TaskWithDict> list = taskService.listByCollegeAndYear(college, year);

        return new Result<>(HttpStatus.OK.value(), null, list);
    }

    /*
     * 获取任务
     * 根据 毕业生的  major graduationYear
     * */
    @GetMapping("running/{major}/{graduationYear}")
    public Result<TaskWithDict> getTask(
            @PathVariable("graduationYear") String graduationYear,
            @PathVariable("major") String major) {

        TaskWithDict task = taskService.getByMajorAndYear(major, graduationYear);

        Result<TaskWithDict> result;
        if (task != null) {
            result = new Result<>(HttpStatus.OK.value(), null, task);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "暂无任务！", null);
        }

        return result;
    }

    /*
     * 获取任务
     * 根据id
     * */
    @GetMapping("{id}")
    public Result<TaskWithDict> getTask(
            @PathVariable("id") Integer id) {

        TaskWithDict task = taskService.getById(id);

        Result<TaskWithDict> result;
        if (task != null) {
            result = new Result<>(HttpStatus.OK.value(), null, task);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "获取任务失败！", null);
        }

        return result;
    }

}
