package com.zzn.pojo.dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName dict_topic_passed
 */
@TableName(value ="dict_topic_passed")
@Data
public class DictTopicPassed implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer passed;

    /**
     * 
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}