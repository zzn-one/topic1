package com.zzn.pojo.user.tutor;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zzn
 * @create 2023-02-14 11:51
 */
@ExcelIgnoreUnannotated
@Data
public class UserTutorWithExcel {
    @ExcelProperty("工号")
    private String tId;
    @ExcelProperty("姓名")
    private String tutorName;
    @ExcelProperty("职称")
    private String title;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("学院")
    private String college;
    @ExcelProperty("所属系")
    private String major;
    @ExcelProperty("手机号")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("办公地址")
    private String office;

    private String updatedBy;

    private Date updatedTime;
}
