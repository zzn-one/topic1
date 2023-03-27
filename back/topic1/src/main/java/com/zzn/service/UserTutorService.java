package com.zzn.service;

import com.zzn.pojo.Page;
import com.zzn.pojo.user.student.UserStudentWithDict;
import com.zzn.pojo.user.tutor.TutorStudent;
import com.zzn.pojo.user.tutor.UserTutor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzn.pojo.user.tutor.UserTutorWithDict;
import com.zzn.pojo.user.tutor.UserTutorWithExcel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author zzz06
* @description 针对表【user_tutor】的数据库操作Service
* @createDate 2022-12-27 11:56:13
*/
public interface UserTutorService extends IService<UserTutor> {

    /*
     * 登录
     * */
    String login(String account, String password, HttpServletRequest request);

    /*
     * 查询单用户
     * tId
     * */
    UserTutorWithDict getOneByTid(String tId);
    /*
    * 分页查询
    * college tId tutorName major
    * */

    Page<UserTutorWithDict> getByConditionAndPage(Integer currentPage, Integer pageSize, String college, String tId, String tutorName, String major);


    /*
     * 查询所有指导老师信息
     * 根据college
     * */
    List<UserTutorWithDict> listByCollege(String college);


    /*
     * 条件查询 指导老师信息
     * 根据 college major graduationYear number(查询条数)
     * */
    List<UserTutorWithDict> exportMsgByCondition(String college, String major, Integer number);

    /*
     * 查询 已经选择课题的毕业生信息
     * 根据tid
     * */
    List<TutorStudent> getStudentByTid(String tid);
}
