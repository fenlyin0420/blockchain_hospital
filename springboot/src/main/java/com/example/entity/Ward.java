package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色用户父类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ward {
    private Integer id;
    /** 名称 */
    private String name;
    /** 价格 */
    private String price;
    /** 头像 */
    private String img;
    /** 介绍 */
    private String description;
    /** 剩余数量 */
    private Integer remain;

}
