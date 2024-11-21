package com.example.common.enums;

public enum ReferalEnum {
    WAIT_OUT_ADMIN("待审批"),
    WAIT_IN_ADMIN("待审核"),
    WAIT_DOCTOR("待分配"),
    REFUSED_BY_OUT_ADMIN("拒绝转出"),
    REFUSED_BY_IN_ADMIN("拒绝转入"),
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
