package com.zzn.pojo.user.tutor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzn.pojo.dict.DictCollege;
import com.zzn.pojo.dict.DictGender;
import com.zzn.pojo.dict.DictMajor;
import com.zzn.pojo.dict.DictTutorTitle;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_tutor
 */
@TableName(value ="user_tutor")
@Data
public class UserTutorWithDict implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 指导老师的登录账号
     */
    private String tId;

    /**
     * 姓名
     */
    private String tutorName;

    /**
     * 职称
     */
    private Integer title;

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
     * 系
     */
    private String major;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 办公室
     */
    private String office;

    /**
     * 指导老师信息更新者id
     */
    private String updatedBy;

    /**
     * 信息更新时间
     */
    private Date updatedTime;

    /**
     * 最近一次登录系统的时间
     */
    private Date loginTime;

    /**
     * 登录地IP
     */
    private String loginIp;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    /**
     * 身份标识
     */
    private Integer userType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private DictGender dictGender;
    private DictCollege dictCollege;
    private DictMajor dictMajor;
    private DictTutorTitle dictTutorTitle;
}