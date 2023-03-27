package com.zzn.pojo.dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName dict_tutor_title
 */
@TableName(value ="dict_tutor_title")
@Data
public class DictTutorTitle implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer title;

    /**
     * 
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}