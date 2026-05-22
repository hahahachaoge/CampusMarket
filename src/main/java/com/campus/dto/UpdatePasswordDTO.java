package com.campus.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    //旧密码
    private String oldPassword;
    //新密码
    private String newPassword;
}
