<<<<<<< HEAD:springboot/src/main/java/com/example/service/ReferralRecordService.java
package com.example.service;


import cn.hutool.core.date.DateUtil;

import com.example.common.Result;
import com.example.common.enums.ReferalEnum;
import com.example.entity.ReferralRecord;
import com.example.mapper.ReferralRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 转诊业务处理
 **/
@Service
public class ReferralRecordService {

    @Resource
    private ReferralRecordMapper referalRecordMapper;

    public PageInfo<ReferralRecord> selectPage(ReferralRecord referalRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ReferralRecord> list = referalRecordMapper.selectAll(referalRecord);
        return PageInfo.of(list);
    }


    public List<ReferralRecord> selectAll(ReferralRecord referalRecord) {
        return referalRecordMapper.selectAll(referalRecord);
    }

    public ReferralRecord selectById(Integer id) {
        ReferralRecord referalRecord = new ReferralRecord();
        referalRecord.setId(id);
        return referalRecordMapper.selectAll(referalRecord).get(0);
    }


    public void updateById(ReferralRecord referalRecord) {
        referalRecord.setInTime(DateUtil.now());
        
        referalRecordMapper.updateById(referalRecord);
    }

    public void deleteById(Integer id) {
        referalRecordMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            referalRecordMapper.deleteById(id);
        }
    }

    /**
     * 医生访问，新加一个转诊申请
     * @param referalRecord
     */
    public void add(ReferralRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.WAIT_OUT_ADMIN.toString());
        referalRecordMapper.add(referalRecord);
    }

    /**
     * 将申请发送到另一家医院
     * @param referralRecord
     * @return
     */
    public Result send(ReferralRecord referralRecord, String url) {
        // 设置转诊状态: 待审核
        referralRecord.setOutTime(DateUtil.now());
        referralRecord.setResult(ReferalEnum.WAIT_IN_ADMIN.toString());
        referalRecordMapper.updateById(referralRecord);

        referralRecord = referalRecordMapper.selectById(referralRecord.getId());

        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReferralRecord> body = new HttpEntity<>(referralRecord, headers);
        ResponseEntity<Result> res = restTemplate.postForEntity(url, body, Result.class);

        // 处理响应
        // if (res.getStatusCode().is2xxSuccessful()) {
        //     Result response = res.getBody();
        //     if (response.isSuccess()) {
        //         System.out.println("用户注册成功：" + response.getMessage());
        //     } else {
        //         System.out.println("用户注册失败：" + response.getMessage());
        //     }
        // } else {
        //     System.out.println("请求失败，状态码：" + responseEntity.getStatusCodeValue());
        // }

        Result response = res.getBody();
        return response;
    }

    /**
     * 接收其他医院的转诊申请
     * @param referralRecord
     */
    public void recieveReferralRecord(ReferralRecord referralRecord) {
        // 设置转诊状态： 待审核
        referralRecord.setResult(ReferalEnum.WAIT_IN_ADMIN.toString());
        referalRecordMapper.add(referralRecord);
    }
=======
package com.example.service;


import cn.hutool.core.date.DateUtil;

import com.example.common.Result;
import com.example.common.enums.ReferalEnum;
import com.example.entity.ReferalRecord;
import com.example.mapper.ReferalRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * 转诊业务处理
 **/
@Service
public class ReferalRecordService {

    @Resource
    private ReferalRecordMapper referalRecordMapper;

    public PageInfo<ReferalRecord> selectPage(ReferalRecord referalRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ReferalRecord> list = referalRecordMapper.selectAll(referalRecord);
        return PageInfo.of(list);
    }


    public List<ReferalRecord> selectAll(ReferalRecord referalRecord) {
        return referalRecordMapper.selectAll(referalRecord);
    }

    public ReferalRecord selectById(Integer id) {
        ReferalRecord referalRecord = new ReferalRecord();
        referalRecord.setId(id);
        return referalRecordMapper.selectAll(referalRecord).get(0);
    }


    public void updateById(ReferalRecord referalRecord) {
        referalRecord.setInTime(DateUtil.now());
        
        referalRecordMapper.updateById(referalRecord);
    }

    public void deleteById(Integer id) {
        referalRecordMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            referalRecordMapper.deleteById(id);
        }
    }

    /**
     * 医生访问，新加一个转诊申请
     * @param referalRecord
     */
    public void add(ReferalRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.WAIT_OUT_ADMIN.toString());
        referalRecordMapper.add(referalRecord);
    }

    /**
     * 将申请发送到另一家医院
     * @param referalRecord
     * @return
     */
    public Result send(ReferalRecord referalRecord, String url) {
        // 设置转诊状态
        referalRecord.setOutTime(DateUtil.now());
        referalRecord.setResult(ReferalEnum.WAIT_IN_ADMIN.toString());
        referalRecordMapper.updateById(referalRecord);

        referalRecord = referalRecordMapper.selectById(referalRecord.getId());

        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReferalRecord> body = new HttpEntity<>(referalRecord, headers);
        ResponseEntity<Result> res = restTemplate.postForEntity(url, body, Result.class);

        // 处理响应
        // if (res.getStatusCode().is2xxSuccessful()) {
        //     Result response = res.getBody();
        //     if (response.isSuccess()) {
        //         System.out.println("用户注册成功：" + response.getMessage());
        //     } else {
        //         System.out.println("用户注册失败：" + response.getMessage());
        //     }
        // } else {
        //     System.out.println("请求失败，状态码：" + responseEntity.getStatusCodeValue());
        // }

        Result response = res.getBody();
        return response;
    }

    /**
     * 接收其他医院的转诊申请
     * @param referalRecord
     */
    public void recieveReferalRecord(ReferalRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.WAIT_IN_ADMIN.toString());
        referalRecordMapper.add(referalRecord);
    }
>>>>>>> c6ddd2616c8f3e7eaecdf178ff7d23f80d24385b:springboot/src/main/java/com/example/service/ReferalRecordService.java
}