package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 患者
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Account implements Serializable {
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
    /** 医院 id*/
    private Integer hospitalId;
    /** 账户余额 */
    private Double account;
    /** 患者公钥 */
    private String publicKey;
    /** 患者私钥 */
    private String privateKey;
    /** 年龄 */
    private Integer age;
    /** 性别 */
    private String sex;
    /** 身份证号 */
    private String idCard;
    /** 职业 */
    private String occupation;
}