package com.example.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockChainTraverseDTO {
    private String idCard;
    private String userName;
    private String doctorName;
    private String hospitalName;
    private String advice;
    private String diagnosis;
    private String drug;
    private String inHospital;
    private String timestamp;
    private String treatmentDate;
    private String recordDate;
    private String img;
    private String illnessDetail;
}
