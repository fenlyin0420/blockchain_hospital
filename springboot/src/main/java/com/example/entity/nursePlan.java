package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

/**
 * 排班信息表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class nursePlan implements Serializable {
    private static final long serialVersionUID = 1L;

    // 数据库属性
    /** ID */
    private Integer id;
    /**医院id*/
    private Integer hospitalId;
    private Integer nurseId;
    /** 挂号剩余量 */
    private Integer num;
    /** 排班时间 */
    private Date date;

    // 非数据库属性
    private String nurseName;
    private String departmentName;
    private Integer departmentId;
    private String hospitalName;
}