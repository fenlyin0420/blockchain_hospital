package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traverse {
    private Integer id;
    /** 患者姓名 */
    private String name;
    private Integer doctorId;
    private Integer hospitalId;
    private String advice;
    private String drug;
    private String inhospital;
    private String jurisdiction;
    private String doctorName; //非数据库字段
    private String hospitalName;
    private String careStatus;
    private Integer wardId;
    private String wardName; //非数据库字段
    /** 时间戳 */
    private String number;
    private String signData;
    private String signResult;
    private String signPubKey;
    private String signKey;
    private Date userDate;
    private String img;
}
