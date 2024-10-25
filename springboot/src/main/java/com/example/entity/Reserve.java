package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 挂号记录
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserve implements Serializable {
    private static final long serialVersionUID = 1L;

    // 数据库成员
    /** ID */
    private Integer id;
    private Integer doctorId;
    private Integer userId;
    private String time;
    private String status;
    /** 医院 id*/
    private Integer hospitalId;

    // 非数据库成员
    private String doctorName;
    private String userName;
    private String hospitalName;
    private String departmentName;
}