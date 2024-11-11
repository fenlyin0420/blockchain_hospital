package com.example.common.enums;

public enum InhospitalEnum {
    Inhospital_YES("待住院"),
    Inhospital_NO("未住院"),
            ;
    public String status;

    InhospitalEnum(String status) {
        this.status = status;
    }
}
