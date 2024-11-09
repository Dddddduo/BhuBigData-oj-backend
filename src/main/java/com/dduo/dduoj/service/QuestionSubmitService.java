package com.dduo.dduoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dduo.dduoj.model.dto.question.QuestionQueryRequest;
import com.dduo.dduoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dduo.dduoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.dduo.dduoj.model.entity.Question;
import com.dduo.dduoj.model.entity.QuestionSubmit;
import com.dduo.dduoj.model.entity.User;
import com.dduo.dduoj.model.vo.QuestionSubmitVO;
import com.dduo.dduoj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author ZDY
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-07-24 17:38:38
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
