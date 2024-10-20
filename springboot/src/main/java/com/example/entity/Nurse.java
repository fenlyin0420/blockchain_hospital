package com.example.entity;

import lombok.*;

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
    /** 医院名称 */
    private String hospitalName;
    private String description;
    private String time;
    private String position;
    private Integer departmentId;
    private String publicKey;
    private String privateKey;

}
