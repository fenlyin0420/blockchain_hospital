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

    /** ID */
    private Integer id;
    private Integer doctorId;
    private Integer num;
    private String week;

    private String doctorName;
    private String departmentName;
    /**医院id*/
    private Integer hospitalId;
    /**医院id*/
    private String hospitalName;
}