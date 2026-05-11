package com.example.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookmanager.dto.BookDTO;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.exception.BusinessException;
import com.example.bookmanager.mapper.BookMapper;
import com.example.bookmanager.service.BookService;
import com.example.bookmanager.vo.BookVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public Page<BookVO> pageUserBooks(Integer page, Integer size, String keyword, String category) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getStatus, 1); // 只查上架的

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Book::getTitle, keyword)
                    .or()
                    .like(Book::getAuthor, keyword)
                    .or()
                    .like(Book::getIsbn, keyword));
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(Book::getCategory, category);
        }
        wrapper.orderByDesc(Book::getCreateTime);

        Page<Book> bookPage = baseMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToVOPage(bookPage, true);
    }

    @Override
    public BookVO getBookVOById(Long id) {
        Book book = baseMapper.selectById(id);
        if (book == null || book.getStatus() == 0) {
            throw new BusinessException("图书不存在或已下架");
        }
        return convertToVO(book, true);
    }

    @Override
    public Page<BookVO> pageAdminBooks(Integer page, Integer size, String keyword, String category) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Book::getTitle, keyword)
                    .or()
                    .like(Book::getAuthor, keyword)
                    .or()
                    .like(Book::getIsbn, keyword));
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(Book::getCategory, category);
        }
        wrapper.orderByDesc(Book::getCreateTime);

        Page<Book> bookPage = baseMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToVOPage(bookPage, false);
    }

    @Override
    @Transactional
    public void addBook(BookDTO bookDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        book.setAvailableQuantity(bookDTO.getTotalQuantity()); // 可用数量 = 总数量
        book.setStatus(1);
        baseMapper.insert(book);
    }

    @Override
    @Transactional
    public void updateBook(Long id, BookDTO bookDTO) {
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        // 记录原来的总数量
        int oldTotal = book.getTotalQuantity();
        int oldAvailable = book.getAvailableQuantity();

        BeanUtils.copyProperties(bookDTO, book);
        book.setId(id);

        // 如果总数量变了，相应地调整可用数量
        if (bookDTO.getTotalQuantity() != null && bookDTO.getTotalQuantity() != oldTotal) {
            int diff = bookDTO.getTotalQuantity() - oldTotal;
            int newAvailable = oldAvailable + diff;
            if (newAvailable < 0) {
                throw new BusinessException("总数量不能小于当前已借出数量");
            }
            book.setAvailableQuantity(newAvailable);
        }

        baseMapper.updateById(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }
        // 逻辑删除：下架
        book.setStatus(0);
        baseMapper.updateById(book);
    }

    /**
     * Book -> BookVO
     */
    private BookVO convertToVO(Book book, boolean isUserView) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        if (isUserView) {
            vo.setCanBorrow(book.getAvailableQuantity() > 0);
        }
        return vo;
    }

    private Page<BookVO> convertToVOPage(Page<Book> bookPage, boolean isUserView) {
        Page<BookVO> voPage = new Page<>(bookPage.getCurrent(), bookPage.getSize(), bookPage.getTotal());
        voPage.setRecords(bookPage.getRecords().stream()
                .map(book -> convertToVO(book, isUserView))
                .toList());
        return voPage;
    }
}