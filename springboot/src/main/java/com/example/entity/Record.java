package com.example.entity;

import lombok.Data;

@Data
public class Record {
    private Integer id;
    private Integer userId;
    private Integer doctorId;
    private String time;
    private String medicalRecord;
    private String inhospital;
    private String inhostpitalRecord;
    private Integer hospitalId;
    
    // 非数据库字段
    private String userName;
    private String doctorName;
    private String hospitalName;
    private String departmentName;
}