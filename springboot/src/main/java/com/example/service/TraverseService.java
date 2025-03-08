package com.example.service;

import com.example.common.enums.InHospitalEnum;
import com.example.dto.TraverseDTO;
import com.example.entity.ReferralTraverse;
import com.example.entity.Traverse;
import com.example.exception.CustomException;
import com.example.mapper.TraverseMapper;
import com.example.mapper.UserMapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TraverseService {

    @Resource
    private TraverseMapper traverseMapper;

    @Resource
    private UserMapper userMapper;

    public List<TraverseDTO> selectAll(Traverse traverse) {
        return traverseMapper.selectAll(traverse);
    }

    public PageInfo<TraverseDTO> selectPage(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TraverseDTO> list = traverseMapper.selectAll(traverse);
        Collections.reverse(list);
        return PageInfo.of(list);
    }

    public TraverseDTO selectById(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public TraverseDTO selectByDoctorId(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setDoctorId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public TraverseDTO selectByHospitalId(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setHospitalId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public List<Traverse> selectByCanSend(Integer id) {
        return traverseMapper.selectByCanSend(id);
    }
    /**
     * 插入一条病历
     * @param traverse
     * @throws ClassCastException
     * @return 病历id
     */
    public Integer add(Traverse traverse) throws ClassCastException{
        if(traverse.getInHospital().equals("是")){
            traverse.setInHospital(InHospitalEnum.InHospital_YES.status);
        }else {
            traverse.setInHospital(InHospitalEnum.InHospital_NO.status);
        }
        Integer success = traverseMapper.add(traverse);
        if(success == 0)
            throw new CustomException("插入病历失败");
        Integer id = traverseMapper.selectLastInsertId();
        return id;
    }

    public void updateById(Traverse traverse) {
        traverseMapper.updateById(traverse);
    }

    public void deleteById(Integer id) {
        traverseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id : ids){
            traverseMapper.deleteById(id);
        }
    }

    public PageInfo<ReferralTraverse> selectPageReferralTraverse(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<ReferralTraverse> list = traverseMapper.selectAlReferralTraverse();
        return PageInfo.of(list);
    }

    public void insertReferralTraverse(ReferralTraverse entity) {
        traverseMapper.insertReferralTraverse(entity);
    }
}