package com.zzn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzn.pojo.user.student.UserStudentWithDict;
import com.zzn.pojo.user.tutor.TutorStudent;
import com.zzn.pojo.user.tutor.UserTutor;
import com.zzn.pojo.user.tutor.UserTutorWithDict;
import com.zzn.pojo.user.tutor.UserTutorWithExcel;

import java.util.List;

/**
* @author zzz06
* @description 针对表【user_tutor】的数据库操作Mapper
* @createDate 2022-12-27 11:56:13
* @Entity com.zzn.pojo.user.tutor.UserTutor
*/
public interface UserTutorMapper extends BaseMapper<UserTutor> {


    UserTutorWithDict getOneByTidAndPassword(String account, String password);


    UserTutorWithDict getOneByTid(String tId);

    List<UserTutorWithDict> pageByCondition(Integer start, Integer size, String college, String tId, String tutorName, String major);


    List<UserTutorWithDict> listByCollege(String college);

    List<UserTutorWithDict> exportMsgByCondition(String college, String major, Integer number);

    List<TutorStudent> getStudentByTid(String tid);
}




