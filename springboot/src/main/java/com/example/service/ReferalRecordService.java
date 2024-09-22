package com.example.service;


import cn.hutool.core.date.DateUtil;
import com.example.entity.ReferalRecord;
import com.example.mapper.ReferalRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
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

    public void add(ReferalRecord referalRecord) {
        referalRecord.setOutTime(DateUtil.now());
        referalRecordMapper.add(referalRecord);
    }


}