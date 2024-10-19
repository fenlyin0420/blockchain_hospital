package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 排班信息表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan implements Serializable {
    private static final long serialVersionUID = 1L;

    // 数据库属性
    /** ID */
    private Integer id;
    /**医院id*/
    private Integer hospitalId;
    private Integer doctorId;
    /** 挂号剩余量 */
    private Integer num;
    /** 排班时间 */
    private String date;
    private String week;

    private String doctorName;
    private String departmentName;
    /** 医院名称 */
    private String hospitalName;
}