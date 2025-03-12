package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.utils.Encrypt;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traverse {
    private Integer id;
    /** 患者id */
    private Integer userId;
    private Integer doctorId;
    private Integer hospitalId;
    @Encrypt
    private String mainDiagnosis;
    @Encrypt
    private String secondaryDiagnosis;
    @Encrypt
    private String drug;
    private String inHospital;
    private String careStatus;
    /** 病床id */
    private Integer bedId;
    /** 时间戳 */
    private String timestamp;
    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date treatmentDate;
    /** 记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date recordDate;
    private String img;
    /** 病情描述 */
    @Encrypt
    private String illnessDetail;
    @Encrypt
    private String check;
    @Encrypt
    private String nonMedicine;
    @Encrypt
    private String care;
    @Encrypt
    private String diet;

    /**
     * 返回被 @encrypt 注解标注的字段
     * @return 被 @encrypt 注解标注的字段列表
     */
    @JsonIgnore
    public List<Field> getEncryptedFields() {
        List<Field> encryptedFields = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Encrypt.class)) {
                encryptedFields.add(field);
            }
        }
        return encryptedFields;
    }
}
