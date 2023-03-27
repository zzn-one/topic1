package com.zzn.service;

import com.zzn.pojo.Page;
import com.zzn.pojo.topic.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzn.pojo.topic.TopicWithDict;

import java.util.List;

/**
* @author zzz06
* @description 针对表【topic(课题)】的数据库操作Service
* @createDate 2022-12-27 11:56:13
*/
public interface TopicService extends IService<Topic> {

    /*
    * 根据tId查询课题列表
    * */
    List<TopicWithDict> listByTid(String tId);

    /*
    * 查询所有课题
    * 根据college year 分页
    * */
    Page<TopicWithDict> pageByCollege(String college,String year,Integer currentPage,Integer pageSize);

    /*
    * 查询所有未审核的课题
    * 根据college year 分页
    * */
    Page<TopicWithDict> pageByCollegeWithUnCheck(String college,String year,Integer currentPage,Integer pageSize);

    /*
     * 查询所有已经审核的课题
     * 根据major year 分页
     * */
    Page<TopicWithDict> pageByMajorWithChecked(String major,String year,Integer currentPage,Integer pageSize);

    /*
     * 查询单课题
     * 根据sId
     * */
    TopicWithDict getBySid(String sId);

}
