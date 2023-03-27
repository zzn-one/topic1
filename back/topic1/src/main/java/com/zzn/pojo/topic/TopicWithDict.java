package com.zzn.pojo.topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzn.pojo.dict.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课题
 *
 * @TableName topic
 */
@TableName(value = "topic")
@Data
public class TopicWithDict implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课题名称
     */
    private String topicName;

    /**
     * 命题性质;0:设计 1：论文
     */
    private Integer type;

    /**
     * 课题来源;0:自命题 1：企业 2：教师科研
     */
    private Integer source;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 所属年份
     */
    private String year;

    /**
     * 创建人;命题老师的id
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 审核人;审核该课题的管理员id
     */
    private String checkedBy;

    /**
     * 毕设课题修改建议
     */
    private String checkSuggestions;

    /**
     * 审核时间
     */
    private Date checkedTime;

    /**
     * 审核标记;0：已审核 1：未审核
     */
    private Integer checked;

    /**
     * 课题的审批情况;0：不及格 1：基本及格 2：及格
     */
    private Integer passed;

    /**
     * 课题的拥有者-毕业生
     */
    private String selectedBy;
    /**
     * 课题的拥有者名称
     * */
    private String selectedName;

    /**
     * 是否已被选;0:已被选 1：未被选择
     */
    private Integer selected;

    /**
     * 逻辑删除;0:已被删除 1：未被删除
     */
    private Integer deleted;
    /**
     * 审核员名称
     */
    private String checkedName;
    /**
     * 创建人名称
     */
    private String createdName;
    /**
     * 乐观锁
     */

    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private DictCollege dictCollege;
    private DictMajor dictMajor;
    private DictTopicPassed dictTopicPassed;
    private DictTopicSource dictTopicSource;
    private DictTopicType dictTopicType;
}