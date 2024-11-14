package com.dduo.dduoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交
 * @TableName question_submit
 */
@TableName(value ="question_submit")
@Data
public class QuestionSubmit implements Serializable {

    // 自动生成的Id
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String language;

    private String code;

    private String judgeInfo;

    private Integer status;

    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}