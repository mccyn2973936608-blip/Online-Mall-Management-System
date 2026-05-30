package com.mall.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效手机号")
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入有效邮箱")
    private String email;

    private String avatar;
    private String address;
}
