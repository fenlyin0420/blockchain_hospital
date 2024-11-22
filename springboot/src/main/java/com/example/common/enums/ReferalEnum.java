package com.example.common.enums;

public enum ReferalEnum {
    WAIT_OUT_ADMIN("等待转出院审批"),
    WAIT_IN_ADMIN("等待转入院审批"),
    WAIT_DOCTOR("等待接受"),
    ;
    public final String status;
    ReferalEnum(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
