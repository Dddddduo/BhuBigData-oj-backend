package com.dduo.dduoj.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求
 *
 * @author <a href="https://github.com/lidduo">程序员鱼皮</a>
 * @from <a href="https://dduo.icu">编程导航知识星球</a>
 */
@Data
public class UserUpdateRequest implements Serializable {

    private String userAccount;

    private String userCode;

    private String userNewPassword;

    private String userCheckPassword;

    private static final long serialVersionUID = 1L;
}