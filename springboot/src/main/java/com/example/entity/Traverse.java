package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.lang.reflect.Field;
import java.util.List;

import com.example.utils.Sign;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traverse {
    private Integer id;
    /** 患者id */
    private Integer userId;
    private Integer doctorId;
    private Integer hospitalId;
    @Sign
    private String advice;
    @Sign
    private String diagnosis;
    @Sign
    private String drug;
    @Sign
    private String inHospital;
    private String careStatus;
    /** 病床id */
    private Integer bedId;
    /** 时间戳 */
    private String timestamp;
    /** 签名数据 */
    private String signData;
    /** 签名结果 */
    private String signResult;
    private String signPubKey;
    private String signKey;
    /** 就诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date treatmentDate;
    private String img;
    /** 是否有效 */
    private String isValid;

    // 非数据库字段
    /** 患者姓名（可能重复） */
    private String userName;
    private String doctorName; 
    private String hospitalName; 
    private String bedName;
    private String wardName;

    public String signData(){
        StringBuilder result = new StringBuilder();
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> concatenatedParts = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Sign.class)) {
                field.setAccessible(true);
                try {
                    // 检查 value 是否为 null
                    Object value = field.get(this);
                    String valueStr = (value != null) ? value.toString() : "";
                    concatenatedParts.add(field.getName() + ":" + valueStr);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < concatenatedParts.size(); i++) {
            result.append(concatenatedParts.get(i));
            if (i < concatenatedParts.size() - 1) {
                result.append(",");
            }
        }
        System.out.println(result.toString());
        return result.toString();
    }
}
