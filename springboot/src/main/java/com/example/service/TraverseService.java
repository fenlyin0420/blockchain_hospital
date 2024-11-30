package com.example.service;

import com.example.entity.ReferalRecord;
import com.example.entity.Traverse;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.TraverseMapper;
import com.example.mapper.UserMapper;
import com.example.utils.JwtSm.MySM2Util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.List;

@Slf4j
@Service
public class TraverseService {

    @Resource
    private TraverseMapper traverseMapper;

    @Resource
    private UserMapper userMapper;

    public List<Traverse> selectAll(Traverse traverse) {
        return traverseMapper.selectAll(traverse);
    }

    public PageInfo<Traverse> selectPage(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Traverse> list = traverseMapper.selectAll(traverse);
        //log.info("list:{}",list);
        return PageInfo.of(list);
    }

    public Traverse selectById(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByName(String name) {
        Traverse traverse = new Traverse();
        traverse.setUserName(name);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByDoctorId(Integer id) {
        Traverse traverse = new Traverse();
        traverse.setDoctorId(id);
        return traverseMapper.selectAll(traverse).get(0);
    }

    public Traverse selectByHospitalId(Integer id) {
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
}