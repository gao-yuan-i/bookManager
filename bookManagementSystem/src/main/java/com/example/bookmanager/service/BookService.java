package com.example.bookmanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookmanager.dto.BookDTO;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.vo.BookVO;

public interface BookService extends IService<Book> {

    /**
     * 分页查询图书（用户端，只查上架的）
     */
    Page<BookVO> pageUserBooks(Integer page, Integer size, String keyword, String category);

    /**
     * 获取图书详情（用户端）
     */
    BookVO getBookVOById(Long id);

    /**
     * 分页查询图书（管理员端，查询所有）
     */
    Page<BookVO> pageAdminBooks(Integer page, Integer size, String keyword, String category);

    /**
     * 新增图书
     */
    void addBook(BookDTO bookDTO);

    /**
     * 修改图书
     */
    void updateBook(Long id, BookDTO bookDTO);

    /**
     * 删除图书（下架）
     */
    void deleteBook(Long id);
}