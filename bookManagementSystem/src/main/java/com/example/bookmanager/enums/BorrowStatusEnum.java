package com.example.bookmanager.enums;

public enum BorrowStatusEnum {
    BORROWING(1, "借阅中"),
    RETURNED(2, "已归还"),
    OVERDUE(3, "已逾期");

    private final Integer code;
    private final String desc;

    BorrowStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}