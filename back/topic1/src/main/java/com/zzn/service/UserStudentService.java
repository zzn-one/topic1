package com.zzn.service;

import com.zzn.pojo.Page;
import com.zzn.pojo.user.student.UserStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzn.pojo.user.student.UserStudentWithDict;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author zzz06
* @description 针对表【user_student】的数据库操作Service
* @createDate 2022-12-27 11:56:13
*/
public interface UserStudentService extends IService<UserStudent> {

    /*
     * 登录
     * */
    String login(String account, String password, HttpServletRequest request);

    /*
     *查询单用户
     * 根据sId
     * */
    UserStudentWithDict getOneBySid(String sid);

    /*
    * 分页条件查询
    * currentPage, pageSize,
    * college,
    * sId,sName, major
    * */

    Page<UserStudentWithDict> pageByCondition(Integer currentPage, Integer pageSize, String college, String sId, String sName, String major);

    /*
     * 查询所有毕业生信息
     * 根据college
     * */
    List<UserStudentWithDict> listByCollege(String college);

    /*
    * 查询毕业生信息中的year列表
    * 根据college
    * */

    List<String> getYearListByCollege(String college);

    /*
     * 条件查询 毕业生信息
     * 根据 college major graduationYear number(查询条数)
     * */
    List<UserStudentWithDict> exportMsgByCondition(String college, String major, String year, Integer number);


}
