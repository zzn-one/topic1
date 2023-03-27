package com.zzn.mapper;

import com.zzn.pojo.topic.Topic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzn.pojo.topic.TopicWithDict;

import java.util.List;

/**
* @author zzz06
* @description 针对表【topic(课题)】的数据库操作Mapper
* @createDate 2022-12-27 11:56:13
* @Entity com.zzn.pojo.topic.Topic
*/
public interface TopicMapper extends BaseMapper<Topic> {

    /*
     * 根据tId查询课题列表
     * */
    List<TopicWithDict> listByTid(String tId);

    /*
     * 查询所有课题
     * 根据college year
     * */
    List<TopicWithDict> pageByCollege(String college, String year,Integer start,Integer size);

    /*
     * 查询所有未审核的课题
     * 根据college year 分页
     * */
    List<TopicWithDict> pageByCollegeWithUnCheck(String college,String year,Integer start,Integer size);

    /*
     * 查询所有已经审核的课题
     * 根据major year 分页
     * */
    List<TopicWithDict> pageByMajorWithChecked(String major,String year,Integer start,Integer size);

    /*
     * 查询单课题
     * 根据sId
     * */
    TopicWithDict getBySid(String sId);


}




