package com.zzn.service;

import com.zzn.pojo.user.admin.UserAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzn.pojo.user.admin.UserAdminWithDict;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
* @author zzz06
* @description 针对表【user_admin】的数据库操作Service
* @createDate 2022-12-27 11:56:13
*/
public interface UserAdminService extends IService<UserAdmin> {

    /*
    * 根据aId查询单个用户
    * */
    UserAdminWithDict getOneByAid(String aid);

    /*
     * 登录业务
     * */
    String login(String aid ,String password, HttpServletRequest request);

    /*
     * 修改密码
     * */
    Boolean updatePassword(String aid,String newPass);

    /*
     * 修改用户信息
     * */
    Boolean updateMsg(HashMap<String, String> userMap);

}
