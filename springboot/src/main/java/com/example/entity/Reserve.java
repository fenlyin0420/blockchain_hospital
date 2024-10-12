package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 预约挂号表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserve implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer doctorId;
    private Integer userId;
    private String time;
    private String status;

    private String doctorName;
    private String userName;
    /**医院id*/
    private Integer hospitalId;
    /**医院id*/
    private String hospitalName;
}