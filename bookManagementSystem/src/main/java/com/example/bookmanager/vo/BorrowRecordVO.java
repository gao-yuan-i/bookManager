package com.example.bookmanager.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowRecordVO {
    private Long id;
    private Long userId;
    private String username;     // 借阅人用户名
    private String realName;     // 借阅人姓名
    private Long bookId;
    private String bookTitle;    // 书名
    private String bookIsbn;     // ISBN
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Integer status;
    private String statusDesc;   // 状态描述：借阅中/已归还/已逾期
}