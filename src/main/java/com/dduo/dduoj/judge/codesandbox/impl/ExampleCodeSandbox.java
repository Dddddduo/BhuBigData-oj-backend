package com.dduo.dduoj.judge.codesandbox.impl;

import com.dduo.dduoj.judge.codesandbox.CodeSandbox;
import com.dduo.dduoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.dduo.dduoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.dduo.dduoj.judge.codesandbox.model.JudgeInfo;
import com.dduo.dduoj.model.enums.JudgeInfoMessageEnum;
import com.dduo.dduoj.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

//示例代码沙箱 (仅供测试 跑通业务流程)
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());

        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.Accepted.getText());
        judgeInfo.setMemoryLimit(100L);
        judgeInfo.setTime(100L);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }
}
