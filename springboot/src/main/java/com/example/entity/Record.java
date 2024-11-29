package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 就诊记录表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer userId;
    private Integer doctorId;
    private String time;
    private String medicalRecord;
    private String inhospital;
    private String inhostpitalRecord;

    private String userName;
    private String doctorName;
    /**医院id*/
    private Integer hospitalId;
    /**医院id*/
    private String hospitalName;

    private String departmentName;
}