package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 床位信息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed {
    /** ID */
    private Integer id;
    /** ID */
    private Integer wardId;
    /** 病床名称 */
    private String bedName;
    /** 状态 */
    private String status;

    // 非数据库字段
    private String wardName;
    private String wardImg;
}
