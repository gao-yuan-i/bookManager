package com.example.bookmanager.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private String role;
    private Integer status;
    private Integer maxBorrow;
    private Integer borrowCount;
    private LocalDateTime createTime;
}