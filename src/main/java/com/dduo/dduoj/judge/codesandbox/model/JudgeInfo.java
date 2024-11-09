package com.dduo.dduoj.judge.codesandbox.model;

//判题信息

import lombok.Data;

@Data
public class JudgeInfo {
    //程序执行信息
    public String message;

    //内存 kb
    public Long memoryLimit;

    //时间 ms
    public Long time;
}
