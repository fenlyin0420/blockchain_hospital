package com.example.common.enums;

public enum CallEnum {
    STATUS_NO("未叫号"),
    STATUS_OK("已叫号"),
    ;

    public String status;

    CallEnum(String status) {
        this.status = status;
    }
}
