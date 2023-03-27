package com.zzn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzn.pojo.Page;
import com.zzn.pojo.Result;
import com.zzn.pojo.topic.Topic;
import com.zzn.pojo.topic.TopicWithDict;
import com.zzn.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author zzn
 * @create 2022-12-25 14:52
 */
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    //==================================单表=====================================================
    /*
     * 新增topic
     * */
    @PostMapping()
    public Result<Boolean> addTopic(@RequestBody Topic topic) {
        //判断该topic的topicName是否已被占用
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getTopicName, topic.getTopicName());
        Topic topicDb = topicService.getOne(queryWrapper);
        if (topicDb != null) {
            //topicName已经被占用
            return new Result<>(HttpStatus.OK.value(), "该课题已经存在,请重新输入！", false);
        }

        //判断该tId的课题数是否少于10个
        LambdaQueryWrapper<Topic> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(Topic::getCreatedBy, topic.getCreatedBy());
        long topicCount = topicService.count(countWrapper);
        if (topicCount >= 10) {
            //tId课题数量达到上限
            return new Result<>(HttpStatus.OK.value(), "您的课题数量已经达到上限（10个），无法继续提交毕设课题！", false);
        }

        //新增课题
        boolean save = topicService.save(topic);
        if (save) {
            //新增成功
            return new Result<>(HttpStatus.OK.value(), null, true);
        } else {
            //新增失败
            return new Result<>(HttpStatus.OK.value(), "课题新增失败！请重新提交", false);
        }

    }

    /*
     * 修改topic的审核结果
     * */
    @PutMapping("/check")
    public Result<Boolean> checkTopic(@RequestBody Topic topic) {

        LambdaUpdateWrapper<Topic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Topic::getId, topic.getId());

        updateWrapper.set(Topic::getChecked, 0);

        updateWrapper.set(Topic::getCheckedBy, topic.getCheckedBy());
        updateWrapper.set(Topic::getCheckedTime, topic.getCheckedTime());
        updateWrapper.set(Topic::getCheckedName, topic.getCheckedName());
        updateWrapper.set(Topic::getPassed, topic.getPassed());
        updateWrapper.set(Topic::getCheckSuggestions, topic.getCheckSuggestions());

        boolean update = topicService.update(updateWrapper);
        Result<Boolean> result;
        if (update) {
            result = new Result<>(HttpStatus.OK.value(), null, true);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "课题审核失败！请重试", false);
        }
        return result;
    }

    /*
     * 选择课题
     * */
    @PutMapping("/selectTopic")
    public Result<String> selectTopic( String topicName,  String selectedBy,  String selectedName) {
        Result<String> result;
        //查询topicName是否已经被选择
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Topic::getTopicName, topicName);
        Topic topicDb = topicService.getOne(queryWrapper);
        if (topicDb.getSelected() == 1) {
            //没被选
            LambdaUpdateWrapper<Topic> updateWrapper = new LambdaUpdateWrapper<>();

            updateWrapper.eq(Topic::getTopicName, topicName);

            updateWrapper.setSql("selected = selected - 1").gt(Topic::getSelected, 0);
            updateWrapper.set(Topic::getSelectedBy, selectedBy);
            updateWrapper.set(Topic::getSelectedName, selectedName);

            boolean update = topicService.update(updateWrapper);
            if (update) {
                result = new Result<>(200, null, topicName);
            } else {
                result = new Result<>(2001, "选择课题失败！", null);
            }
        } else {
            result = new Result<>(2002, "该课题：" + topicName + " 已经被选择，请选择其他课题！", null);
        }

        return result;
    }

    /*
     * 选择课题
     * */
    @PutMapping("/resetTopic")
    public Result<String> resetTopic( String oldTopic, String newTopic, String selectedBy,  String selectedName) {
        Result<String> result;
        //查询newTopic是否已经被选择
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Topic::getTopicName, newTopic);
        Topic topicDb = topicService.getOne(queryWrapper);
        if (topicDb.getSelected() == 1) {
            //没被选

            //返还旧课题
            LambdaUpdateWrapper<Topic> updateWrapper2 = new LambdaUpdateWrapper<>();
            updateWrapper2.eq(Topic::getTopicName, oldTopic);

            updateWrapper2.set(Topic::getSelected, 1);
            updateWrapper2.set(Topic::getSelectedBy, null);
            updateWrapper2.set(Topic::getSelectedName, null);
            topicService.update(updateWrapper2);

            //选择新课题

            LambdaUpdateWrapper<Topic> updateWrapper = new LambdaUpdateWrapper<>();

            updateWrapper.eq(Topic::getTopicName, newTopic);

            updateWrapper.setSql("selected = selected - 1").gt(Topic::getSelected, 0);
            updateWrapper.set(Topic::getSelectedBy, selectedBy);
            updateWrapper.set(Topic::getSelectedName, selectedName);

            boolean update = topicService.update(updateWrapper);
            if (update) {
                result = new Result<>(200, null, newTopic);
            } else {
                result = new Result<>(2001, "选择课题失败！", null);
            }
        } else {
            result = new Result<>(2002, "该课题：" + newTopic + " 已经被选择，请选择其他课题！", null);
        }

        return result;
    }

    //==================================多表=======================================================
    /*
     * 根据tId
     * 查询topic
     *
     * */
    @GetMapping("{tId}")
    public Result<List<TopicWithDict>> getTopicByTId(@PathVariable("tId") String tId) {
        List<TopicWithDict> list = topicService.listByTid(tId);

        return new Result<>(HttpStatus.OK.value(), null, list);
    }

    /*
     * 分页查询topic
     *根据college
     * */
    @GetMapping("{currentPage}/{pageSize}/{college}/{year}")
    public Result<Page<TopicWithDict>> pageByCollege(
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("college") String college,
            @PathVariable("year") String year
    ) {

        Page<TopicWithDict> page = topicService.pageByCollege(college, year, currentPage, pageSize);

        return new Result<>(HttpStatus.OK.value(), null, page);
    }

    /*
     * 分页查询topic（未审核）
     *根据college
     * */
    @GetMapping("/unCheck/{currentPage}/{pageSize}/{college}/{year}")
    public Result<Page<TopicWithDict>> unCheckPageByCollege(
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("college") String college,
            @PathVariable("year") String year
    ) {

        Page<TopicWithDict> page = topicService.pageByCollegeWithUnCheck(college, year, currentPage, pageSize);

        return new Result<>(HttpStatus.OK.value(), null, page);
    }

    /*
     * 分页查询topic
     *根据major
     * */
    @GetMapping("/checked/{currentPage}/{pageSize}/{major}/{year}")
    public Result<Page<TopicWithDict>> unCheckPageByMajor(
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("major") String major,
            @PathVariable("year") String year) {

        Page<TopicWithDict> page = topicService.pageByMajorWithChecked(major, year, currentPage, pageSize);
        return new Result<>(HttpStatus.OK.value(), null, page);
    }

    /*
     * 查询topic
     * 根据sId
     * */
    @GetMapping("/selected/{sId}")
    public Result<TopicWithDict> getBySid(@PathVariable("sId") String sId) {
        TopicWithDict topic = topicService.getBySid(sId);

        return new Result<>(HttpStatus.OK.value(), null, topic);
    }


}
