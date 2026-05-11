package com.example.bookmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;
    private String realName;
    private String phone;
    private String email;
    private String role;
    private Integer maxBorrow;
}