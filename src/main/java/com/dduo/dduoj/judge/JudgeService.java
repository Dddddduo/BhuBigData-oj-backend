package com.dduo.dduoj.judge;


import com.dduo.dduoj.model.entity.QuestionSubmit;
import com.dduo.dduoj.model.vo.QuestionSubmitVO;

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
