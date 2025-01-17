package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralTraverse {
    private String idCard;
    private String timestamp;
    private String treatmentDate;
    private String userName;
    private String doctorName;
    private String hospitalName;
    private String img;
    private String diagnosis;
    private String advice;
    private String drug;
    private String inHospital;
    private String careStatus;
    private String referralReason;
    private String signKey;
    private String signResult;
}