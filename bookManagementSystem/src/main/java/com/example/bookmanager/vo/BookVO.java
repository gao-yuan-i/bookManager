package com.example.bookmanager.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookVO {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private String category;
    private String description;
    private String coverImage;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private String location;
    private Integer status;
    private Boolean canBorrow;  // 用户端是否可借
    private LocalDateTime createTime;
}