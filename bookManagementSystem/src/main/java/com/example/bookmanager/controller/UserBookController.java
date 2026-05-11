package com.example.bookmanager.controller;

import com.example.bookmanager.common.PageResult;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.service.BorrowRecordService;
import com.example.bookmanager.service.UserService;
import com.example.bookmanager.vo.BookVO;
import com.example.bookmanager.vo.BorrowRecordVO;
import com.example.bookmanager.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserBookController {

    private final BookService bookService;
    private final BorrowRecordService borrowRecordService;
    private final UserService userService;

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    // ==================== 图书相关 ====================

    /**
     * 查询图书列表（支持搜索）
     */
    @GetMapping("/books")
    public Result<PageResult<BookVO>> listBooks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        var bookPage = bookService.pageUserBooks(page, size, keyword, category);
        return Result.success(PageResult.of(bookPage.getTotal(), bookPage.getRecords()));
    }

    /**
     * 查看图书详情
     */
    @GetMapping("/books/{id}")
    public Result<BookVO> getBook(@PathVariable Long id) {
        return Result.success(bookService.getBookVOById(id));
    }

    // ==================== 借阅相关 ====================

    /**
     * 借书
     */
    @PostMapping("/borrow/{bookId}")
    public Result<Void> borrowBook(@PathVariable Long bookId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        borrowRecordService.borrowBook(userId, bookId);
        return Result.success("借书成功");
    }

    /**
     * 还书
     */
    @PostMapping("/return/{recordId}")
    public Result<Void> returnBook(@PathVariable Long recordId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        borrowRecordService.returnBook(userId, recordId);
        return Result.success("还书成功");
    }

    /**
     * 查看我的借阅记录
     */
    @GetMapping("/my-borrows")
    public Result<PageResult<BorrowRecordVO>> myBorrows(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        var recordPage = borrowRecordService.pageMyBorrows(userId, page, size, status);
        return Result.success(PageResult.of(recordPage.getTotal(), recordPage.getRecords()));
    }

    // ==================== 个人信息相关 ====================

    /**
     * 查看个人信息
     */
    @GetMapping("/my-info")
    public Result<UserVO> myInfo(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(userService.getUserVOById(userId));
    }

    /**
     * 修改个人信息
     */
    @PutMapping("/my-info")
    public Result<Void> updateMyInfo(@RequestBody com.example.bookmanager.dto.UserDTO userDTO,
                                     HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        userService.updateMyInfo(userId, userDTO);
        return Result.success("修改成功");
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> params,
                                       HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功");
    }
}