package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralRecord {
    private Integer id;
    private Integer userId;
    private Integer outHospitalId;
    private Integer inHospitalId;
    private Integer outDoctorId;
    private Integer inDoctorId;
    private String outTime;
    private String inTime;
    private String reason;
    private String referalStatus;
    private String outHospitalName;
    private String inHospitalName;
    private String userName;
    // 非数据库字段
    private String outDoctorName;
    private String inDoctorName;
    private String communication;
    private String referralType;
    private String sex;
    private String age;
    private String idCard;
    private String phone;
    private String diagnosis;
    private String signatureUrl;
    private String outHospitalAdvice;
    private String referralStatus;
}

