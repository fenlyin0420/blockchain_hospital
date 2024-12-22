package com.example.common.enums;

public enum ReferalEnum {
    /** 待审批 */
    WAIT_OUT_ADMIN("待审批"),
    /** 待审核 */
    WAIT_IN_ADMIN("待审核"),
    /** 待分配 */
    WAIT_DOCTOR("待分配"),
    /** 拒绝转出 */
    REFUSED_BY_OUT_ADMIN("拒绝转出"),
    /** 拒绝转入 */
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
