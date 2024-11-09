package com.dduo.dduoj.judge;

import com.dduo.dduoj.judge.strategy.DefaultJudgeStrategy;
import com.dduo.dduoj.judge.strategy.JavaLanguageJudgeStrategy;
import com.dduo.dduoj.judge.strategy.JudgeContext;
import com.dduo.dduoj.judge.strategy.JudgeStrategy;
import com.dduo.dduoj.judge.codesandbox.model.JudgeInfo;
import com.dduo.dduoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/*
*  判题管理(简化调用)
* */
@Service
public class JudgeManger {

    /*
     * 执行判题
     * @param judgeContext
     * @return
     * */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
