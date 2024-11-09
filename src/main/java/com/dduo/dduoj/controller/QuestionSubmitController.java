package com.dduo.dduoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dduo.dduoj.annotation.AuthCheck;
import com.dduo.dduoj.common.BaseResponse;
import com.dduo.dduoj.common.ErrorCode;
import com.dduo.dduoj.common.ResultUtils;
import com.dduo.dduoj.constant.UserConstant;
import com.dduo.dduoj.exception.BusinessException;
import com.dduo.dduoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.dduo.dduoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.dduo.dduoj.model.entity.QuestionSubmit;
import com.dduo.dduoj.model.entity.User;
import com.dduo.dduoj.model.vo.QuestionSubmitVO;
import com.dduo.dduoj.service.QuestionSubmitService;
import com.dduo.dduoj.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@CrossOrigin
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

//    @Resource
//    private QuestionQueryRequest questionQueryRequest;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交（除了管理员外 普通用户是能看见非答案 提交代码等非公开信息）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        //从数据库中获取信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        //返回脱敏方法
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }



}
