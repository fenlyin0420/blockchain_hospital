package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traverse {
    private Integer id;
    /** 患者id */
    private Integer userId;
    private Integer doctorId;
    private Integer hospitalId;
    private String advice;
    private String drug;
    private String inHospital;
    private String careStatus;
    /** 病房id */
    private Integer wardId;
    /** 时间戳 */
    private String timestamp;
    /** 签名数据 */
    private String signData;
    /** 签名结果 */
    private String signResult;
    private String signPubKey;
    private String signKey;
    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date treatmentDate;
    private String img;

    // 非数据库字段
    /** 患者姓名（可能重复） */
    private String userName;
    private String doctorName; 
    private String hospitalName; 
    private String wardName; 
}
