package com.example.bookmanager.controller;

import com.example.bookmanager.common.PageResult;
import com.example.bookmanager.common.Result;
import com.example.bookmanager.dto.BookDTO;
import com.example.bookmanager.dto.UserDTO;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.service.BorrowRecordService;
import com.example.bookmanager.service.UserService;
import com.example.bookmanager.vo.BookVO;
import com.example.bookmanager.vo.BorrowRecordVO;
import com.example.bookmanager.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;
    private final UserService userService;
    private final BorrowRecordService borrowRecordService;

    // ==================== 图书管理 ====================

    /**
     * 查询图书列表
     */
    @GetMapping("/books")
    public Result<PageResult<BookVO>> listBooks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        var bookPage = bookService.pageAdminBooks(page, size, keyword, category);
        return Result.success(PageResult.of(bookPage.getTotal(), bookPage.getRecords()));
    }

    /**
     * 查看图书详情
     */
    @GetMapping("/books/{id}")
    public Result<BookVO> getBook(@PathVariable Long id) {
        return Result.success(bookService.getBookVOById(id));
    }

    /**
     * 新增图书
     */
    @PostMapping("/books")
    public Result<Void> addBook(@Valid @RequestBody BookDTO bookDTO) {
        bookService.addBook(bookDTO);
        return Result.success("新增图书成功");
    }

    /**
     * 修改图书
     */
    @PutMapping("/books/{id}")
    public Result<Void> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        bookService.updateBook(id, bookDTO);
        return Result.success("修改图书成功");
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/books/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Result.success("删除图书成功");
    }

    // ==================== 用户管理 ====================

    /**
     * 查询用户列表
     */
    @GetMapping("/users")
    public Result<PageResult<UserVO>> listUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        var userPage = userService.pageUsers(page, size, keyword);
        return Result.success(PageResult.of(userPage.getTotal(), userPage.getRecords()));
    }

    /**
     * 查看用户详情
     */
    @GetMapping("/users/{id}")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserVOById(id));
    }

    /**
     * 新增用户
     */
    @PostMapping("/users")
    public Result<Void> addUser(@Valid @RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return Result.success("新增用户成功");
    }

    /**
     * 修改用户
     */
    @PutMapping("/users/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return Result.success("修改用户成功");
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        userService.updateUserStatus(id, status);
        return Result.success(status == 1 ? "用户已启用" : "用户已禁用");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除用户成功");
    }

    // ==================== 借阅管理 ====================

    /**
     * 查看所有借阅记录
     */
    @GetMapping("/borrows")
    public Result<PageResult<BorrowRecordVO>> listBorrows(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        var recordPage = borrowRecordService.pageAllBorrows(page, size, status);
        return Result.success(PageResult.of(recordPage.getTotal(), recordPage.getRecords()));
    }

    /**
     * 查看逾期未还记录
     */
    @GetMapping("/borrows/overdue")
    public Result<PageResult<BorrowRecordVO>> listOverdueBorrows(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        var recordPage = borrowRecordService.pageOverdueBorrows(page, size);
        return Result.success(PageResult.of(recordPage.getTotal(), recordPage.getRecords()));
    }
}