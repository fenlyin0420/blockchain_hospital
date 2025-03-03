package com.example.common.enums;

public enum InHospitalEnum {
    InHospital_YES("待住院"),
    InHospital_NO("未住院"),
            ;
    public String status;

    InHospitalEnum(String status) {
        this.status = status;
    }
}
