package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traverse {
    private Integer id;
    private String name;
    private Integer doctorId;
    private Integer hospitalId;
    private String advice;
    private String drug;
    private String inhospital;
    private String jurisdiction;
    private String doctorName;
    private String hospitalName;
    private String number;
    private String signData;
    private String signResult;
    private String signPubKey;
    private String signKey;
    private Date userDate;
}
