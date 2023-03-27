package com.zzn.pojo.dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName dict_major
 */
@TableName(value ="dict_major")
@Data
public class DictMajor implements Serializable {
    /**
     * 
     */
    @TableId
    private String major;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String college;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}