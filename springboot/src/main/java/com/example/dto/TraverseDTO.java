package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.example.entity.Traverse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TraverseDTO extends Traverse{
    /** 环公钥 */
    private String signPubKey;
    /** 签名数据 */
    private String signData;
    /** 姓名 */
    private String userName;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 年龄 */
    private Integer age;
    /** 性别 */
    private String sex;
    /** 身份证号 */
    private String idCard;
    /** 职业 */
    private String occupation;
    /** 医生姓名 */
    private String doctorName; 
    /** 医院名称 */
    private String hospitalName; 
    /** 病床名称 */
    private String bedName;
    /** 病房名称 */
    private String wardName;
}
