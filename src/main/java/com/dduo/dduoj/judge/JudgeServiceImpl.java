package com.dduo.dduoj.judge;

import cn.hutool.json.JSONUtil;
import com.dduo.dduoj.common.ErrorCode;
import com.dduo.dduoj.exception.BusinessException;
import com.dduo.dduoj.judge.codesandbox.CodeSandbox;
import com.dduo.dduoj.judge.codesandbox.CodeSandboxFactory;
import com.dduo.dduoj.judge.codesandbox.CodeSandboxProxy;
import com.dduo.dduoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.dduo.dduoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.dduo.dduoj.judge.strategy.JudgeContext;
import com.dduo.dduoj.model.dto.question.JudgeCase;
import com.dduo.dduoj.judge.codesandbox.model.JudgeInfo;
import com.dduo.dduoj.model.entity.Question;
import com.dduo.dduoj.model.entity.QuestionSubmit;
import com.dduo.dduoj.model.enums.QuestionSubmitStatusEnum;
import com.dduo.dduoj.service.QuestionService;
import com.dduo.dduoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    // 题目服务
    @Resource
    private QuestionService questionService;

    // 题目提交服务
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type:example}")
    private String value;

    @Resource
    private JudgeManger judgeManger;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {

        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }

        //拿到题目提交信息
        Long questionId = questionSubmit.getQuestionId();

        //拿到题目
        Question question = questionService.getById(questionId);

        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 题目存在
        // 开始判题
        // 更改题目的状态 status

        // 如果不为等待状态
        if (questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        // 重新设置
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean judge = questionSubmitService.updateById(questionSubmitUpdate);

        if (!judge) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 接下来放代码沙箱
        CodeSandbox codeSandbox = CodeSandboxFactory.NewInstance(value);
        codeSandbox = new CodeSandboxProxy(codeSandbox);

        // 拿出数据
        String code = questionSubmit.getCode();

        String language = questionSubmit.getLanguage();

        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaselist = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaselist.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeRequest = ExecuteCodeRequest.builder().code(code).language(language).inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse=codeSandbox.executeCode(executeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();

        // 根据沙箱的执行结果 设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaselist(judgeCaselist);

        // 用策略去模式解决
        JudgeInfo judgeInfo = judgeManger.doJudge(judgeContext);

        // 修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));

        // 看数据库
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);

        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 返回结果
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
