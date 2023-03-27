package com.zzn.pojo.user.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzn.pojo.dict.DictCollege;
import com.zzn.pojo.dict.DictGender;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_admin
 */
@TableName(value ="user_admin")
@Data
public class UserAdminWithDict implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员登录账号
     */
    private String aId;

    /**
     * 姓名
     */
    private String aName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String password;

    /**
     * 学院
     */
    private String college;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 最近一次登录系统的时间
     */
    private Date loginTime;

    /**
     * 登录地ip
     */
    private String loginIp;

    /**
     * 身份标识
     */
    private Integer userType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private DictGender dictGender;
    private DictCollege dictCollege;
}