package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    /** 时间戳 */
    private String timestamp;
    private String name;
    private String username;
    private String phone;
    private String no;
    private String telephone;
    private String completedSites;
    private String uncompletedSites;
    private Integer id;
    private String type;
    private String receiverId;
    private Integer pageNum;
    private Integer pageSize;
    private String carNum;
    private String nameOfMaterial;
    private String uploaderName;
    private String receivingStatus;
    private String image;
    private String jobNumber;
    private String job;
    private String password;
    private String role;
    private String activityName;
    private String initiator;
    private String startTime;
    private String endTime;
    private String activityInformation;
    private String runningState;
    private String activityId;
    private String participant;
    private String auditStatus;
    private String actName;
    private String signData;
    private String signKey;
    private String vote;
    private String signNum;
    private String activityIntroduction;
    private String advice;
    private String drug;
}
