package com.dduo.dduoj.judge.strategy;

import com.dduo.dduoj.model.dto.question.JudgeCase;
import com.dduo.dduoj.judge.codesandbox.model.JudgeInfo;
import com.dduo.dduoj.model.entity.Question;
import com.dduo.dduoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/*
* 上下文 用于定义在策略中传递的参数
* */

@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaselist ;

    private QuestionSubmit questionSubmit;

}
