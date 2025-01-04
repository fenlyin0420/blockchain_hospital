package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferalRecord {
    private Integer id;
    private Integer userId;
    private Integer outHospitalId;
    private Integer inHospitalId;
    private Integer outDoctorId;
    private Integer inDoctorId;
    private String outTime;
    private String inTime;
    private String why;
    private String result;

    // 非数据库字段
    private String userName;
    private String outHospitalName;
    private String inHospitalName;
    private String outDoctorName;
    private String inDoctorName;
}

