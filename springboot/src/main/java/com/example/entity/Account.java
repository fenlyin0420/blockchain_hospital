package com.example.entity;

import lombok.*;

/**
 * 角色用户父类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Integer id;
    /** 用户名 */
    private String username;
    /** 名称 */
    private String name;
    /** 密码 */
    private String password;
    /** 角色标识 */
    private String role;
    /** 新密码 */
    private String newPassword;
    /** 头像 */
    private String avatar;

    private String token;
    private String publicKey;
    private String privateKey;
    /**医院id*/
    private Integer hospitalId;
    /**医院id*/
    private String hospitalName;
}
