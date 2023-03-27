package com.zzn.mapper;

import com.zzn.pojo.user.admin.UserAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzn.pojo.user.admin.UserAdminWithDict;
import org.apache.ibatis.annotations.Param;

/**
* @author zzz06
* @description 针对表【user_admin】的数据库操作Mapper
* @createDate 2022-12-27 11:56:13
* @Entity com.zzn.pojo.user.admin.UserAdmin
*/
public interface UserAdminMapper extends BaseMapper<UserAdmin> {

    UserAdminWithDict getOneByAId(@Param("aId") String aId);

    UserAdminWithDict getOneByAIdAndPassword(@Param("aId") String aId, @Param("password") String password);
}




