package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 就诊记录表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer userId;
    private Integer doctorId;
    private String time;
    private String medicalRecord;
    private String inhospital;
    private String inhostpitalRecord;

    private String userName;
    private String doctorName;
    /**医院id*/
    private Integer hospitalId;
    /**医院id*/
    private String hospitalName;

    private String departmentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getInhospital() {
        return inhospital;
    }

    public void setInhospital(String inhospital) {
        this.inhospital = inhospital;
    }

    public String getInhostpitalRecord() {
        return inhostpitalRecord;
    }

    public void setInhostpitalRecord(String inhostpitalRecord) {
        this.inhostpitalRecord = inhostpitalRecord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}