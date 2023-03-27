package com.zzn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzn.pojo.Page;
import com.zzn.pojo.topic.Topic;
import com.zzn.pojo.topic.TopicWithDict;
import com.zzn.service.TopicService;
import com.zzn.mapper.TopicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author zzz06
* @description 针对表【topic(课题)】的数据库操作Service实现
* @createDate 2022-12-27 11:56:13
*/
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
    implements TopicService{

    @Resource
    private TopicMapper topicMapper;

    @Override
    public List<TopicWithDict> listByTid(String tId) {
        return topicMapper.listByTid(tId);
    }

    @Override
    public Page<TopicWithDict> pageByCollege(String college, String year ,Integer currentPage,Integer pageSize) {

        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getCollege, college);
        queryWrapper.eq(Topic::getYear, year);

        Long total = topicMapper.selectCount(queryWrapper);

        int start = (currentPage - 1) * pageSize ;
        List<TopicWithDict> list = topicMapper.pageByCollege(college, year, start, pageSize);

        return new Page<>(total, list);
    }

    @Override
    public Page<TopicWithDict> pageByCollegeWithUnCheck(String college, String year,Integer currentPage,Integer pageSize) {
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getCollege, college);
        queryWrapper.eq(Topic::getYear, year);
        queryWrapper.eq(Topic::getChecked, 1);

        Long total = topicMapper.selectCount(queryWrapper);

        int start = (currentPage - 1) * pageSize ;
        List<TopicWithDict> list = topicMapper.pageByCollegeWithUnCheck(college, year, start, pageSize);

        return new Page<>(total, list);
    }

    @Override
    public Page<TopicWithDict> pageByMajorWithChecked(String major, String year,Integer currentPage,Integer pageSize) {
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getMajor, major);
        queryWrapper.eq(Topic::getYear, year);
        queryWrapper.eq(Topic::getChecked, 0);
        queryWrapper.eq(Topic::getSelected, 1);
        queryWrapper.eq(Topic::getPassed, 2);

        Long total = topicMapper.selectCount(queryWrapper);

        int start = (currentPage - 1) * pageSize ;
        List<TopicWithDict> list = topicMapper.pageByMajorWithChecked(major, year, start, pageSize);

        return new Page<>(total, list);
    }


    @Override
    public TopicWithDict getBySid(String sId) {
        return topicMapper.getBySid(sId);
    }
}




