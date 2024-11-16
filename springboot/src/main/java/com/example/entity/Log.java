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
public class Log {
    private Integer id;
    /** 操作内容 */
    private String content;
    /** 操作人姓名 */
    private String name;
    /** 操作时间 */
    private String time;
    /** 操作人角色 */
    private String role;
    /** 操作ip*/
    private String ip;
}
