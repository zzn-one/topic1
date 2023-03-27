package com.zzn.pojo.user.student;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzn.pojo.dict.DictCollege;
import com.zzn.pojo.dict.DictGender;
import com.zzn.pojo.dict.DictMajor;
import lombok.Data;

import java.util.Date;

/**
 * @author zzn
 * @create 2023-02-10 21:58
 */
@Data
@ExcelIgnoreUnannotated
public class UserStudentWithExcel {


    /**
     * 学号
     */
    @ExcelProperty("学号")
    private String sId;

    /**
     * 毕业生姓名
     */
    @ExcelProperty("姓名")
    private String sName;

    /**
     * 性别
     */
    @ExcelProperty("性别")
    private String gender;

    /**
     * 毕业年份
     */
    @ExcelProperty("毕业年份")
    private String graduationYear;

    /**
     * 所属学院
     */
    @ExcelProperty("学院")
    private String college;

    /**
     * 所属专业
     */
    @ExcelProperty("专业")
    private String major;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    private String email;


    /**
     * 毕业生信息更新者id
     */
    private String updatedBy;

    /**
     * 最近一次信息更新的时间
     */
    private Date updatedTime;


}
