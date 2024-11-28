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

        // 用传入的questionId 去数据库中查询题目的完整信息
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);

        // 没有查询出题目
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 题目存在 开始判题
        // 更新题目的状态为判题中

        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean judge = questionSubmitService.updateById(questionSubmitUpdate);

        if (!judge) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 题目状态已经更新为判题中 接下来调代码沙箱进行下一步处理

        // 这个value是写在配置文件中的 设计模式是:静态工厂模式
        CodeSandbox codeSandbox = CodeSandboxFactory.NewInstance(value);
        // 设计模式是:代理模式
        codeSandbox = new CodeSandboxProxy(codeSandbox);

        // 根据提交的题目拿到 代码
        String code = questionSubmit.getCode();

        // 根据提交的题目拿到 语言
        String language = questionSubmit.getLanguage();

        // 获取输入输出用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaselist = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        // 输入用例
        List<String> inputList = judgeCaselist.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeRequest = ExecuteCodeRequest.builder().code(code).language(language).inputList(inputList).build();

        // 调用代码沙箱获取结果
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

        // 更新题目提交数据状态数据库
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 更新题目的获取总提交数
        Integer submitNum = question.getSubmitNum();
        submitNum++;
        question.setSubmitNum(submitNum);

        // 更新题目的通过
        if(judgeInfo.message.equals("Accepted")){
            Integer acceptedNum = question.getAcceptedNum();
            acceptedNum+=1;
            question.setAcceptedNum(acceptedNum);
        }

        // 更新题目数据状态数据库
        boolean questionJudge = questionService.updateById(question);
        if (!questionJudge) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目更新错误");
        }

        // 返回结果
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
