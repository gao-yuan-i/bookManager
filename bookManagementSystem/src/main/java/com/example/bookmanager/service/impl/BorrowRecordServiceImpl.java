package com.example.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookmanager.entity.Book;
import com.example.bookmanager.entity.BorrowRecord;
import com.example.bookmanager.entity.User;
import com.example.bookmanager.enums.BorrowStatusEnum;
import com.example.bookmanager.exception.BusinessException;
import com.example.bookmanager.mapper.BookMapper;
import com.example.bookmanager.mapper.BorrowRecordMapper;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.BorrowRecordService;
import com.example.bookmanager.vo.BorrowRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord>
        implements BorrowRecordService {

    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void borrowBook(Long userId, Long bookId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() == 0) {
            throw new BusinessException("用户不存在或已被禁用");
        }

        LambdaQueryWrapper<BorrowRecord> overdueWrapper = new LambdaQueryWrapper<>();
        overdueWrapper.eq(BorrowRecord::getUserId, userId)
                .eq(BorrowRecord::getStatus, BorrowStatusEnum.BORROWING.getCode())
                .lt(BorrowRecord::getDueDate, LocalDateTime.now());
        if (baseMapper.selectCount(overdueWrapper) > 0) {
            throw new BusinessException("您有逾期未还的书籍，请先归还后再借");
        }

        if (user.getBorrowCount() >= user.getMaxBorrow()) {
            throw new BusinessException("借阅数量已达上限（" + user.getMaxBorrow() + "本），请先归还后再借");
        }

        Book book = bookMapper.selectById(bookId);
        if (book == null || book.getStatus() == 0) {
            throw new BusinessException("图书不存在或已下架");
        }
        if (book.getAvailableQuantity() <= 0) {
            throw new BusinessException("该图书已全部借出");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookMapper.updateById(book);

        user.setBorrowCount(user.getBorrowCount() + 1);
        userMapper.updateById(user);

        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusMonths(1));
        record.setStatus(BorrowStatusEnum.BORROWING.getCode());
        baseMapper.insert(record);
    }

    @Override
    @Transactional
    public void returnBook(Long userId, Long recordId) {
        BorrowRecord record = baseMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException("借阅记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("只能归还自己的借阅记录");
        }
        if (!BorrowStatusEnum.BORROWING.getCode().equals(record.getStatus())) {
            throw new BusinessException("该记录已归还或已逾期");
        }

        record.setReturnDate(LocalDateTime.now());
        record.setStatus(BorrowStatusEnum.RETURNED.getCode());
        baseMapper.updateById(record);

        Book book = bookMapper.selectById(record.getBookId());
        if (book != null) {
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookMapper.updateById(book);
        }

        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setBorrowCount(Math.max(0, user.getBorrowCount() - 1));
            userMapper.updateById(user);
        }
    }

    @Override
    public Page<BorrowRecordVO> pageMyBorrows(Long userId, Integer page, Integer size, Integer status) {
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getUserId, userId);
        if (status != null) {
            if (BorrowStatusEnum.OVERDUE.getCode().equals(status)) {
                wrapper.eq(BorrowRecord::getStatus, BorrowStatusEnum.BORROWING.getCode())
                        .lt(BorrowRecord::getDueDate, LocalDateTime.now());
            } else {
                wrapper.eq(BorrowRecord::getStatus, status);
            }
        }
        wrapper.orderByDesc(BorrowRecord::getCreateTime);

        Page<BorrowRecord> recordPage = baseMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToVOPage(recordPage);
    }

    @Override
    public Page<BorrowRecordVO> pageAllBorrows(Integer page, Integer size, Integer status) {
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(BorrowRecord::getStatus, status);
        }
        wrapper.orderByDesc(BorrowRecord::getCreateTime);

        Page<BorrowRecord> recordPage = baseMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToVOPage(recordPage);
    }

    @Override
    public Page<BorrowRecordVO> pageOverdueBorrows(Integer page, Integer size) {
        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getStatus, BorrowStatusEnum.BORROWING.getCode())
                .lt(BorrowRecord::getDueDate, LocalDateTime.now());
        wrapper.orderByDesc(BorrowRecord::getDueDate);

        Page<BorrowRecord> recordPage = baseMapper.selectPage(new Page<>(page, size), wrapper);
        return convertToVOPage(recordPage);
    }

    /**
     * 转换单条借阅记录为VO
     */
    private BorrowRecordVO convertToVO(BorrowRecord record) {
        BorrowRecordVO vo = new BorrowRecordVO();
        vo.setId(record.getId());
        vo.setUserId(record.getUserId());
        vo.setBookId(record.getBookId());
        vo.setBorrowDate(record.getBorrowDate());
        vo.setDueDate(record.getDueDate());
        vo.setReturnDate(record.getReturnDate());

        // 动态判断逾期
        if (BorrowStatusEnum.BORROWING.getCode().equals(record.getStatus())
                && record.getDueDate().isBefore(LocalDateTime.now())) {
            vo.setStatus(BorrowStatusEnum.OVERDUE.getCode());
            vo.setStatusDesc(BorrowStatusEnum.OVERDUE.getDesc());
        } else {
            vo.setStatus(record.getStatus());
            if (BorrowStatusEnum.BORROWING.getCode().equals(record.getStatus())) {
                vo.setStatusDesc(BorrowStatusEnum.BORROWING.getDesc());
            } else if (BorrowStatusEnum.RETURNED.getCode().equals(record.getStatus())) {
                vo.setStatusDesc(BorrowStatusEnum.RETURNED.getDesc());
            }
        }

        return vo;
    }

    /**
     * 批量转换为VO，并填充用户名和书名
     */
    private Page<BorrowRecordVO> convertToVOPage(Page<BorrowRecord> recordPage) {
        List<BorrowRecord> records = recordPage.getRecords();

        Page<BorrowRecordVO> voPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());

        if (records.isEmpty()) {
            voPage.setRecords(new ArrayList<>());
            return voPage;
        }

        // 批量获取用户ID
        List<Long> userIds = records.stream()
                .map(BorrowRecord::getUserId)
                .distinct()
                .collect(Collectors.toList());

        final Map<Long, User> userMap;
        if (userIds.isEmpty()) {
            userMap = new HashMap<>();
        } else {
            userMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, u -> u));
        }

        // 批量获取图书ID
        List<Long> bookIds = records.stream()
                .map(BorrowRecord::getBookId)
                .distinct()
                .collect(Collectors.toList());

        final Map<Long, Book> bookMap;
        if (bookIds.isEmpty()) {
            bookMap = new HashMap<>();
        } else {
            bookMap = bookMapper.selectBatchIds(bookIds).stream()
                    .collect(Collectors.toMap(Book::getId, b -> b));
        }

        // 组装VO列表
        List<BorrowRecordVO> voList = records.stream().map(record -> {
            BorrowRecordVO vo = convertToVO(record);
            User user = userMap.get(record.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setRealName(user.getRealName());
            }
            Book book = bookMap.get(record.getBookId());
            if (book != null) {
                vo.setBookTitle(book.getTitle());
                vo.setBookIsbn(book.getIsbn());
            }
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }
}