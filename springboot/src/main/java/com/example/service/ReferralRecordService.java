package com.example.service;

import cn.hutool.core.date.DateUtil;

import com.example.common.Result;
import com.example.common.enums.ReferralEnum;
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
    private ReferralRecordMapper referralRecordMapper;

    public PageInfo<ReferralRecord> selectPage(ReferralRecord referralRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ReferralRecord> list = referralRecordMapper.selectAll(referralRecord);
        return PageInfo.of(list);
    }

    public PageInfo<ReferralRecord> selectSuccess(ReferralRecord referralRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ReferralRecord> list = referralRecordMapper.selectSuccess(referralRecord);
        return PageInfo.of(list);
    }

    public List<ReferralRecord> selectAll(ReferralRecord referralRecord) {
        return referralRecordMapper.selectAll(referralRecord);
    }

    public ReferralRecord selectById(Integer id) {
        ReferralRecord referralRecord = new ReferralRecord();
        referralRecord.setId(id);
        return referralRecordMapper.selectAll(referralRecord).get(0);
    }


    public void update(ReferralRecord referralRecord) {
        referralRecordMapper.update(referralRecord);
    }

    public void deleteById(Integer id) {
        referralRecordMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            referralRecordMapper.deleteById(id);
        }
    }

    /**
     * 医生访问，新加一个转诊申请
     * @param referralRecord
     */
    public void add(ReferralRecord referralRecord) {
        // referralRecord.setResult(ReferralEnum.WAIT_OUT_ADMIN.toString());
        referralRecordMapper.add(referralRecord);
    }

    /**
     * 接收其他医院的转诊申请
     * @param referralRecord
     */
    public void recieveReferralRecord(ReferralRecord referralRecord) {
        referralRecord.setReferralStatus(ReferralEnum.WAIT_IN_ADMIN.toString());
        referralRecordMapper.add(referralRecord);
    }
}