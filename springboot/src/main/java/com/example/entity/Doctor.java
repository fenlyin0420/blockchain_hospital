package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 医生
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
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
    /** 个人简介 */
    private String description;
    /** 挂号费 */
    private Double price;
    /** 入职时间 */
    private String time;
    /** 职位 */
    private String position;
    /** 科室id */
    private Integer departmentId;
    private String publicKey;
    private String privateKey;

    // 非数据库属性
    /** 医院名称 */
    private String hospitalName;
    /** 星期 */
    private String week;
    /** 日期 */
    private Date date;
    /** 上午或下午 */
    private String period;
    /** 所属科室 */
    private String departmentName;
    /** 挂号剩余数量 */
    private Integer num;
    /** 用户挂号时选择的日期 */ // 临时作为医生的成员查一查吧
    private String selectedDate;
}