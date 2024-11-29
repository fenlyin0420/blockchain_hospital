package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公告信息表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer userId;
    private String room;
    private Double price;
    private String status;
    private String medicine;
    private String hosStatus;

    private String userName;
    /**医院id*/
    private Integer hospitalId;
    /**医院id*/
    private String hospitalName;
}