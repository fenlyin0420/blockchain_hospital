package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
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
    private String advice;
    private String diagnosis;
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
    private String illnessDetail;
}
