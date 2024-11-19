package com.dduo.dduoj.judge;


import com.dduo.dduoj.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param
     * @return
     */

    QuestionSubmit doJudge(Long questionSubmitId);


}
