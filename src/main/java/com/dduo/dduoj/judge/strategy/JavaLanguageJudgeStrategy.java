package com.dduo.dduoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.dduo.dduoj.model.dto.question.JudgeCase;
import com.dduo.dduoj.model.dto.question.JudgeConfig;
import com.dduo.dduoj.judge.codesandbox.model.JudgeInfo;
import com.dduo.dduoj.model.entity.Question;
import com.dduo.dduoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/*
 * Java程序的判题策略
 * */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        // 从上下文对象获取信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaselist();

        Long memory;
        Long time;

        JudgeInfo judgeInfoResponse = new JudgeInfo();

        if(judgeInfo==null){
            // 没有判题信息说明 说明 编译错误 这个时候要避免抛出空指针
            // 直接返回就行
            memory = (long) -1;
            time = (long) -1;
            JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.Compile_Error;
            judgeInfoResponse.setMemoryLimit(memory);
            judgeInfoResponse.setTime(time);
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }else{
            // 从判题信息中获取信息
             memory = judgeInfo.getMemoryLimit();
             time = judgeInfo.getTime();
        }


        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.Accepted;
        judgeInfoResponse.setMemoryLimit(memory);
        judgeInfoResponse.setTime(time);

        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.Wrong_Answer;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.Wrong_Answer;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }

        // 判断题目限制
        // 暂时没有做内存限制的处理
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
//
//        Long needMemoryLimit = judgeConfig.getMemoryLimit();
//        if (memory > needMemoryLimit) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.Memory_Limit_Exceeded;
//            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
//            return judgeInfoResponse;
//        }

        // Java程序本身需要额外执行1秒钟
//        long JAVA_POGRAME_TIME_COST=1000L;
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (time  > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.Time_Limit_Exceeded;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());

        return judgeInfoResponse;
    }
}
