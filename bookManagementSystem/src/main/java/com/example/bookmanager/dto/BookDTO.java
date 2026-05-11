package com.example.bookmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookDTO {
    private String isbn;

    @NotBlank(message = "书名不能为空")
    private String title;

    private String author;
    private String publisher;
    private LocalDate publishDate;
    private String category;
    private String description;
    private String coverImage;

    @Min(value = 1, message = "数量至少为1")
    private Integer totalQuantity;

    private String location;
}