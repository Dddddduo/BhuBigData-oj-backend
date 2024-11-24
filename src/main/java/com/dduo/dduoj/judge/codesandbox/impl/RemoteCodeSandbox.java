package com.dduo.dduoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.dduo.dduoj.exception.BusinessException;
import com.dduo.dduoj.judge.codesandbox.CodeSandbox;
import com.dduo.dduoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.dduo.dduoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;
import static com.dduo.dduoj.common.ErrorCode.API_REQUEST_ERROR;

//远程代码沙箱 (实际调用)
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        System.out.println("传过来的请求: "+executeCodeRequest);

        // 替换成代码沙箱接口的路径
        String url = "http://localhost:8090/health";

        String json = JSONUtil.toJsonStr(url);

        String responseStr = HttpUtil.createGet(url)
                .body(json)
                .execute()
                .body();

        if(StringUtils.isBlank(responseStr)){
            throw new BusinessException(API_REQUEST_ERROR, "executeCode remoteSandbox error, message = {}"+responseStr);
        }
        return JSONUtil.toBean(responseStr,ExecuteCodeResponse.class);
    }
}
