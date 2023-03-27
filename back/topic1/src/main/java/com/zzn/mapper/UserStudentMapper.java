package com.zzn.mapper;

import com.zzn.pojo.user.student.UserStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzn.pojo.user.student.UserStudentWithDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zzz06
* @description 针对表【user_student】的数据库操作Mapper
* @createDate 2022-12-27 11:56:13
* @Entity com.zzn.pojo.user.student.UserStudent
*/
public interface UserStudentMapper extends BaseMapper<UserStudent> {

    UserStudentWithDict getOneBySidAndPassword(@Param("sId") String account,@Param("password") String password);

    UserStudentWithDict getOneBySid(@Param("sId") String sId);

    List<UserStudentWithDict> pageByCondition(Integer start, Integer size, String college, String sId, String sName, String major);

    List<UserStudentWithDict> listByCollege(String college);

    List<String> getYearListByCollege(String college);

    List<UserStudentWithDict> exportMsgByCondition(String college, String major, String year, Integer number);


}




