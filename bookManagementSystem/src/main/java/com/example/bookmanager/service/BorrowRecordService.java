package com.example.bookmanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.vo.BorrowRecordVO;

public interface BorrowRecordService extends IService<BorrowRecord> {

    /**
     * 借书
     */
    void borrowBook(Long userId, Long bookId);

    /**
     * 还书
     */
    void returnBook(Long userId, Long recordId);

    /**
     * 查询我的借阅记录
     */
    Page<BorrowRecordVO> pageMyBorrows(Long userId, Integer page, Integer size, Integer status);

    /**
     * 管理员查询所有借阅记录
     */
    Page<BorrowRecordVO> pageAllBorrows(Integer page, Integer size, Integer status);

    /**
     * 管理员查询逾期记录
     */
    Page<BorrowRecordVO> pageOverdueBorrows(Integer page, Integer size);
}