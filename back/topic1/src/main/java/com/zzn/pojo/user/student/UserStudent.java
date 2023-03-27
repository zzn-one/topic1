package com.zzn.pojo.user.student;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName user_student
 */
@TableName(value = "user_student")
@Data
public class UserStudent implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    private String sId;

    /**
     * 毕业生姓名
     */
    private String sName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 毕业年份
     */
    private String graduationYear;

    /**
     * 所属学院
     */
    private String college;

    /**
     * 所属专业
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
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除 0：已被删除 1：未被删除
     */
    private Integer deleted;

    /**
     * 毕业生信息更新者id
     */
    private String updatedBy;

    /**
     * 最近一次信息更新的时间
     */
    private Date updatedTime;

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

}