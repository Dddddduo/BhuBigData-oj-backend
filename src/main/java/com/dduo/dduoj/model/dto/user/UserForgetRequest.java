package com.dduo.dduoj.model.dto.user;

import lombok.Data;

@Data
public class UserForgetRequest {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 邮箱
     */
    private String userProfile;

}
