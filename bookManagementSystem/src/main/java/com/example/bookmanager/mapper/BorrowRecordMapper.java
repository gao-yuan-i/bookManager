package com.example.bookmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmanager.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
}