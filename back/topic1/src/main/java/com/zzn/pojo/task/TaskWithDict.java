package com.zzn.pojo.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzn.pojo.dict.DictCollege;
import com.zzn.pojo.dict.DictMajor;
import com.zzn.pojo.dict.DictTaskStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName task
 */
@TableName(value ="task")
@Data
public class TaskWithDict implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 毕业年份
     */
    private String year;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务时长
     */
    private Integer duration;

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 任务发布者aId
     */
    private String publishBy;
    /*
     * 任务发布者名称
     *
     * */
    private String publishName;
    /**
     * 任务发布时间
     */
    private Date publishTime;

    /**
     * 乐观锁
     */
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private DictCollege dictCollege;

    private DictMajor dictMajor;

    private DictTaskStatus dictTaskStatus;
}