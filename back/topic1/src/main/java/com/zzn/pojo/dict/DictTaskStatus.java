package com.zzn.pojo.dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName dict_task_status
 */
@TableName(value ="dict_task_status")
@Data
public class DictTaskStatus implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer status;

    /**
     * 
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}