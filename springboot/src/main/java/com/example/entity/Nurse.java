package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nurse extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    //护士这里有个问题，就是护士-病房的表还没建立

    private Integer id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 头像 */
    private String avatar;
    /** 角色标识 */
    private String role;
    /** 医院id */
    private Integer hospitalId;
    /** 介绍 */
    private String description;
    /** 科室id */
    private Integer departmentId;
    /** 职位 */
    private String position;
    /** 入职时间 */
    private String time;
    private String publicKey;
    private String privateKey;

    //非数据库属性
    private String hospitalName;
    private String departmentName;

}
