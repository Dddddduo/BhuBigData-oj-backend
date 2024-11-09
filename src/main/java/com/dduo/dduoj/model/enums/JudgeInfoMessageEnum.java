package com.dduo.dduoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息枚举
 */
public enum JudgeInfoMessageEnum {

    Accepted("Accepted", "Accepted"),
    Wrong_Answer("Wrong Answer", "Wrong Answer"),
    Compile_Error("Compile Error", "Compile Error"),
    Memory_Limit_Exceeded("Memory Limit Exceeded", "Memory Limit Exceeded"),
    Time_Limit_Exceeded("Time Limit Exceeded", "Time Limit Exceeded"),
    Presentation_Error("Presentation Error", "Presentation Error"),
    Output_Limit_Exceeded("Output Limit Exceeded", "Output Limit Exceeded"),
    Waiting("Waiting", "Waiting"),
    Dangerous_Operation("Dangerous Operation", "Dangerous Operation"),
    Runtime_error("Runtime error", "Runtime error"),
    System_error("System error", "System error");

    private final String text;

    private final String value;
    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
    }
