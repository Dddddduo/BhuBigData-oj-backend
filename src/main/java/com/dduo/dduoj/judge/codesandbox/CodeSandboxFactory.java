package com.dduo.dduoj.judge.codesandbox;


import com.dduo.dduoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.dduo.dduoj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.dduo.dduoj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

//代码沙箱工厂 根据字符串参数 创建指定的代码沙箱示例
public class CodeSandboxFactory {

    /*
     * 创建代码沙箱示例
     * @param type 沙箱类型
     * @return
     * */
    public static CodeSandbox NewInstance(String type) {
        switch (type) {
            // 示例代码沙箱
            case "example":
                return new ExampleCodeSandbox();
            // 远程代码沙箱
            case "remote":
                return new RemoteCodeSandbox();
            // 第三方代码沙箱
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}