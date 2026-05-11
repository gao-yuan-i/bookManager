package com.example.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("books")
public class Book {
    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}